package threadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test {

	public static void main(String[] args) {
		ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),new MyRejected());
		MyThread t1 = new MyThread(1);
		MyThread t2 = new MyThread(2);
		MyThread t3 = new MyThread(3);
		MyThread t4 = new MyThread(4);
		MyThread t5 = new MyThread(5);
		MyThread t6 = new MyThread(6);
		
		pool.execute(t1);
		pool.execute(t2);
		pool.execute(t3);
		pool.execute(t4);
		pool.execute(t5);
		pool.execute(t6);
		
		pool.shutdown();
	}

}
