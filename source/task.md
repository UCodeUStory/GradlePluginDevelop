task initTask{

    println('***初始化模块***')

}
/**在终端手动调用的时候才会执行 ./gradlew releaseJar*/
task releaseJar <<{
    println('***创建jar包***')
}



//如果代码没有加<<，则这个任务在脚本initialization的时候执行 ./gradlew releaseJar*/

//
//这和我们调用task这个函数的方式有关！如果没有<<，则闭包在task函数返回前会执行，而如果加了<<，则变成调用myTask.doLast添加一个Action了，自然它会等到grdle myTask的时候才会执行！
