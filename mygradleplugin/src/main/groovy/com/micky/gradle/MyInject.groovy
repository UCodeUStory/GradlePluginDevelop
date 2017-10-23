package com.micky.gradle

import com.android.ddmlib.Log
import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor
import javassist.CtField
import javassist.CtMethod
import org.gradle.api.Project

import java.io.File;
import java.io.FileOutputStream
import java.lang.reflect.Method;
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
        /**
         * 添加依赖的jar
         */
        pool.appendClassPath(project.android.bootClasspath[0].toString());
//        pool.appendClassPath(Dir)
//        pool.appendClassPath("/Users/qiyue/Library/Android/sdk/extras/android/m2repository/com/android/support/appcompat-v7/25.0.0/appcompat-v7-25.0.0-javadoc.jar")
        //引入android.os.Bundle包，因为onCreate方法参数有Bundle
        pool.importPackage("android.os.Bundle");
//        pool.importPackage("android.support.v7.app.AppCompatActivity");
        File dir = new File(path)
        if (dir.isDirectory()) {
            println("开始遍历所有文件")


            dir.eachFileRecurse { File file ->
                String filePath = file.absolutePath

                /**
                 * 通过文件名 和 路径 与过滤文件
                 */
                if (file.getName().equals("MainActivity.class")) {
                    //获取Test.class

                    CtClass ctClass = pool.getCtClass("com.wangpos.test.MainActivity");
//                    /**
//                     * 获取父类名字
//                     */
//                    CtClass supterCtClass = ctClass.getSuperclass();
//
//                    println("supterCtClass = " + supterCtClass)

//                    Method[] ctMethodArray = ctClass.toClass().getDeclaredMethods();
////
//////                    CtMethod []ctMethodArray = ctClass.declaredMethods();
////
//                    for (int i = 0; i < ctMethodArray.length ; i++) {
//                         println("method "+i + ctMethodArray[0].name);
//                    }


                    println("ctClass = " + ctClass) //解冻
                    if (ctClass.isFrozen()) ctClass.defrost()


                    /**
                     *   添加静态常量
                     */
                    CtField finalStaticField = new CtField(pool.getCtClass("int"),"F_DEBUG",ctClass);

                    finalStaticField.setModifiers(Modifier.FINAL|Modifier.STATIC|Modifier.PUBLIC);

                    ctClass.addField(finalStaticField);
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

                    /**
                     *   修改方法 在 OnCreate方法前后插入代码
                     */

                    CtMethod ctMethod = ctClass.getDeclaredMethod("onCreate")
                    println("方法名 = " + ctMethod+"4656")
                    String insetBeforeStr = """ android.widget.Toast.makeText(this,"我是被插入的Toast代码~!!",android.widget.Toast.LENGTH_SHORT).show(); """
                    //在方法开头插入代码
                    ctMethod.insertBefore(insetBeforeStr);

                    String insetBehand = "android.util.Log.i(\"qiyue\",\"\"+System.currentTimeMillis());";
                    ctMethod.insertAfter(insetBehand);
                    ctClass.writeFile(path)
                    ctClass.detach()





                    //释放
                }

                if (file.getName().equals("Test.class")){


                    CtClass ctClass = pool.getCtClass("com.wangpos.test.Test");

                    /**
                     *   添加属性
                     */
                    CtField enoField = new CtField(pool.getCtClass("int"),"t_eno",ctClass);
                    enoField.setModifiers(Modifier.PRIVATE);
                    ctClass.addField(enoField);

                    CtField nameField = new CtField(pool.getCtClass("java.lang.String"),"name",ctClass);
                    nameField.setModifiers(Modifier.PRIVATE);
                    ctClass.addField(nameField);

                    /**
                     * 添加有参数构造方法
                     */
                    CtClass []ctArray = new CtClass[2];
                    ctArray[0] = pool.getCtClass("java.lang.String")
                    ctArray[1] = pool.getCtClass("int")
                    CtConstructor constructor = new CtConstructor(ctArray, ctClass);
                    constructor.setModifiers(Modifier.PUBLIC);
                    /**
                     * 写代码要注意，容易丢分号之类的，报错source is missing
                     */
                    constructor.setBody("{this.name=\$1;this.t_eno=\$2;}");
                    ctClass.addConstructor(constructor);

                    /**
                     * 添加方法
                     */
                    CtMethod m1 = CtMethod.make("public void init(){}", ctClass);
                    ctClass.addMethod(m1);

                    CtMethod m2 = CtMethod.make("public int calculate(int a , int b){ return a + b;}", ctClass);
                    ctClass.addMethod(m2);



                    ctClass.writeFile(path)
                    ctClass.detach()


                }





            }
        }
    }








}