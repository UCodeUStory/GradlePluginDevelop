package com.micky.gradle

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.gradle.api.Project

public class MyInject {

    private static ClassPool pool = ClassPool.getDefault()
    private static String injectStr = "Log.i(\"qiyue\",\"---------inject code --------\");";

    public static void injectDir(String path, String packageName, Project project) {
        pool.appendClassPath(path)
        pool.appendClassPath(path);
        // project.android.bootClasspath 加入android.jar，不然找不到android相关的所有类
        pool.appendClassPath(project.android.bootClasspath[0].toString());
        //引入android.os.Bundle包，因为onCreate方法参数有Bundle
        pool.importPackage("android.os.Bundle");
        File dir = new File(path)
        if (dir.isDirectory()) {
            println("开始遍历所有文件")


            dir.eachFileRecurse { File file ->
                String filePath = file.absolutePath


                if (file.getName().equals("MainActivity.class")) {
                    //获取Test.class
                    CtClass ctClass = pool.getCtClass("com.wangpos.test.MainActivity");
                    println("ctClass = " + ctClass) //解冻
                    if (ctClass.isFrozen()) ctClass.defrost()
                    //获取到OnCreate方法
                    CtMethod ctMethod = ctClass.getDeclaredMethod("onCreate")
                    println("方法名 = " + ctMethod)
                    String insetBeforeStr = """ android.widget.Toast.makeText(this,"我是被插入的Toast代码~!!",android.widget.Toast.LENGTH_SHORT).show(); """
                    //在方法开头插入代码
                    ctMethod.insertBefore(insetBeforeStr);
                    ctClass.writeFile(path)
                    ctClass.detach()
                    //释放
                }


            }
        }
    }








}