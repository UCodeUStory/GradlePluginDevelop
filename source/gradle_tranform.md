### 新增Gradle Transform

监听文件编译结束，通过javasist实现字节码修改,实现代码插入,通过这种插件化的AOP 和代码中使用Aspectj 区别就是，避免代码碎片化，添加一个功能修改多处代码，即使用了Aspectj 也许要在修改的地方添加注解，当修改处很多的时候很不方便，通过transform和javassist 可以遍历整个工程，按照满足条件的一次性修改，并且以后我们可以写成通用性的组建，比如自动注册一些组建在所有Activity，里面Javassist用了反射原理，但是这是编译器，不损失效率,Javassist非常强大，需要仔细学习

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
       }