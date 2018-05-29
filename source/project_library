###项目依赖

- 对于项目依赖android library的话，在这里需要使用gradle mulit project机制。 Mulit project设置是gradle约定的一种格式，如果需要编译某个项目之前，要先编译另外一个项目的时候，就需要用到。结构如下（来自于官方文档）：
        
        MyProject/ 
        | settings.gradle 
         + app/ 
        | build.gradle 
         + libraries/ 
          + lib1/ 
           | build.gradle 
          + lib2/ 
           | build.gradle

- 需要在workplace目录下面创建settings.gradle 的文件，然后在里面写上：

        include ':app', ':libraries:lib1', ':libraries:lib2'