## synchronized和lock的区别:
+ Lock 的锁定是通过代码实现的，而 synchronized 是在 JVM 层面上实现的。
+ synchronized 在锁定时如果方法块抛出异常，JVM 会自动将锁释放掉，不会因为出了异常没有释放锁造成线程死锁。但是 Lock 的话就享受不到 JVM 带来自动的功能，出现异常时必须在 finally 将锁释放掉，否则将会引起死锁。
+ 在资源竞争不是很激烈的情况下，偶尔会有同步的情形下，synchronized是很合适的。原因在于，编译程序通常会尽可能的进行优化synchronize，另外可读性非常好，不管用没用过5.0多线程包的程序员都能理解。
## ReentrantLock（重入锁）
如果Lock类只有lock和unlock方法也太简单了，Lock类提供了丰富的加锁的方法和对加锁的情况判断。主要有
+ 实现锁的公平
+ 获取当前线程调用lock的次数，也就是获取当前线程锁定的个数
+ 获取等待锁的线程数
+ 查询指定的线程是否等待获取此锁定
+ 查询是否有线程等待获取此锁定
+ 查询当前线程是否持有锁定
+ 判断一个锁是不是被线程持有
+ 加锁时如果中断则不加锁，进入异常处理
+ 尝试加锁，如果该锁未被其他线程持有的情况下成功

案例：
```
package lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UseReentrantLock {
	
	private static int num = 0;
	Lock lock = new ReentrantLock();
	
	public void add(){
		try {
			lock.lock();
			num++;
			System.out.println(Thread.currentThread().getName()+"-->"+num);
		} catch (Exception e) {
			
		}finally {
			lock.unlock();
		}
	}
	public static void main(String[] args) {

		final UseReentrantLock ur = new UseReentrantLock();
		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					for (int i = 0; i < 100; i++) {
						ur.add();
						Thread.sleep(100);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}, "t1");
		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					for (int i = 0; i < 100; i++) {
						ur.add();
						Thread.sleep(100);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "t2");
		t1.start();
		t2.start();
	}

}
```
输出结果：
```
t1-->1
t2-->2
t1-->3
t2-->4
t1-->5
t2-->6
t1-->7
t2-->8
t1-->9
t2-->10
......
```
## 锁与等待/通知 (Condition)
>Condition是Java提供了来实现等待/通知的类，Condition类还提供比wait/notify更丰富的功能，Condition对象是由lock对象所创建的。但是同一个锁可以创建多个Condition的对象，即创建多个对象监视器。这样的好处就是可以指定唤醒线程。notify唤醒的线程是随机唤醒一个。

案例：显示简单的等待/通知
```
package lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UseCondition {
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();

	public void fun1(){
		try {
			lock.lock();
			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getName()+"等待通知");
			condition.await();
			System.out.println(Thread.currentThread().getName()+"收到通知");
		} catch (Exception e) {
		}finally{
			lock.unlock();
		}
	}
	public void fun2(){
		try {	
			Thread.sleep(3000);
			lock.lock();		
			System.out.println(Thread.currentThread().getName()+"发出通知");
			condition.signal();
		} catch (Exception e) {
		}finally{
			lock.unlock();
		}
	}
	public static void main(String[] args) {
		final UseCondition uc = new UseCondition();
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				uc.fun1();
			}
		}, "t1");
		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				uc.fun2();
			}
		}, "t2");
		t1.start();
		t2.start();
	}
}
```
输出结果：
```
t1等待通知
t2发出通知
t1收到通知
```
