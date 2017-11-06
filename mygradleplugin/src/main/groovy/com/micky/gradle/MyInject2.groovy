package com.micky.gradle

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.gradle.api.Project

import java.lang.annotation.Annotation

public class MyInject2 {

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
                if (file.getName().equals("Presenter.class")) {
                    //获取Test.class

                    CtClass ctClass = pool.getCtClass("com.wangpos.test.Presenter");

                    /**
                     *   修改方法 在 OnCreate方法前后插入代码
                     */

                    CtMethod ctOnResumeMethod = ctClass.getDeclaredMethod("onResume")
                    println("方法名 = " + ctOnResumeMethod+"4656")


                    String insetBeforeStr = """ android.widget.Toast.makeText(,"我是被插入的Toast代码~!!",android.widget.Toast.LENGTH_SHORT).show(); """
                    //在方法开头插入代码

                    /**
                     * 遍历方法
                     */
                    for (CtMethod ctmethod : ctClass.getDeclaredMethods()) {
                        String methodName = Utils.getSimpleName(ctmethod);
                        println("methodName >>>>>>>>= " + methodName+"")

                        for (Annotation mAnnotation : ctmethod.getAnnotations()) {
                            println("annotation= >>>>>>>>= " + mAnnotation.annotationType().canonicalName)
                            if (mAnnotation.annotationType().canonicalName.equals("com.wangpos.test.OnResume")){
                                println("找到方法 methodName >>>>>>>>= " + methodName+"")
                                ctOnResumeMethod.insertBefore(methodName+"();");
                            }


                        }
                    }


                    String insetBehand = "android.util.Log.i(\"qiyue\",\"\"+System.currentTimeMillis());";
                    ctOnResumeMethod.insertAfter(insetBehand);
                    ctClass.writeFile(path)
                    ctClass.detach()





                    //释放
                }






            }
        }
    }








}