package com.micky.gradle;

import org.gradle.api.*;

/**
 * 每次修改插件都需要执行gradle uploadArchives 命令 上传到本地仓库
 *
 * build 过程 apply代码会执行，这段代码里面又定义了一个任务，任务可以通过 终端命令 gradle 任务名执行，例如 gradle myTask
 *
 */
class MyCustomPlugin implements Plugin<Project> {
    void apply(Project project) {
        System.out.println("这是自定义插件");
        project.task('myTask') << {
            println "Hi this is micky's plugin"
        }
    }
}