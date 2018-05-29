//makeJar  打包Jar 可以通过from指定要打包的目录文件就是.class文件


task makeSdkJar(type:org.gradle.api.tasks.bundling.Jar){

    baseName 'pluginsdk'

    //只打包org.cmdmac下的org.cmdmac.pluginsdk.impl和org.cmdmac.gamecenter,其他子包不会被打包进去

    from('build/intermediates/classes/debug/org/cmdmac/'){

        include'pluginsdk/impl'

        include'gamecenter'

    }

    into('org/cmdmac/')

    //    exclude('R.class')   可以过滤不需要打包的文件
    
    //    exclude{ it.name.startsWith('R$');}

task makeSdkJar(type:org.gradle.api.tasks.bundling.Jar){

        baseName 'pluginsdk'

      //只打包org.cmdmac下的org.cmdmac.pluginsdk.impl和org.cmdmac.gamecenter,其他子包不会被打包进去

        from('build/intermediates/classes/debug/org/cmdmac/'){

            include'pluginsdk/impl'

            include'gamecenter'

        }

        into('org/cmdmac/')
    
    //    exclude('R.class')
    
    //    exclude{ it.name.startsWith('R$');}

}
