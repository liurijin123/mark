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
			Thread.sleep(3000);
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
			Thread.sleep(1000);
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
