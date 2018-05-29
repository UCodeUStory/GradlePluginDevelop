### gradle命令 

一般是./gradlew +参数， gradlew代表 gradle wrapper，意思是gradle的一层包装，大家可以理解为在这个项目本地就封装了gradle，即gradle wrapper， 在gradle/wrapper/gralde-wrapper.properties文件中声明了它指向的目录和版本。只要下载成功即可用grdlew wrapper的命令代替全局的gradle命令。

./gradlew -v 版本号
./gradlew clean 清除app目录下的build文件夹
./gradlew build 检查依赖并编译打包
./gradlew tasks 列出所有task
这里注意的是 ./gradlew build 命令把debug、release环境的包都打出来，如果正式发布只需要打Release的包，该怎么办呢，下面介绍一个很有用的命令 assemble， 如：

### 过滤构建类型打包
./gradlew assembleDebug 编译并打Debug包
./gradlew assembleRelease 编译并打Release的包

### 过滤项目打包

除此之外，assemble还可以和productFlavors结合使用：

./gradlew installRelease Release模式打包并安装
./gradlew uninstallRelease 卸载Release模式包


assemble结合Build Variants来创建task
assemble 还能和 Product Flavor 结合创建新的任务，其实 assemble 是和 Build Variants 一起结合使用的，而 Build Variants = Build Type + Product Flavor，举个例子大家就明白了： 
如果我们想打包 wandoujia 渠道的release版本，执行如下命令就好了：

./gradlew assembleWandoujiaRelease
如果我们只打wandoujia渠道版本，则：

./gradlew assembleWandoujia
此命令会生成wandoujia渠道的Release和Debug版本 
同理我想打全部Release版本：

./gradlew assembleRelease
这条命令会把Product Flavor下的所有渠道的Release版本都打出来。 
总之，assemble 命令创建task有如下用法：

assemble<Variant Name>： 允许直接构建一个Variant版本，例如assembleFlavor1Debug。
assemble<Build Type Name>： 允许构建指定Build Type的所有APK，例如assembleDebug将会构建Flavor1Debug和Flavor2Debug两个Variant版本。
assemble<Product Flavor Name>： 允许构建指定flavor的所有APK，例如assembleFlavor1将会构建Flavor1Debug和Flavor1Release两个Variant版本。