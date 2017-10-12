## Gradle插件开发介绍

1.使用Android Studio开发Gradle插件


- 项目创建
  - 创建一个Android工程
  - 创建一个Android Library类型的Moudle
  - 将此Module的java修改改成groovy
  - 包名自己可以修改
  - 此Moudle下build.gradle内容清空，添加如下代码
  
  
    apply plugin: 'groovy'

    apply plugin: 'maven'
    
    dependencies {

        compile gradleApi()
        compile localGroovy()
    }
    
    repositories {
    
        mavenCentral()
    }
    
    //group和version在后面使用自定义插件的时候会用到 可以随便起
    group='com.micky'
    
    version='1.0.0'
    //上传本地仓库的task，
    uploadArchives {
    
        repositories {
            mavenDeployer {
            //本地仓库的地址，自己随意选，但使用的时候要保持一致，这里就是当前项目目录
                repository(url: uri('../repo'))
            }
        }
    }
-
    在groovy路径下创建一个MyPlugin.groovy,新建文件一定要带后缀名
    
    
    package  com.hc.plugin

    import org.gradle.api.Plugin
    import org.gradle.api.Project
    
    public class MyPlugin implements Plugin<Project> {
    
        void apply(Project project) {
            System.out.println("========================");
            System.out.println("hello gradle plugin!");
            System.out.println("========================");
        }
    }
- (6) 现在，我们已经定义好了自己的gradle插件类，接下来就是告诉gradle，哪一个是我们自定义的插件类，因此，需要在main目录下新建resources目录，然后在resources目录里面再新建META-INF目录，再在META-INF里面新建gradle-plugins目录。最后在gradle-plugins目录里面新建properties文件，注意这个文件的命名，你可以随意取名，但是后面使用这个插件的时候，会用到这个名字。比如，你取名为com.hc.gradle.properties，而在其他build.gradle文件中使用自定义的插件时候则需写成：

   apply plugin: 'com.hc.gradle'

- 然后在com.hc.gradle.properties文件里面指明你自定义的类

  implementation-class=com.hc.plugin.MyPlugin
  
- 执行 gradle uploadArchives 上传到本地仓库会生成jar
- 然后在项目的app目录下的build.gradle 使用插件
- 
buildscript {

    repositories {
        maven {//本地Maven仓库地址
            url uri('D:/repos')
        }
    }
    dependencies {
        //格式为-->group:module:version
        classpath 'com.hc.plugin:myplugin:1.0.0'
    }
}

//com.hc.gradle为resources/META-INF/gradle-plugins
//下的properties文件名称

apply plugin: 'com.hc.gradle'

- 最后 先clean project(很重要！),然后再make project.从messages窗口打印如下信息
- 



# 待添加 Gradle介绍
