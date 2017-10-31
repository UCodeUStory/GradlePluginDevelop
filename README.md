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

## 问题总结
  1.找不到依赖库，需要在repositories种添加jcenter() 
  2.还有javassist找不到jar包，就是需要javassist引入jar包
  3.发现生成的apk没有变化，删除了build目录重新build，仍然无变化，点击Android Studio setting 清理缓存，重新启动

## Gradle详细介绍
   1.
   2.
