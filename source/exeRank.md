###Gradle 脚本执行顺序

- gradle的解析顺序：rootproject 的setting.gradle,然后是rootproject的build.gradle,然后是各个subproject。所以project下的build.gradle会先于app下的build.gradle。

- 在build.gradle中，我们可以通过apply plugin: 引入插件，也可以通过 apply from .gradle引入其他gradle脚本中的函数定义或task等


#### 一般hook，我们指的是gradle的生命周期：

- 在解析setting.gradle之后，开始解析build.gradle之前，这里如果要干些事情（更改build.gradle校本内容），可以写在beforeEvaluate
举个例子，我们将我们的一个subproject中的apply plugin改掉，原来是一个library工程，我们希望它被当作application处理：


    project.beforeEvaluate {
                // Change android plugin from `lib' to `application' dynamically
                // FIXME: Any better way without edit file?
    
                if (mBakBuildFile.exists()) {
                    // With `tidyUp', should not reach here
                    throw new Exception("Conflict buildFile, please delete file $mBakBuildFile or " +
                            "${project.buildFile}")
                }
    
                def text = project.buildFile.text.replaceAll(
                        'com\\.android\\.library', 'com.android.application')
                project.buildFile.renameTo(mBakBuildFile)
                project.buildFile.write(text)
            }
            
- 在所有build.gradle解析完成后，开始执行task之前，此时所有的脚本已经解析完成，task，plugins等所有信息可以获取，task的依赖关系也已经生成，如果此时需要做一些事情，可以写在afterEvaluate


    project.afterEvaluate {
                // Set application id
                def manifest = new XmlParser().parse(project.android.sourceSets.main.manifestFile)
                project.android.defaultConfig.applicationId = manifest.@package
            }
        
        
**每个task都可以定义doFirst，doLast，用于定义在此task执行之前或之后执行的代码**

    project.assemble.doLast {
                        println "assemble finish"
                    }
    project.assemble.doFirst {
                        println "assemble start"
                    }
