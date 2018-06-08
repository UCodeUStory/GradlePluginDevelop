- compileSdkVersion 26 
 

    告诉 Gradle 用哪个 Android SDK 版本编译你的应用,当你修改了 compileSdkVersion 的时候，
    可能会出现新的编译警告、编译错误，但新的 compileSdkVersion 不会被包含到 APK 中：它纯粹只是在编译的时候使用。
    compileSdkVersion 通常我们使用最新的，在编译的时候检查代码的错误和警告，提示开发者修改和优化


- buildToolsVersion "26.0.1" 


    表示构建工具的版本号，这个属性值对应 AndroidSDK 中的 Android SDK Build-tools，
    正常情况下 build.gradle 中的 buildToolsVersion 跟你电脑中 Android SDK Build-tools 的最新版本是一致的
 

- minSdkVersion 15  


    应用可以运行的最低要求,app运行所需的最低sdk版本.低于minSdkVersion的手机将无法安装.
 

- targetSdkVersion 26


    minSdkVersion和targetSdkVersion相信非常多人都不太理解。我在网上也看了很多关于这两者差别的文章，感觉说的都非常模糊。直到我在stackOverFlow看到Android Min SDK Version vs. Target SDK Version这篇文章后，我才最终弄清楚怎样去设置minSdkVersion和targetSdkVersion。如今我将分享给大家。
    简言之，这篇文章的目的是为了区分minSDK和targetSDK，这两者相当于一个区间。你能够用到targetSDK中最新的API和最酷的新功能，但你又不得不向下兼容到minSDK，保证这个区间内的设备都能够正常的执行你的app。换句话说，你想使用Android刚刚推出的新特性。但这对于你的app又不是必须的。你就能够将targetSDK设置为你想使用新特性的SDK版本号，minSDK设置成低版本号保证全部人都能够使用你的app。
 