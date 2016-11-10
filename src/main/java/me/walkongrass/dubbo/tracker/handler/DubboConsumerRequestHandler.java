/**
 * 
 */
package me.walkongrass.dubbo.tracker.handler;

import java.net.Inet4Address;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;

import me.walkongrass.dubbo.tracker.model.BaseTraceData;

/**
 * @author Cacti
 * 
 *	2016年11月9日
 * 
 */
public class DubboConsumerRequestHandler extends DubboBaseHandler{
	
	public void handle(Invoker<?> invoker, Invocation invocation){
		BaseTraceData upstreamData = BaseTraceData.get();
		
		BaseTraceData traceData = new BaseTraceData();
		URL url = invoker.getUrl();
		traceData.setApplicationName(url.getParameter(Constants.APPLICATION_KEY));
		try{
			traceData.setIpAddress(Inet4Address.getLocalHost().getHostAddress());
		}catch(Exception e) {}
		traceData.setService(url.getServiceName());
		traceData.setMethod(invocation.getMethodName());
		traceData.setVersion(url.getParameter(Constants.VERSION_KEY));
		traceData.setType("consumer");
		if(upstreamData != null) {
			traceData.setHop(upstreamData.getHop()+1);
			traceData.setGroupId(upstreamData.getGroupId());
		}
		else {
			
		}
		
		fullfillContext(traceData);

		sendTraceData(traceData);
	}
	

}
