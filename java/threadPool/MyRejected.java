package threadPool;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class MyRejected implements RejectedExecutionHandler{

	@Override
	public void rejectedExecution(Runnable t, ThreadPoolExecutor arg1) {
		System.out.println("自定义处理..");
		System.out.println("当前被拒绝任务为：" + t.toString());
	}	
}
