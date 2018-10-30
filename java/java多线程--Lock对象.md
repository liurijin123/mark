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
## ReentrantReadWriteLock(读写锁)
      读写锁ReentrantReadWriteLock，其核心就是实现读写分离的锁。在高并发访问下，尤其是读多写少的情况下，性能要远高于重入锁。

      之前学synchronized、ReentrantLock时，我们知道，同一时间内，只能有一个线程进行访问被镇定的代码，那么读写锁则不同，其本质是分成两个锁，即读锁、写锁。在读锁下，多个线程可以并发的进行访问，但是在写锁的时候，只能一个一个的顺序访问。

口诀：读读共享，写写互斥，读写互斥。
案例：
```
package lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class UseReentrantReadWriteLock {

	private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private ReadLock readLock = rwLock.readLock();
    private WriteLock writeLock = rwLock.writeLock();
    
    public void read(){
        try {
            readLock.lock();
            System.out.println("当前线程:" + Thread.currentThread().getName() + "进入...");
            Thread.sleep(3000);
            System.out.println("当前线程:" + Thread.currentThread().getName() + "退出...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    public void write(){
        try {
            writeLock.lock();
            System.out.println("当前线程:" + Thread.currentThread().getName() + "进入...");
            Thread.sleep(3000);
            System.out.println("当前线程:" + Thread.currentThread().getName() + "退出...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {

        final UseReentrantReadWriteLock urrw = new UseReentrantReadWriteLock();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                urrw.read();
            }
        }, "t1");
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                urrw.read();
            }
        }, "t2");
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                urrw.write();
            }
        }, "t3");
        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                urrw.write();
            }
        }, "t4");       

        //读读共享
        t1.start();
        t2.start();

        //读写互斥
//      t1.start(); // R
//      t3.start(); // W

        //写写互斥
//      t3.start();
//      t4.start();


    }
}
```
输出结果：

当启动 t1 , t2 时 打印结果为：
```
当前线程:t2进入...
当前线程:t1进入...
当前线程:t2退出...
当前线程:t1退出...
```
如果你注意观看打印结果，发现t1 , t2同时打印，这也是是我们口诀中的“读读共享”。

当启动 t1 , t3 时 打印结果为：
```
当前线程:t1进入...
当前线程:t1退出...
当前线程:t3进入...
当前线程:t3退出...
```
如果你注意观看打印结果，发现t1线程执行完后，t3线程才开始执行，这也是是我们口诀中的“读写互斥”。

当启动 t3 , t4 时 打印结果为：
```
当前线程:t3进入...
当前线程:t3退出...
当前线程:t4进入...
当前线程:t4退出...
```
如果你注意观看打印结果，发现t3线程执行完后，t4线程才开始执行，这也是是我们口诀中的“写写互斥”。
