package threadPool;

public class MyThread implements Runnable{

	int threadId ;
	
	
	public MyThread(int threadId ) {
		this.threadId = threadId;
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
			System.out.println("线程ID："+getThreadId());
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
