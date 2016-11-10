package me.walkongrass.dubbo.tracker.handler;

import java.net.Inet4Address;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;

import me.walkongrass.dubbo.tracker.model.BaseTraceData;

public class DubboProviderRequestHandler extends DubboBaseHandler {

	@Override
	public void handle(Invoker<?> invoker, Invocation invocation) {
		BaseTraceData upstreamTraceData = fromContext();
		BaseTraceData traceData = new BaseTraceData();
		URL url = invoker.getUrl();
		traceData.setApplicationName(url.getParameter(Constants.APPLICATION_KEY));
		try{
			traceData.setIpAddress(Inet4Address.getLocalHost().getHostAddress());
		}catch(Exception e) {}
		traceData.setService(url.getServiceName());
		traceData.setMethod(invocation.getMethodName());
		traceData.setVersion(url.getParameter(Constants.VERSION_KEY));
		traceData.setType("provider");
		if(upstreamTraceData != null) {
			traceData.setHop(upstreamTraceData.getHop()+1);
			traceData.setGroupId(upstreamTraceData.getGroupId());
		}
		else {
			
		}
		
		BaseTraceData.add(traceData);
		sendTraceData(traceData);
	}
	
	

}
