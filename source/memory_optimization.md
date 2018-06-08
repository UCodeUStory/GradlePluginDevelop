### 内存泄露


<img width="480" height="240" src="https://github.com/UCodeUStory/GradlePlugin/blob/master/source/gc1.jpeg"/>

<img width="480" height="240" src="https://github.com/UCodeUStory/GradlePlugin/blob/master/source/gc2.jpeg"/>

- 内存泄漏指的是那些程序不再使用的对象无法被GC识别。


    内存泄露是内存溢出OOM的重要原因之一，会导致Crash


- 导致频繁GC，可能是内存抖动，或者瞬间产生大量对象。
  
  
    **GC期间所有线程将暂停，GC所占用的时间和它是哪一个Generation也有关系，执行时间的长短也和当前Generation中的对象数量有关。
    
- 导致GC频繁执行有两个原因：

      
      Memory Churn内存抖动，内存抖动是因为大量的对象被创建又在短时间内马上被释放。
      瞬间产生大量的对象会严重占用Young Generation(新生的)的内存区域，当达到阀值，剩余空间不够的时候，也会触发GC。即使每次分配的对象占用了很少的内存，但是他们叠加在一起会增加Heap的压力，从而触发更多其他类型的GC。这个操作有可能会影响到帧率，并使得用户感知到性能问题。
      
      例如，你需要避免在for循环里面分配对象占用内存，需要尝试把对象的创建移到循环体之外，自定义View中的onDraw方法也需要引起注意，每次屏幕发生绘制以及动画执行过程中，onDraw方法都会被调用到，避免在onDraw方法里面执行复杂的操作，避免创建对象。对于那些无法避免需要创建对象的情况，我们可以考虑对象池模型，通过对象池来解决频繁创建与销毁的问题，但是这里需要注意结束使用之后，需要手动释放对象池中的对象。
      
-  解决内存抖动 


      用对象池技术有很多好处，它可以避免内存抖动，提升性能，但是在使用的时候有一些内容是需要特别注意的。通常情况下，初始化的对象池里面都是空白的，当使用某个对象的时候先去对象池查询是否存在，如果不存在则创建这个对象然后加入对象池，但是我们也可以在程序刚启动的时候就事先为对象池填充一些即将要使用到的数据，这样可以在需要使用到这些对象的时候提供更快的首次加载速度，这种行为就叫做预分配。使用对象池也有不好的一面，程序员需要手动管理这些对象的分配与释放，所以我们需要慎重地使用这项技术，避免发生对象的内存泄漏。为了确保所有的对象能够正确被释放，我们需要保证加入对象池的对象和其他外部对象没有互相引用的关系。

#### 检测工具
 
 - Leaks   
   
        傻瓜式的内存检测工具，但是非常好用
         
 - 当然我们可以用AS Monitor+MAT来自己分析内存泄漏原因
 
 
#### 那么都有哪些资源是GC Roots呢？
   
   1.Class 由System Class Loader/Boot Class Loader加载的类，这些类不会被回收。注意是类不会被回收，实例还是会被回收的，但是不依赖实例的静态static变量是依赖类的，因此很多内存泄露都是因为被静态变量引用导致的。
   
   2. Thread 线程，激活状态的线程；
   
   3. Stack Local 栈中的对象。每个线程都会分配一个栈，栈中的局部变量或者参数都是GC root，因为它们的引用随时可能被用到；
   
   4. JNI Local JNI中的局部变量和参数引用的对象；可能在JNI中定义的，也可能在虚拟机中定义
   
   5. JNI Global JNI中的全局变量引用的对象；同上
   
   6. Monitor Used 用于保证同步的对象，例如wait()，notify()中使用的对象、锁等。
   
   7. Held by JVM JVM持有的对象。JVM为了特殊用途保留的对象，它与JVM的具体实现有关。比如有System Class Loader, 一些Exceptions对象，和一些其它的Class Loader。对于这些类，JVM也没有过多的信息。
   
        也就是说所有的内存泄漏问题从根本上都是因为被这些GC Root引用着导致的
        
#### 常见问题

- 非静态内部类，匿名内部类（由于原因1，handler）

- Thread（由于原因2）

- ContentObserver，File，Cursor，Stream，Bitmap等资源未关闭（由于原因3）

- Webview 内存泄露
 

        
    1.可以将 Webview 的 Activity 新起一个进程，结束的时候直接System.exit(0);退出当前进程；
    启动新进程，主要代码： AndroidManifest.xml 配置文件代码如下
    
        <activity
            android:name=".ui.activity.Html5Activity"
            android:process=":lyl.boon.process.web">
            <intent-filter>
                <action android:name="com.lyl.boon.ui.activity.htmlactivity"/>
                <category android:name="android.intent.category.DEFAULT"/>
            </intent-filter>
        </activity>
    在新进程中启动 Activity ，里面传了 一个 Url：
    
        Intent intent = new Intent("com.lyl.boon.ui.activity.htmlactivity");
        Bundle bundle = new Bundle();
        bundle.putString("url", gankDataEntity.getUrl());
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    然后在 Html5Activity 的onDestory() 最后加上 System.exit(0); 杀死当前进程。
    
    2.不能在xml中定义 Webview ，而是在需要的时候创建，并且Context使用 getApplicationgContext()，如下代码：
    
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mWebView = new WebView(getApplicationContext());
            mWebView.setLayoutParams(params);
            mLayout.addView(mWebView);
    3.在 Activity 销毁的时候，可以先让 WebView 加载null内容，然后移除 WebView，再销毁 WebView，最后置空。
    代码如下：
    
        @Override
        protected void onDestroy() {
            if (mWebView != null) {
                mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                mWebView.clearHistory();
    
                ((ViewGroup) mWebView.getParent()).removeView(mWebView);
                mWebView.destroy();
                mWebView = null;
            }
            super.onDestroy();
        }



- BraodcastReceiver，EventBus等观察者注册未注销（由于原因1）

- 单例Dialog 一直持有Context（这种依赖Context 不要使用单例）

#### 解决原则

- 内部类静态化，内部类里面的资源及时关闭不要静态化

- 注意线程的及时关闭

- 注意资源的及时关闭

- webView单独开线程（下面有具体的例子）

- 同样需要及时关闭