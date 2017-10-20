package com.micky.gradle

import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor
import javassist.CtField
import javassist.CtMethod
import org.gradle.api.Project

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Modifier;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;

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

                    /**
                     *   添加属性
                     */

//                    CtField enoField = new CtField(pool.getCtClass("int"),"eno",ctClass);
//                    enoField.setModifiers(Modifier.PRIVATE);
//                    ctClass.addField(enoField);

//                    /**
//                     *   添加属性
//                     */
//                    CtField enameField = new  CtField(pool.getCtClass("java.lang.String"),"ename",ctClass);
//                    enameField.setModifiers(Modifier.PRIVATE);
//                    ctClass.addField(enameField);
//
//                    /**
//                     * 添加get set方法
//                     */
//                    ctClass.addMethod(CtNewMethod.getter("getEname", enameField));
//                    ctClass.addMethod(CtNewMethod.setter("setEname", enameField));

//                    /**
//                     * 添加自定义方法
//                     */
//                    CtMethod cm = new CtMethod(CtClass.intType, "add", new CtClass[2] { CtClass.intType ; CtClass.intType }, cc);
//                    ctClass.addMethod(cm)


                    println("ctClass = " + ctClass) //解冻
                    if (ctClass.isFrozen()) ctClass.defrost()
                    /**
                     *   添加属性
                     */
                    CtField enoField = new CtField(pool.getCtClass("int"),"eno",ctClass);
                    enoField.setModifiers(Modifier.PRIVATE);
                    ctClass.addField(enoField);


                    /**
                     *   添加属性
                     */
                    CtField enameField = new  CtField(pool.getCtClass("java.lang.String"),"ename",ctClass);
                    enameField.setModifiers(Modifier.PRIVATE);
                    ctClass.addField(enameField);
                    /**
                     * 添加get set方法
                     */
                    ctClass.addMethod(CtNewMethod.getter("getEname", enameField));
                    ctClass.addMethod(CtNewMethod.setter("setEname", enameField));

                    /**
                     * 添加自定义方法1
                     */
                    CtMethod m1 = CtMethod.make("public int getAge(){return 23;}", ctClass);
                    ctClass.addMethod(m1);
                    /**
                     * 添加方法2 代码可以通过编辑器写好，直接粘过来
                     */
                    CtMethod m2 = CtMethod.make("  public String getHelloWorld(String msg){return msg+\"HelloWorld\";}", ctClass);
                    ctClass.addMethod(m2);


                    //获取到OnCreate方法
                    CtMethod ctMethod = ctClass.getDeclaredMethod("onCreate")
                    println("方法名 = " + ctMethod+"4656")
                    String insetBeforeStr = """ android.widget.Toast.makeText(this,"我是被插入的Toast代码~!!",android.widget.Toast.LENGTH_SHORT).show(); """
                    //在方法开头插入代码
                    ctMethod.insertBefore(insetBeforeStr);
                    ctClass.writeFile(path)
                    ctClass.detach()





                    //释放
                }

                if (file.getName().equals("Test.class")){
//                    CtClass ctClass2 = pool.makeClass("Person")
//                    println("***********修改Test")
//
//                    CtClass ctClass = pool.makeClass("com.wangpos.test.Emp");
//
//                    CtConstructor ctConstructor = new CtConstructor(new CtClass[1]{CtClass.booleanType}, ctClass);
//                    ctClass.addConstructor(ctConstructor)
//                    ctClass.writeFile(path)
//                    ctClass.detach()
                }





            }
        }
    }








}