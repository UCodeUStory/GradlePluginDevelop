### 针对自定义View优化：

1. 针对自定义View，我们可能犯下面三个错误：
    
    
    Useless calls to onDraw()：我们知道调用View.invalidate()会触发View的重绘，有两个原则需要遵守，第1个是仅仅在View的内容发生改变的时候才去触发invalidate方法，第2个是尽量使用ClipRect等方法来提高绘制的性能。
    Useless pixels：减少绘制时不必要的绘制元素，对于那些不可见的元素，我们需要尽量避免重绘。
    Wasted CPU cycles：对于不在屏幕上的元素，可以使用Canvas.quickReject把他们给剔除，避免浪费CPU资源。另外尽量使用GPU来进行UI的渲染，这样能够极大的提高程序的整体表现性能。


2. 执行延迟任务，通常有下面三种方式：
    
    
    AlarmManager：使用AlarmManager设置定时任务，可以选择精确的间隔时间，也可以选择非精确时间作为参数。除非程序有很强烈的需要使用精确的定时唤醒，否者一定要避免使用他，我们应该尽量使用非精确的方式。
    SyncAdapter：我们可以使用SyncAdapter为应用添加设置账户，这样在手机设置的账户列表里面可以找到我们的应用。这种方式功能更多，但是实现起来比较复杂（Google官方培训课程）
    JobSchedulor：这是最简单高效的方法，我们可以设置任务延迟的间隔，执行条件，还可以增加重试机制。
    8.0WorkManager:

3. Android的Heap空间是不会自动做兼容压缩的，意思就是如果Heap空间中的图片被收回之后，这块区域并不会和其他已经回收过的区域做重新排序合并处理，那么当一个更大的图片需要放到heap之前，很可能找不到那么大的连续空闲区域，那么就会触发GC，使得heap腾出一块足以放下这张图片的空闲区域，如果无法腾出，就会发生OOM。

尽量减少PNG图片的大小是Android里面很重要的一条规范。相比起JPEG，PNG能够提供更加清晰无损的图片，但是PNG格式的图片会更大，占用更多的磁盘空间。到底是使用PNG还是JPEG，需要设计师仔细衡量，对于那些使用JPEG就可以达到视觉效果的，可以考虑采用JPEG即可。
Webp，它是由Google推出的一种既保留png格式的优点，又能够减少图片大小的一种新型图片格式。


4. 使用inBitmap属性可以告知Bitmap解码器去尝试使用已经存在的内存区域，新解码的bitmap会尝试去使用之前那张bitmap在heap中所占据的pixel data内存区域，而不是去问内存重新申请一块区域来存放bitmap。利用这种特性，即使是上千张的图片，也只会仅仅只需要占用屏幕所能够显示的图片数量的内存大小。

在SDK 11 -> 18之间，重用的bitmap大小必须是一致的，例如给inBitmap赋值的图片大小为100-100，那么新申请的bitmap必须也为100-100才能够被重用。从SDK 19开始，新申请的bitmap大小必须小于或者等于已经赋值过的bitmap大小。
新申请的bitmap与旧的bitmap必须有相同的解码格式，例如大家都是8888的，如果前面的bitmap是8888，那么就不能支持4444与565格式的bitmap了



- 优化建议

  1、RelativeLayout会让子View调用2次onMeasure，在不影响层级深度的情况下,使用LinearLayout和FrameLayout而不是RelativeLayout。
  
  2、如果在View树层级的末端，应尽量用一个RelativeLayout来代替两层LinearLayout或FrameLayout。降低View树的层级才是王道。
  
  3、LinearLayout 在有weight时，可能会调用子View2次onMeasure，降低测量的速度，在使用LinearLayout 应尽量避免使用layout_weight。
  
  LinearLayout 在有weight属性时，为什么是可能会导致 2次measure ?
  
  分析源码发现，并不是所有的layout_weight都会导致两次measure：
  
  Vertical模式下，child设置了weight（height＝0，weight > 0）时将会跳过这一次Measure，之后会再一次Measure
  
  //Vertical模式下，child设置（height＝0，weight > 0）时将会跳过这一次Measure，之后会再一次Measure
  if (heightMode == MeasureSpec.EXACTLY && lp.height == 0 && lp.weight > 0) {
     // Optimization: don't bother measuring children who are going to use
     // leftover space. These views will get measured again down below if
     // there is any leftover space.
     final int totalLength = mTotalLength;
     mTotalLength = Math.max(totalLength, totalLength + lp.topMargin + lp.bottomMargin);
     skippedMeasure = true;//跳过这一次measure
  } 