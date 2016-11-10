/**
 * 
 */
package me.walkongrass.dubbo.tracker.handler;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcContext;

import me.walkongrass.dubbo.tracker.model.BaseTraceData;

/**
 * @author Cacti
 * 
 *	2016年11月9日
 * 
 */
public abstract class DubboBaseHandler {
	protected void fullfillContext(BaseTraceData baseTraceData){
		if(baseTraceData == null) {
			return ;
		}
		RpcContext context = RpcContext.getContext();
		context.setAttachments(baseTraceData.toRpcContextMap());
	}
	
	protected BaseTraceData fromContext(){
		return BaseTraceData.fromRpcContextMap(RpcContext.getContext().getAttachments());
	}
	
	protected void sendTraceData(BaseTraceData traceData){
		System.out.println(traceData.toString());
	}
	
	public abstract void handle(Invoker<?> invoker, Invocation invocation);
	
}
