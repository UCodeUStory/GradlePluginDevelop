#### 终端打包传入参数

在很多打包场景为了避免修改代码，通过命令行方式传递参数是很方便

- 参数名字前加P，每个参数用空格隔开

       
        ./gradlew assembleRelease -PTestName='hello' -Pcustom=2

- 在代码中如何使用呢,看下面：


    def custom = project.hasProperty('custom')?custom:'defaultCustom'
    def testName = project.hasProperty('TestName')?TestName:'defaultName'
    
    //println(custom)
    //println(testName)
