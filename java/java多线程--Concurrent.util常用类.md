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
案例：
```
package concurrent;

import java.util.concurrent.CountDownLatch;

public class UseCountDownLatch {

	public static void main(String[] args) {
		final CountDownLatch countDown = new CountDownLatch(2);
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {		
				try {
					System.out.println("进入线程1，等待其他线程初始化完成");
					countDown.await();
					System.out.println("线程1收到通知，继续执行");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "t1");
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {	
				try {
					System.out.println("线程2进行初始化操作");
					Thread.sleep(3000);
					System.out.println("线程2初始化完成");
					countDown.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}		
			}
		}, "t2");
		Thread t3 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					System.out.println("线程3进行初始化操作");
					Thread.sleep(3000);
					System.out.println("线程3初始化完成");
					countDown.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "t3");
		t1.start();
		t2.start();
		t3.start();
	}
}
```
输出结果：
```
线程3进行初始化操作
进入线程1，等待其他线程初始化完成
线程2进行初始化操作
线程3初始化完成
线程2初始化完成
线程1收到通知，继续执行
```
## 3.Callable和Future的使用
>Future是一个设计模式，可以实现异步获取数据的。Future模式是非常合适在处理耗时很长的业务逻辑时进行使用，可以有效的减少系统的响应时间，提高系统的吞吐量。
### Callable与Runnable
先说一下java.lang.Runnable吧，它是一个接口，在它里面只声明了一个run()方法：
```
public interface Runnable {
    public abstract void run();
}
```
由于run()方法返回值为void类型，所以在执行完任务之后无法返回任何结果。

Callable位于java.util.concurrent包下，它也是一个接口，在它里面也只声明了一个方法，只不过这个方法叫做call()：
```
public interface Callable<V> {
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    V call() throws Exception;
}
```
可以看到，这是一个泛型接口，call()函数返回的类型就是传递进来的V类型。

那么怎么使用Callable呢？一般情况下是配合ExecutorService来使用的，在ExecutorService接口中声明了若干个submit方法的重载版本：
```
<T> Future<T> submit(Callable<T> task);
<T> Future<T> submit(Runnable task, T result);
Future<?> submit(Runnable task);
```
第一个submit方法里面的参数类型就是Callable。

暂时只需要知道Callable一般是和ExecutorService配合来使用的，具体的使用方法讲在后面讲述。

一般情况下我们使用第一个submit方法和第三个submit方法，第二个submit方法很少使用。
### Future
Future就是对于具体的Runnable或者Callable任务的执行结果进行取消、查询是否完成、获取结果。必要时可以通过get方法获取执行结果，该方法会阻塞直到任务返回结果。

Future类位于java.util.concurrent包下，它是一个接口：
```
public interface Future<V> {
    boolean cancel(boolean mayInterruptIfRunning);
    boolean isCancelled();
    boolean isDone();
    V get() throws InterruptedException, ExecutionException;
    V get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;
}
```
在Future接口中声明了5个方法，下面依次解释每个方法的作用：

+ cancel方法用来取消任务，如果取消任务成功则返回true，如果取消任务失败则返回false。参数mayInterruptIfRunning表示是否允许取消正在执行却没有执行完毕的任务，如果设置true，则表示可以取消正在执行过程中的任务。如果任务已经完成，则无论mayInterruptIfRunning为true还是false，此方法肯定返回false，即如果取消已经完成的任务会返回false；如果任务正在执行，若mayInterruptIfRunning设置为true，则返回true，若mayInterruptIfRunning设置为false，则返回false；如果任务还没有执行，则无论mayInterruptIfRunning为true还是false，肯定返回true。
+ isCancelled方法表示任务是否被取消成功，如果在任务正常完成前被取消成功，则返回 true。
+ isDone方法表示任务是否已经完成，若任务完成，则返回true；
+ get()方法用来获取执行结果，这个方法会产生阻塞，会一直等到任务执行完毕才返回；
+ get(long timeout, TimeUnit unit)用来获取执行结果，如果在指定时间内，还没获取到结果，就直接返回null。
　　也就是说Future提供了三种功能：

　　1）判断任务是否完成；

　　2）能够中断任务；

　　3）能够获取任务执行结果。

　　因为Future只是一个接口，所以是无法直接用来创建对象使用的，因此就有了下面的FutureTask。
### FutureTask
我们先来看一下FutureTask的实现：
```
public class FutureTask<V> implements RunnableFuture<V>
```
FutureTask类实现了RunnableFuture接口，我们看一下RunnableFuture接口的实现：
```
public interface RunnableFuture<V> extends Runnable, Future<V> {
    void run();
}
```
可以看出RunnableFuture继承了Runnable接口和Future接口，而FutureTask实现了RunnableFuture接口。所以它既可以作为Runnable被线程执行，又可以作为Future得到Callable的返回值。

