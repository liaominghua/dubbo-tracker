/**
 * 
 */
package me.walkongrass.dubbo.tracker.handler;

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
		
		if(this.tracerService != null) {
			try{
				tracerService.trace(traceData);
			}catch(Exception e) {
				logger.debug("send trace data error,"+traceData.toString(),e);
			}
		}
		else {
			logger.debug("trace data:"+traceData.toString());
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
