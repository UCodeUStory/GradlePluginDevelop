## Gradle插件开发介绍


####  Gradle详解：

    这一次一定要系统掌握，你准备好了吗？
    
    
- [初识Gradle 和 领域专用语言](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/day01.gradle)
- [Gradle 版本配置](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/day02.gradle)
- [Gradle 模块配置](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/day03.gradle)
- [Gradle 插件分类](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/day04.gradle)
- [Gradle Android插件包含的内容](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/android.gradle)
- [Gradle 统一配置你的版本号](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/version.gradle)
- [Gradle 分渠道打包](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/productflavor.gradle)
- [Gradle 配置你的AndroidManifest](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/android.gradle)
- Gradle 编译器动态生成java 配置
- [Gradle 指定你的源码路径](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/sourceSet.gradle)
- Gradle 改变项目依赖路径
- Gradle lintOption
- Gradle 指定使用的java版本
- Gradle 优化项目 （代码混淆、
- [Gradle gradle.properties 配置gradle版本和buildTools版本，和一些不便的版本](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/properties.gradle)
- [Gradle 使用variantFilter修改生成apk](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/renameapk.gradle)
- [Gradle 指定java版本](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/set_java_version.gradle)

- Gradle 生成测试报告
- Gradle 生成接口文档
- AAR 生成
- jar 生成
- 元编程

  
  
使用Android Studio开发Gradle插件

- 1. 创建一个Android Library类型的Moudle；例如mygradleplugin
- 2. 将此Module的java修改改成groovy
- 3. 包名自己可以修改
- 4. 此Moudle下build.gradle内容清空，添加如下代码




        apply plugin: 'groovy'
        apply plugin: 'maven'
    
        dependencies {
            compile gradleApi()
            compile localGroovy()
        }
    
        repositories {
            mavenCentral()
        }
    
        // group和version在后面使用自定义插件的时候会用到 可以随便起
        group='com.micky'
        version='1.0.0'
        
        // 上传本地仓库的task，
        uploadArchives {
            repositories {
                mavenDeployer {
                //本地仓库的地址，自己随意选，但使用的时候要保持一致，这里就是当前项目目录
                    repository(url: uri('../repo'))
                }
            }
        }
        
    
- 5. 在groovy路径下创建一个MyCustomPlugin.groovy,新建文件一定要带后缀名
 

        class MyCustomPlugin implements Plugin<Project> {
            void apply(Project project) {
                System.out.println("这是自定义插件");
                project.task('myTask') << {
                    println "Hi this is micky's plugin"
                }
            }
        }
    
- 6. 现在，我们已经定义好了自己的gradle插件类，接下来就是告诉gradle，哪一个是我们自定义的插件类，因此，需要在main目录下新建resources目录，然后在resources目录里面再新建META-INF目录，再在META-INF里面新建gradle-plugins目录。最后在gradle-plugins目录里面新建properties文件，注意这个文件的命名，你可以随意取名，(这里起名com.micky.mycustom.properties)但是后面使用这个插件的时候，会用到这个名字。比如，你取名为com.hc.gradle.properties，而在其他build.gradle文件中使用自定义的插件时候则需写成：apply plugin: 'com.hc.gradle'



- 7. 然后在com.micky.mycustom.properties文件里面指明你自定义的类


        implementation-class='com.micky.plugin.MyCustomPlugin

- 8. 执行 gradle uploadArchives 上传到本地仓库会生成jar

##### 然后在项目的app目录下的build.gradle 使用插件



            //引入依赖       
            buildscript {
                repositories {
                    maven {
                        url uri('../repo')
                    }
                    jcenter()
                }
            
               //这里和插件定义要一致，插件中name未指定就为默认项目名
                dependencies {
                    classpath group: 'com.micky',
                            name: 'mygradleplugin',
                            version: '1.0.1'
                }
            }



            //这个名字一定要对应上.properties文件名
            apply plugin: 'com.micky.mycustom'

