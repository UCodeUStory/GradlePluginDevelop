
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

