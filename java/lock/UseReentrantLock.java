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
