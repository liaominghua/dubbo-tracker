/**
 * 
 */
package me.walkongrass.dubbo.tracker.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;

import me.walkongrass.dubbo.tracker.TraceDataUtil;
import me.walkongrass.dubbo.tracker.model.BaseTraceData;
import me.walkongrass.dubbo.tracker.service.DubboTracerService;

/**
 * @author Cacti
 * 
 *	2016年11月9日
 * 
 */
public abstract class DubboBaseHandler {
	private Logger logger = LoggerFactory.getLogger(DubboBaseHandler.class);
	
	private static final ThreadLocal<BaseTraceData> Container = new ThreadLocal<BaseTraceData>();
	
	@Autowired(required=false)
	private DubboTracerService tracerService;
	
	private final ExecutorService executorService = Executors.newFixedThreadPool(5);
	private final LinkedBlockingDeque<BaseTraceData> queue = new LinkedBlockingDeque<BaseTraceData>(100);
	
	public DubboBaseHandler(){
		try{
			
			for(int i = 0 ;i < 5 ;i ++) {
				executorService.submit(new Runnable() {
					
					public void run() {
						while(true){
							try{
								BaseTraceData traceData = queue.take();
								if(getTracerService() != null) {
									try{
										getTracerService() .trace(traceData);
									}catch(Exception e) {
										logger.debug("send trace data error,"+traceData.toString(),e);
									}
								}
								else {
									logger.debug("trace data:"+traceData.toString());
								}
							}catch(Exception e){
								
							}
						}
						
					}
				});
			}
		}catch(Exception e) {
			
		}
	}
	protected void fullfillContext(BaseTraceData baseTraceData){
		if(baseTraceData == null) {
			return ;
		}
		RpcContext context = RpcContext.getContext();
		context.setAttachments(TraceDataUtil.toRpcContextMap(baseTraceData));
	}
	
	protected BaseTraceData fromContext(){
		return TraceDataUtil.fromRpcContextMap(RpcContext.getContext().getAttachments());
	}
	
	protected void sendTraceData(BaseTraceData traceData){
		try{
			if(queue.size() >= 100) {
				//直接丢弃该数据，不阻塞
				return;
			}
			queue.offer(traceData);
		}catch(Exception e) {
			
		}
	}
	
	public abstract void handle(Invoker<?> invoker, Invocation invocation);

	public DubboTracerService getTracerService() {
		return tracerService;
	}

	public void setTracerService(DubboTracerService tracerService) {
		this.tracerService = tracerService;
	}
	
	public static BaseTraceData get(){
		return Container.get();
	}
	
	public static void remove(){
		Container.remove();
	}
	
	public static void add(BaseTraceData traceData){
		Container.set(traceData);
	}
	
}
