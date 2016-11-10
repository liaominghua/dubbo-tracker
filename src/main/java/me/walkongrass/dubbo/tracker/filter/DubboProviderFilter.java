package me.walkongrass.dubbo.tracker.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

import me.walkongrass.dubbo.tracker.handler.DubboBaseHandler;
import me.walkongrass.dubbo.tracker.handler.DubboProviderRequestHandler;
import me.walkongrass.dubbo.tracker.service.DubboTracerService;

/**
 * Created by chenjg on 16/7/24.
 */
@Activate(group = Constants.PROVIDER)
@Component
public class DubboProviderFilter extends DubboRpcContextBaseFilter {
	
	@Autowired
	private DubboProviderRequestHandler dubboProviderRequestHandler;

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
    	try{
    		if(invoker.getUrl().getServiceInterface().equals(DubboTracerService.class.getName())){
        		return invoker.invoke(invocation);
        	}
    		dubboProviderRequestHandler.handle(invoker, invocation);
        	Result rpcResult = invoker.invoke(invocation);
            return rpcResult;
    	}catch(RpcException ex) {
    		throw ex;
    	}finally{
        	DubboBaseHandler.remove();

    	}
    	
    }

	public DubboProviderRequestHandler getDubboProviderRequestHandler() {
		return dubboProviderRequestHandler;
	}

	public void setDubboProviderRequestHandler(DubboProviderRequestHandler dubboProviderRequestHandler) {
		this.dubboProviderRequestHandler = dubboProviderRequestHandler;
	}
}
