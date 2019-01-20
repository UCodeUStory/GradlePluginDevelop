
### Gradle版本

Gradle 目录下  - wrapper  - gradle-wrapper.properties

    distributionBase=GRADLE_USER_HOME
    distributionPath=wrapper/dists
    zipStoreBase=GRADLE_USER_HOME
    zipStorePath=wrapper/dists
    distributionUrl=https\://services.gradle.org/distributions/gradle-2.14.1-all.zip

    通常我们 只需要修改distributionUrl就可以了，比如导入其他人的项目通常卡住，其实是因为本地没有对应的gradle版本，正在联网下载，因此我们只需要在导入前改这个文件，查找本地已经有的版本就可以了
    本地版本一般在用户目录.gradle
    
    Wrapper是对Gradle的一层包装，便于在团队开发过程中统一Gradle构建的版本号，这样大家都可以使用统一的Gradle版本进行构建

### Android 项目构建还需要引入Gradle插件，下面是配置插件版本（默认创新新项目插件版本就是当前AndroidStudio版本）

```gradle
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.5.0'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
```

### Plugin for Gradle版本 和 Gradle版本 之间的版本对应关系 


 Gradle Plugin version | Required Gradle version 
---|---
1.0.0 - 1.1.3 | 2.2.1 - 2.3 
1.2.0 - 1.3.1 | 2.2.1 - 2.9
1.5.0	2.2.1 | 2.13
2.0.0 - 2.1.2 | 2.10 - 2.13
2.1.3 - 2.2.3 | 2.14.1+
2.3.0+	  | 3.3+


