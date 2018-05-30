###Gradle 操作文件

#### 应用场景
     当我们在开发一个SDK的时候，针对不同的用户会提供不同的接口，在不修改代码的情况下，就是通过
     gradle配置过滤一些class文件，通常我的接口都会在统一初始化入口初始化配置，这个初始化配置，如果
     我们只是单独去掉我们的接口class文件，显然初始化配置哪里会报错，NotFound，所以在初始化的地方我们也需要动态配置
     这就需要这个初始化配置我们单独写一个类，而这个类我们用Gradle来根据需求生成
    （当我们学习一个技术的时候如果应用不那就没有必要去学）
    


- 生成我们自己的配置，首先需要创建一个默认BuildConfigDefault类，具有默认配置

     
    
    def replaceFirstIfTaged(str,tag,src,dest) {
        if (str.contains(tag)) {
            println "** ${tag} = ${dest};"
            return str.replaceFirst(src, "${dest}")
        }
        return str
    }
    
    def replaceInt(key,src,value) {
        def configFile = "${projectDir}/src/main/java/config/BuildConfigDefault.java"
        def encoding = "UTF-8"
        def lines = []
        new File(configFile).newReader(encoding).eachLine { line ->
            lines.add( replaceFirstIfTaged(line,key,src,value) )
        }
    
        BufferedWriter writer = new File(configFile).newWriter(encoding)
        lines.each{ line ->  writer.writeLine(line) }
        writer.close()
    }
    
    
    
    /**
     * 替换整数
     * @param key
     * @param value
     * @return
     */
    def initConfigInt(key,value) {
        replaceInt(key,/\d+/,value)
    }
    
    
    //替换代码中字符串变量的值
    def replaceStringMemberValue(str,tag,dest) {
        if (str.contains(tag)) {
            println "** ${tag} = ${dest};"
            str=str.substring(0,str.indexOf("\"")+1);
            str=str+dest+"\";";
            return str
        }
        return str
    }
    
    def initConfigString(key,value) {
        println("'projectDir="+projectDir)///Users/qiyue/GitProject/GradlePluginWS/GradlePlugin/app
    
        def configFile = "${projectDir}/src/main/java/config/BuildConfigDefault.java"
        def encoding = "UTF-8"
        def lines = []
        new File(configFile).newReader(encoding).eachLine { line ->
            lines.add(replaceStringMemberValue(line,key,value))
        }
    
        BufferedWriter writer = new File(configFile).newWriter(encoding)
        lines.each{ line ->  writer.writeLine(line) }
        writer.close()
    }

initConfigString("name","configName")
initConfigInt("isOpen",4)


- 修改我们的serviceLoader
    
    ext {
        baseDevConfigs = [
                'InstallManager'
                ,
                'InstallManager'
                ,
                'InstallManager'
                ,
                'InstallManager'
                ,
                'InstallManager'
    
    
        ]
    }
    
    /**
     * 生成新的ServiceLoader.java文件内容
     * @return
     */
    def initServiceLoaderFile() {
        def configFile = "${projectDir}/src/main/java/config/ServiceLoader.java"
        File file = new File(configFile);
        def oldlines = file.readLines();
        def newlines = []
        int startIndex = -1;
        int i = 0;
        def expConfig = "configs.put(";
    
        def packageIndex = 0;
        for (line in oldlines) {
            if (!line.contains("configs.put")) {
                newlines.add(line)
            }
            if (line.contains("package")) {
                packageIndex = i;
            }
            if (line.contains("public ServiceLoader(){")){
                startIndex = i;
            }
    
            i++;
    
        }
        if (!newlines.contains("import com.wangpos.test.inter.*;")) {
            newlines.add(packageIndex + 1, "import com.wangpos.test.inter.*;");
        }
    
        //拼接默认配置内容
        for (line in baseDevConfigs) {
    
            def instance = "		configs.put(${line}.class,${line}Impl.class);";
            newlines.add(startIndex+1, instance);
    
        }
    
        PrintWriter writer = file.newPrintWriter("UTF-8")
        newlines.each { line -> writer.println(line) }
        writer.close()
    }
    
    initServiceLoaderFile()
    
    
### 文件操作基础

- 文件对象
在工程目录下，我们可以通过File的构造方法来快速定位一个文件并创建相应的File对象：
    
        // 传入文件的相对路径
        File configFile = new File('src/config.xml')
        
        // 传入文件的绝对路径
        configFile = new File(configFile.absolutePath)
        
        // 通过相对路径构建一个 java.nio.file.Path 对象并传入
        configFile = new File(Paths.get('src', 'config.xml'))
        
        // 读取property变量构建 java.nio.file.Path 对象并传入
        configFile = new File(Paths.get(System.getProperty('user.home')).resolve('global-config.xml'))
    
    - 文件集合FileCollection

这个接口描述了针对一组文件的操作和属性。在Gradle中，许多类都继承了这一接口，例如依赖配置对象dependency configurations .
与创建File对象不同，创建FileCollection对象的唯一方式是通过 Project.files(java.lang.Object[])
方法，该方法的入参数目是任意多个，类型也可以是表示相对路径的字符串，File对象，甚至是集合，数组等。
 
    
    FileCollection collection = files('src/file1.txt',
                                      new File('src/file2.txt'),
                                      ['src/file3.txt', 'src/file4.txt'],
                                      Paths.get('src', 'file5.txt'))


    // 遍历文件集合
    collection.each { File file ->
        println file.name
    }
    
    // 将FileCollection对象转换为其他类型
    Set set = collection.files
    Set set2 = collection as Set
    List list = collection as List
    String path = collection.asPath
    File file = collection.singleFile
    File file2 = collection as File
    
    // 对FileCollection进行加减操作
    def union = collection + files('src/file3.txt')
    def different = collection - files('src/file3.txt')
