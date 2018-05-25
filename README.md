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
- [Gradle 配置你的AndroidManifest](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/configManifest.gradle)
- Gradle 编译器动态生成java 配置
- [Gradle 指定你的源码路径](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/sourceSet.gradle)
- Gradle 改变项目依赖路径
- [Gradle lintOption](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/lintOption.gradle)
- [lint报告](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/lint-results-obmDebug.html)
- [Gradle 优化项目](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/optimization.gradle)
- [Gradle gradle.properties 配置gradle版本和buildTools版本，和一些不便的版本](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/properties.gradle)
- [Gradle 使用variantFilter修改生成apk](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/renameapk.gradle)
- [Gradle 指定java版本](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/set_java_version.gradle)

- [AndroidStudio常见问题](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/android_studio.gradle)
- Gradle 生成测试报告
- Gradle 生成接口文档
- AAR 生成
- jar 生成
- 元编程

  
  
#### 开发Gradle插件



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




