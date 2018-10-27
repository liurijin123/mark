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