FutureTask提供了2个构造器：
```
public FutureTask(Callable<V> callable) {
}
public FutureTask(Runnable runnable, V result) {
}
```
事实上，FutureTask是Future接口的一个唯一实现类。
案例：假设我们现在有一个任务，要计算出1-10000之间的所有数字的和，为了提升计算速度，我们使用两个线程，第一个线程计算1-5000的和，另外有一个线程计算5001-10000之间的数字的和。
```
package concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class UseFuture {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newCachedThreadPool();
		AndNumTask task1 = new AndNumTask(0, 5000);
		AndNumTask task2 = new AndNumTask(5001, 10000);
		FutureTask<Integer> future1 = new FutureTask<Integer>(task1);
		FutureTask<Integer> future2 = new FutureTask<Integer>(task2);
		executor.submit(future1);
		executor.submit(future2);
		executor.shutdown();
		while(true){
			if(future1.isDone() && !future1.isCancelled() && future2.isDone() && !future2.isCancelled()){
				int sum = future1.get() + future2.get();
				System.out.println(sum);
				break;
			}
			else{
				Thread.sleep(100);
			}
		}
	}	
}
class AndNumTask implements Callable<Integer>{

	private int start;
	private int end;
	public AndNumTask(int start,int end){
		this.start = start;
		this.end = end;
	}
	@Override
	public Integer call() throws Exception {
		int sum = 0;
		for(int i=start;i<=end;i++){
			sum += i; 
		}
		Thread.sleep(5000);
		return sum;
	}	
}
```
输出结果：
>50005000
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

容量评估：一般来说通过开发、运维、测试、以及业务等相关人员．綜合出系统的一系列阈值，然后我们根据关键阈值如qps、rt等，对系统讲行有效的变更。一般来讲．我们讲行多轮压力测试以后，可以对系统讲行峰值评估，采用所谓的80/20原则，即80％的访网请求将在20％的时间内达到。这样我们可以根据系统对应PV计算出峰值 qps。

峰值qps= (总PV ×80％）/（60 × 60× 24 ×20％）

然后在将总的峰值qps除以单台机器所能承受的最高的qps值，就是所需要机器的数量：机器数=总的峰值qps /压测得出的单机极限qps

当然不排除系统在上线前进行大型促销活动，或者双十一、双十二热点事件、遭受到DDos攻击等情况，系统的开发和运维人员急需要了解当前系统运行的状态和负载情况，一般都会有后台系统去维护。

Semaphone可以控制系统的流量: 
拿到信号量的线程可以讲入，否则就等待.通过acquire()和rekease()获取和释放访同许可。
案例：
```
package concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class UseSemaphore {

	public static void main(String[] args) {
		// 线程池
		ExecutorService exec = Executors.newCachedThreadPool();
		// 只能5个线程同时访问
		final Semaphore semp = new Semaphore(5);
		// 模拟20个客户端访问
		for (int index = 0; index < 20; index++) {
			final int NO = index;
			Runnable run = new Runnable() {
				public void run() {
					try {
						// 获取许可
						semp.acquire();
						System.out.println("Accessing: " + NO + "获得许可");
						// 模拟实际业务逻辑
						Thread.sleep((long) (Math.random() * 10000));
						// 访问完后，释放
						System.out.println("Accessing: " + NO + "释放许可");
						semp.release();
					} catch (InterruptedException e) {
					}
				}
			};
			exec.execute(run);
		}

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// System.out.println(semp.getQueueLength());

		// 退出线程池
		exec.shutdown();
	}

}
```
输出结果：
```
Accessing: 0获得许可
Accessing: 2获得许可
Accessing: 3获得许可
Accessing: 1获得许可
Accessing: 5获得许可
Accessing: 1释放许可
Accessing: 4获得许可
Accessing: 5释放许可
Accessing: 6获得许可
Accessing: 3释放许可
Accessing: 7获得许可
Accessing: 0释放许可
Accessing: 8获得许可
Accessing: 2释放许可
Accessing: 9获得许可
Accessing: 6释放许可
Accessing: 10获得许可
Accessing: 8释放许可
Accessing: 11获得许可
Accessing: 7释放许可
Accessing: 12获得许可
Accessing: 9释放许可
Accessing: 13获得许可
Accessing: 12释放许可
Accessing: 14获得许可
Accessing: 4释放许可
Accessing: 15获得许可
Accessing: 13释放许可
Accessing: 16获得许可
Accessing: 14释放许可
Accessing: 17获得许可
Accessing: 15释放许可
Accessing: 18获得许可
Accessing: 10释放许可
Accessing: 19获得许可
Accessing: 11释放许可
Accessing: 19释放许可
Accessing: 18释放许可
Accessing: 16释放许可
Accessing: 17释放许可
```
