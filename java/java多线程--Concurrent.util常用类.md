## 1.CyclicBarrier的使用 
**场景还原：** 每个线程线程代表一个跑步运动员，当运动员都准备好后，才一起除非，只要有一个人没有准备好，大家都等待。这个种情况如何使代码实现呢？
>这种情况就需要用到CyclicBarrier了，它可以把程序阻塞在一个地方进行等待，指定需要执行的任务达到CyclicBarrier设置的值，此时CyclicBarrier后面的代码才会被执行。

示列如下：
```
package concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MyThread implements Runnable{

	int threadId ;
	private CyclicBarrier barrier; 
	
	public MyThread(int threadId, CyclicBarrier barrier) {
		this.threadId = threadId;
		this.barrier = barrier;
	}
	
	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}

	@Override
	public void run() {
		try {	
			Thread.sleep(1000);
			System.out.println("线程ID："+getThreadId()+"准备就绪");
			barrier.await();			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
		System.out.println("线程ID："+getThreadId()+"执行");
	}

}
```
测试：
```
package concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test {

	public static void main(String[] args) {
		CyclicBarrier barrier = new CyclicBarrier(3);
		ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 3, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),new MyRejected());
		MyThread t1 = new MyThread(1,barrier);
		MyThread t2 = new MyThread(2,barrier);
		MyThread t3 = new MyThread(3,barrier);
		MyThread t4 = new MyThread(4,barrier);
		MyThread t5 = new MyThread(5,barrier);
		MyThread t6 = new MyThread(6,barrier);
		
		pool.execute(t1);
		pool.execute(t2);
		pool.execute(t3);
		pool.execute(t4);
		pool.execute(t5);
		pool.execute(t6);
		
		pool.shutdown();
	}

}
```
输出结果：
```
线程ID：1准备就绪
线程ID：5准备就绪
线程ID：6准备就绪
线程ID：6执行
线程ID：1执行
线程ID：5执行
线程ID：4准备就绪
线程ID：3准备就绪
线程ID：2准备就绪
线程ID：2执行
线程ID：4执行
线程ID：3执行
```
## 2.CountDownLacth的使用 
>他经常用于监听某些初始化操作，等初始化执行完毕后，通知主线程继续工。
区别：CyclicBarrier所有线程都阻塞；CountDownLatch只有一个(主)线程阻塞等待
## 3.Callable和Future的使用
>Future是一个设计模式，可以实现异步获取数据的。Future模式是非常合适在处理耗时很长的业务逻辑时进行使用，可以有效的减少系统的响应时间，提高系统的吞吐量。
## 4.Semaphore（计算信号量）的使用
>官网API解释：计数信号量。从概念上讲，一个信号量维护一组允许。每个 acquire()块如果必要的许可证前，是可用的，然后把它。每个 release()添加许可，潜在收购方释放阻塞。然而，不使用实际允许的对象； Semaphore只是计数的数量和相应的行为。 
信号量通常是用来限制线程的数量比可以访问一些（物理或逻辑）资源。

Semaphore非常适合高并发访问，新系统在上线之前，要对系统的访问进行评估，当然这值肯定不是随便拍拍脑袋就能想出来的，是经过以往的经验、数据历年的访问量，已经推广力度进行一个合理的评估，当然评估标准不能太大也不能太小，太大的话投入的资源达不到实际效果，纯属浪费资源，太小的话，某时间点一个高峰值的访问量上来直接可以压垮系统。

相关概念： 
PV（Page View） 网站的总访问量，页面浏览量或者点击量，用户没刷新一次就会被记录一次。

UV（Unique Visitor） 访问网站的一台电脑客户端为一个访客。一般来讲，时间上以00:00-24:00之内相同ip的客户只记录一次。

QPS （Query Per Second）即每秒查询数，qps很大程度上代表了业务系统上的繁忙程度，每次请求的背后，可能对应着多次磁盘I/O，多次网络请求，多个CPU时间片等。我们通过QPS可以非常直观的了解当前系统业务的情况，一旦当前QPS超过设定的预警值，可以考虑增加机器对集群扩容，以免压力过大造成宕机，可以根据前期的压力测试得以评估，再结合后期综合运维情况，估算出的阈值。

RT （Response Time）即请求的响应时间，这个指标非常关键，直接说明前端用户的体验，因此任何系统设计师都想降低RT时间。

当然还涉及cpu，内存，网络，磁盘等情况，细节问题很多。如select,updata,delete等数据层的操作。
