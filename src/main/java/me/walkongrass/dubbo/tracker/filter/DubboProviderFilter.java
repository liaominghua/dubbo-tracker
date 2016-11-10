package me.walkongrass.dubbo.tracker.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

import me.walkongrass.dubbo.tracker.handler.DubboProviderRequestHandler;
import me.walkongrass.dubbo.tracker.model.BaseTraceData;

/**
 * Created by chenjg on 16/7/24.
 */
@Activate(group = Constants.PROVIDER)
public class DubboProviderFilter extends DubboRpcContextBaseFilter {
	
	private final DubboProviderRequestHandler dubboProviderRequestHandler = new DubboProviderRequestHandler();

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
    	dubboProviderRequestHandler.handle(invoker, invocation);
    	Result rpcResult = invoker.invoke(invocation);
    	BaseTraceData.clean();
        return rpcResult;
    }
}
