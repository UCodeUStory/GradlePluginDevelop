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