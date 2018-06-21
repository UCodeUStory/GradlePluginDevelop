## Gradle plugin development introduction


####  Gradle basic：

    This time must be systematically mastered. Are you ready?
    
    
- [Acquaintance Gradle and Domain Specific Languages](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/day01.gradle)
- [Gradle version configuration](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/day02.md)
- [Gradle module configuration](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/day03.gradle)
- [Gradle plugin classification](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/day04.gradle)
- [Gradle Android plugin content](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/android.gradle)
- [CompileSdkVersion minSdkVersion targetSdkVersion buildToolsVersion区别](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/sdkVersionType.md)
- [Gradle integrate configuration version](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/version.gradle)
- [Gradle branch channel packaging](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/productflavor.gradle)
- [Gradle configuration AndroidManifest](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/configManifest.gradle)
- [Gradle Configure your source path, dynamically remove classes that do not require packaging](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/sourceSet.gradle)
- [Gradle Project Dependency Configuration](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/project_library.md)
- [Gradle lintOption·优](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/lintOption.gradle)
- [lint report](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/lint-results-obmDebug.html)
- [Gradle package optimization configuration](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/optimization.gradle)
- [Gradle gradle.properties Configure gradle version and buildTools version, and some constant version number](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/properties.gradle)
- [Gradle use variantFilter modify the generated apk path, name](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/applicationVariant.gradle)
- [Gradle configure java version](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/set_java_version.gradle)
- [Gradle packagingOptions Solve duplicate packages and files](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/packageOption.gradle)
- [AndroidStudio common problem](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/android_studio.xml)
- [Gradle command package apk](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/assemble.md)
- [Gradle command line passing parameters](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/assembleWithParams.md)
- [Gradle Compiler dynamic generation java · excellent·优](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/operate_file.md)
- [Gradle create Task](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/task.md)
- [Gradle select different AndroidManifest.xml](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/diffManifest.md)
- [Gradle Order of execution](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/exeRank.md)
- Gradle Generate test report
- [Gradle Generate interface document](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/genJavadoc.gradle)
- [AAR generate](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/aar.md)
- [jar generate](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/makeJar.md)
- [Metaprogramming](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/metaprogramming.md)
- See all tasks commands    *./gradlew tasks --all*

  
#### Gradle Advanced plug-in development
 - [Plugin development steps](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/plugin_develop.md)
 - [Gradle Transform Listening files compile end](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/gradle_tranform.md)

#### Android Performance optimization
- [apk Slimming optimization](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/android_apk_optimization.md) 
- [Interface performance UI](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/ui_optimization.md) 
- [Memory leak](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/memory_optimization.md)
- [WorkManager](https://github.com/UCodeUStory/GradlePlugin/blob/master/source/workmanager.md)


### Conclusion of issue

  - 1. Cannot find dependent libraries, need to add jcenter() in repositories
  - 2. javassist can not find the jar package, it is necessary to javassist import jar package
  - 3. Found that the generated apk did not change, delete the build directory to rebuild, still no change, click Android Studio setting to clear the cache, restart
  - 4. The project app changes the name of the error message is not found when the project, the general root directory.idea can be resolved
  - 5. Resolve Error: All flavors must now belong to a named flavor dimension.
 
        flavorDimensions "versionCode"
 - 6.Android Studio clean cause Error:Execution failed for task ':app:mockableAndroidJar' > java.lang.NullPointer
 
          Solution 1. This problem is caused by changing the version of the main project complieSdk. It is only necessary to change the version of all subprojects to be the same;
    
          Solution 2. You can also pass
    
                - 1. Go to File -> Settings -> Build, Execution, Deployment -> Compiler
    
                - 2. Add to "Command-line Options": -x :app:mockableAndroidJar
    
                - 3. Press "OK" and try to Rebuild Project again.
               
    
          Solution 3. File -> Settings -> Build, Execution, Deployment -> Build Tools -> Gradle -> Experimental
                Cancel Enable All test.. Checked, but mac version did not find this option
               
          Solution 4. Add in the root directory
           
               gradle.taskGraph.whenReady {
                       tasks.each { task ->
                           if (task.name.equals('mockableAndroidJar')) {
                               task.enabled = false
                           }
                       }
               }
- 7. When we modify the compile 'com.android.support:appcompat-v7:25.0.0' version, it will report a lot of value
  The topic could not be found error
      At this point we only need to modify the compile SDK version and this version of V7 to be the same


#### friendship link
[fly803/BaseProject](https://github.com/fly803/BaseProject) 