- 最后 先clean project(很重要！),然后再make project.从messages窗口打印如下信息



# 新增Gradle Transform
监听文件编译结束，通过
javasist实现字节码修改,实现代码插入,通过这种插件化的AOP 和代码中使用Aspectj 区别就是，避免代码碎片化，添加一个功能修改多处代码，即使用了Aspectj 也许要在修改的地方添加注解，当修改处很多的时候很不方便，通过transform和javassist 可以遍历整个工程，按照满足条件的一次性修改，并且以后我们可以写成通用性的组建，比如自动注册一些组建在所有Activity，里面Javassist用了反射原理，但是这是编译器，不损失效率,Javassist非常强大，需要仔细学习

1.新建一个MyTransform 再新建一个插件MyPlugin注册这个Transform

Mytransform 重写transform方法
里面要将输入内容复制给输出，否者报错，这是第一步，其实就是相当于在运行我们给拦截了，必须再把内容输出出去才能打包成dex

里面遍历

// Transform的inputs有两种类型，一种是目录，一种是jar包，要分开遍历
        inputs.each { TransformInput input ->
            //对类型为“文件夹”的input进行遍历
            input.directoryInputs.each { DirectoryInput directoryInput ->
                //文件夹里面包含的是我们手写的类以及R.class、BuildConfig.class以及R$XXX.class等

                /**
                 * 这里就统一处理一些逻辑，避免代码分散，碎片化
                 */

                println("transform transformsalkfjdl;kajf#####################*********")
                println(directoryInput.file.absolutePath)
                MyInject.injectDir(directoryInput.file.absolutePath,"com\\wangpos\\test",project)

                // 获取output目录
                def dest = outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes,
                        Format.DIRECTORY)

                // 将input的目录复制到output指定目录
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
            //对类型为jar文件的input进行遍历
            input.jarInputs.each { JarInput jarInput ->

                //jar文件一般是第三方依赖库jar文件

                // 重命名输出文件（同目录copyFile会冲突）
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                //生成输出路径
                def dest = outputProvider.getContentLocation(jarName + md5Name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)
                //将输入内容复制到输出
                FileUtils.copyFile(jarInput.file, dest)
            }
####         }

里面都是groovy代码，也可以使用java代码，看哪个方便吧

# 问题总结

 1.找不到依赖库，需要在repositories种添加jcenter()
 2.还有javassist找不到jar包，就是需要javassist引入jar包
 3.发现生成的apk没有变化，删除了build目录重新build，仍然无变化，点击Android Studio setting 清理缓存，重新启动

# Gradle介绍

-    1.删除无用资源  shrinkResources true 一般在release里配置

-    2.zipalign优化  zipAlignEnabled true zipalign优化的最根本目的是帮助操作系统更高效率的根据请求索引资源 一般在release里配置
-
-    3.混淆  minifyEnabled false

-    4.pseudoLocalesEnabled true //
-    如果没有提供混淆规则文件，则设置默认的混淆规则文件 （SDK/tools/proguard/proguard-android.txt）

-    5.proguardFiles getDefaultProguardFile('proguard-Android.txt'), 'proguard-rules.pro'

-    6.配置签名 signingConfig  signingConfigs.debug

-    7.配置多渠道打包productFlavors

        1）为什么要多渠道打包？

        安卓应用商店（一个商店也叫做一个渠道，如360，baidu，xiaomi）众多，大大小小几百个，我们发布应用之后需要统计各个渠道的用户下载量，所以才有了多渠道打包。
        现在有比较成熟的第三方应用帮我们实现统计功能（比如友盟），统计的本质就是收集用户信息传输到后台，后台生成报表，帮助我们跟踪分析并完善app。通过系统的方法已经可以获取到，
        版本号，版本名称，系统版本，机型，地区等各种信息，唯独应用商店（渠道）的信息我们是没有办法从系统获取到的，所以我们就人为的在apk里面添加渠道信息（其实就用一个字段进行标识，如360，baidu），
        我们只要把这些信息打包到apk文件并将信息传输到后台，后台根据这个标识，可以统计各个渠道的下载量了，并没有多么的高大上。

        说了那么多，其实多渠道打包只需要关注两件事情：


        将渠道信息写入apk文件

        将apk中的渠道信息传输到统计后台


        添加配置,以友盟的方式，传到友盟来统计，他需要UMENG_CHANNEL_VALUE来区分


        android {
            productFlavors {
                xiaomi {
                    manifestPlaceholders = [UMENG_CHANNEL_VALUE: "xiaomi"]
                }
                _360 {
                    manifestPlaceholders = [UMENG_CHANNEL_VALUE: "_360"]
                }
                baidu {
                    manifestPlaceholders = [UMENG_CHANNEL_VALUE: "baidu"]
                }
                wandoujia {
                    manifestPlaceholders = [UMENG_CHANNEL_VALUE: "wandoujia"]
                }
            }
        }

        然后在Android左下角可以通过Build Variants选择构建不同渠道应用

- 8.添加不同buildType，默认我们只有debug 和Realse

- 9.每种渠道都对应这些buildType 在选择buildVarient时候会全部显示出来

- 10.通过不同的打包渠道，我们还可以将框架层抽离，比如我们的开发两个app图标和应用名字不一样，应用也不一样，可以通过这种，来实现多个apk

        oea {
                applicationId "com.janus.oea.advancedgradledemo"
                applicationIdSuffix ".plus"
                //这样最终的包名是 com.janus.oea.advancedgradledemo.plus
                versionCode 1
                versionName "1.0.0"
                manifestPlaceholders = [APP_NAME: "APP_OEA"]
                buildConfigField 'String', 'OEM', '"OEA"'
            }
        oeb {
                applicationId "com.janus.oeb.advancedgradledemo"
                versionCode 2
                versionName "1.2.0"
                manifestPlaceholders = [APP_NAME: "APP_OEB"]
                buildConfigField 'String', 'OEM', '"OEB"'
            }

    依赖一下

    oeaCompile project(':oea')
    oebCompile project(':oeb')

- 11.gradle动态注入属性

通过${name}，使得你可以在你的Manifest插入一个占位符。看下面的例子:

    <activity android:name=".Main">
        <intent-filter>
            <action android:name="${applicationId}.foo">
            </action>
        </intent-filter>
    </activity>

    通过上面的代码，${applicationId}会被替换成真实的applicationId,例如对于branchOne这个variant,它会变成：

    <action android:name="com.example.branchOne.foo">


这是非常有用的，因为我们要根据variant用不同的applicationId填充Manifest.


如果你想创建自己的占位符，你可以在manifestPlaceholders定义，语法是：

    productFlavors {
        branchOne {
            manifestPlaceholders = [branchCustoName :"defaultName"]
        }
        branchTwo {
            manifestPlaceholders = [branchCustoName :"otherName"]
        }
    }



- 12.如果你想做更复杂的事情，你可以applicationVariants.all这个task中添加代码进行执行。


假设，我想设置一个applicationId给branchTwo和distrib结合的variant,我可以在build.gradle里面这样写：

    applicationVariants.all { variant ->
        def mergedFlavor = variant.mergedFlavor
        switch (variant.flavorName) {
            case "brancheTwoDistrib":
                mergedFlavor.setApplicationId("com.example.oldNameBranchTwo")
                break
        }
    }
有时某些buildTypes-flavor结合没有意义，我们想告诉Gradle不要生成这些variants，只需要用variant filter就可以做到

    variantFilter { variant ->
        if (variant.buildType.name.equals('release')) {
            variant.setIgnore(!variant.getFlavors().get(1).name.equals('distrib'));
        }
        if (variant.buildType.name.equals('debug')) {
            variant.setIgnore(variant.getFlavors().get(1).name.equals('distrib'));
        }
    }

在上面的代码中，我们告诉Gradle buildType=debug不要和flavor=distrib结合而buildType=release只和flavor=distrib结合，生成的Variants


