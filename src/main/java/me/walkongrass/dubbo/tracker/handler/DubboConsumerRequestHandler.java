/**
 * 
 */
package me.walkongrass.dubbo.tracker.handler;

import java.net.InetSocketAddress;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
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
public class DubboConsumerRequestHandler extends DubboBaseHandler{
	
	private Logger logger = LoggerFactory.getLogger(DubboConsumerRequestHandler.class);
	
	
	public void handle(Invoker<?> invoker, Invocation invocation){
		try{
			BaseTraceData upstreamData = DubboBaseHandler.get();
			
			BaseTraceData traceData = new BaseTraceData();
			URL url = invoker.getUrl();
			traceData.setApplicationName(url.getParameter(Constants.APPLICATION_KEY));
			try{
				InetSocketAddress inetSocketAddress = RpcContext.getContext().getRemoteAddress();
		        String ipAddr = RpcContext.getContext().getUrl().getIp();
				traceData.setIpAddress(ipAddr);
				traceData.setPort(String.valueOf(inetSocketAddress.getPort()));
			}catch(Exception e) {}
			traceData.setService(url.getServiceName());
			traceData.setMethod(invocation.getMethodName());
			traceData.setVersion(url.getParameter(Constants.VERSION_KEY));
			traceData.setType("consumer");
			if(upstreamData != null) {
				traceData.setHop(upstreamData.getHop()+1);
				traceData.setGroupId(upstreamData.getGroupId());
			}
			
			fullfillContext(traceData);

			sendTraceData(traceData);
		}catch(Exception e) {
				logger.debug("error:",e);
		}
		
	}
	

}
