### Task 用法


- 一个Task代表一个构建工作的原子操作，例如编译calsses或者生成javadoc。
Gradle中，每一个待编译的工程都叫一个Project。每一个Project在构建的时候都包含一系列的Task。比如一个Android APK的编译可能包含：Java源码编译Task、资源编译Task、JNI编译Task、lint检查Task、打包生成APK的Task、签名Task等。插件本身就是包含了若干Task的。
如下就是一个task的简单例子：



    //脚本运行的时候就会不调用
    task initTask{
    
        println('***初始化模块***')
    
    }

    /**在终端手动调用的时候才会执行 ./gradlew releaseJar*/
    task releaseJar <<{
        println('***创建jar包***')
    }



//如果代码没有加<<，则这个任务在脚本initialization的时候执行 ./gradlew releaseJar*/


//这和我们调用task这个函数的方式有关！如果没有<<，则闭包在task函数返回前会执行，而如果加了<<，则变成调用myTask.doLast添加一个Action了，自然它会等到grdle myTask的时候才会执行！

    
    task myTask
    task myTask { configure closure }  // closure是一个闭包
    task myType << { task action }    // <<符号是doLast的缩写  
    task myTask(type: SomeType)   // SomeType可以指定任务类型，Gradle本身提供有Copy、Delete、Sync等
    task myTask(type: SomeType) { configure closure }

- 一个Task包含若干Action。所以，Task有doFirst和doLast两个函数，用于添加需要最先执行的Action和需要和需要最后执行的Action。Action就是一个闭包。闭包，英文叫Closure，是Groovy中非常重要的一个数据类型或者说一种概念。
- Task创建的时候可以通过 type: SomeType 指定Type，Type其实就是告诉Gradle，这个新建的Task对象会从哪个基类Task派生。比如，Gradle本身提供了一些通用的Task，最常见的有Copy 任务。Copy是Gradle中的一个类。当我们：task myTask(type:Copy)的时候，创建的Task就是一个Copy Task。
- 当我们使用 taskmyTask{ xxx}的时候，花括号就是一个closure。
- 当我们使用taskmyTask << {xxx}的时候，我们创建了一个Task对象，同时把closure做为一个action加到这个Task的action队列中，并且告诉它“最后才执行这个closure”


### Type
####Copy
- 将文件复制到目标目录。此任务在复制时也可以执行重命名和过滤文件操作。它实现了CopySpec接口，使用CopySpec.from()方法可以指定源文件，CopySpec.into()方法可以指定目标目录。
    
    
    task copyDocs(type: Copy) {
        from 'src/main/doc'
        into 'build/target/doc'
    }
    
    //这是个Ant filter
    import org.apache.tools.ant.filters.ReplaceTokens
    
    //这是一个闭包
    def dataContent = copySpec {
        from 'src/data'
        include '*.data'
    }
    
    task initConfig(type: Copy) {
        from('src/main/config') {
            include '**/*.properties'
            include '**/*.xml'
            filter(ReplaceTokens, tokens: [version: '2.3.1'])
        }
        from('src/main/config') {
            exclude '**/*.properties', '**/*.xml'
        }
        from('src/main/languages') {
            rename 'EN_US_(.*)', '$1'
        }
        into 'build/target/config'
        exclude '**/*.bak'
    
        includeEmptyDirs = false
    
        with dataContent
    }

- 替换AndroidManifest文件
    
    
    task chVer(type: Copy) { // 指定Type为Copy任务
        from "src/main/manifest/AndroidManifestCopy.xml"  // 复制src/main/manifest/目录下的AndroidManifest.xml
        into 'src/main'  // 复制到指定目标目录
        rename { String fileName -> //在复制时重命名文件
            fileName = "AndroidManifest.xml" // 重命名
        }
    
    }
- 替换so文件


        task chSo(type: Copy) {
            from "src/main/jniLibs/test"   // 复制test文件夹下的所有so文件
            into "src/main/jniLibs/armeabi-v7a" //复制到armeabi-v7a文件夹下
        }
        这样每次打包APK前执行以上任务就可以自动替换文件啦！

问：那如果有多个任务需要执行是不是要执行多次任务呢？
答：可以通过多任务命令调用一次即可。

    gradlew task1 task2 [...]

问：任务名太长不想输入这么多字怎么办?
答：可以采用简化操作，但是必须保证可以唯一区分出该任务的字符，如：

    gradlew cV

问：那我不想每次打包前都输入命令怎么办？
答：可以每次build时自动执行自定义任务。

    afterEvaluate {
        tasks.matching {
            // 以process开头以ReleaseJavaRes或DebugJavaRes结尾的task
            it.name.startsWith('process') && (it.name.endsWith('ReleaseJavaRes') || it.name.endsWith
                    ('DebugJavaRes'))
       }.each { task ->
            task.dependsOn(chVer, chSo)  // 任务依赖：执行task之前需要执行dependsOn指定的任务
        }
    }
####Sync
- 此任务与Copy任务类似，唯一的区别是当执行时会复制源文件到目标目录，目标目录中所有非复制文件将会被删除，除非指定Sync.preserve(org.gradle.api.Action)。
例子：


    task syncDependencies(type: Sync) {
        from 'my/shared/dependencyDir'
        into 'build/deps/compile'
    }
    
    // 你可以保护目标目录已经存在的文件。匹配的文件将不会被删除。
    task sync(type: Sync) {
        from 'source'
        into 'dest'
        preserve {
            include 'extraDir/**'
            include 'dir1/**'
            exclude 'dir1/extra.txt'
        }
    }

#### Zip
- 创建ZIP归档文件，默认压缩文件类型为zip。
例子：

    task zip(type: Zip) {
        from 'src/dist'
        into('libs') 
    }
    
###自定义Task
    
    // 需要继承自DefaultTask
    class HelloWorldTask extends DefaultTask {
        // @Optional 表示在配置该Task时，message是可选的。
        @Optional
        String message = 'I am kaku'
        // @TaskAction 表示该Task要执行的动作,即在调用该Task时，hello()方法将被执行
        @TaskAction
        def hello(){
            println "hello world $message"
        }
    }
    
    // hello使用了默认的message值
    task hello(type:HelloWorldTask)
    
    // 重新设置了message的值
    task helloOne(type:HelloWorldTask){
       message ="I am a android developer"
    }
 