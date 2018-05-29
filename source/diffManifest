- 构建过程中配置不同的AndroidManifest.xml

比如遇到一个需求安装一个没有图标的应用，而调试的时候我们是需要打开才方便调试的

这就需要在release时去掉：

            <category android:name="android.intent.category.LAUNCHER" />

首先我们可以在gradle.properties 线面添加属性

    isDebug=true
    
在项目的app(有可能这里改了名字)目录build.gradle 配置：


     sourceSets {
            main {
                if (!isDebug.toBoolean()) {//主项目需要在非debug模式下开发
                    //release
                    manifest.srcFile 'src/release/AndroidManifest.xml'
                } else {
                    //debug
                    manifest.srcFile 'src/debug/AndroidManifest.xml'
                }
            }
        }