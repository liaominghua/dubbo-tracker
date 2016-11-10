package me.walkongrass.dubbo.tracker.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

import me.walkongrass.dubbo.tracker.handler.DubboConsumerRequestHandler;

/**
 * Created by chenjg on 16/7/24.
 */
@Activate(group = Constants.CONSUMER)
public class DubboConsumerFilter extends DubboRpcContextBaseFilter {
	
	private DubboConsumerRequestHandler dubboConsumerRequestHandler = new DubboConsumerRequestHandler();


    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try{
        	dubboConsumerRequestHandler.handle(invoker, invocation);
            Result rpcResult = invoker.invoke(invocation);
            return rpcResult;
        }catch (RpcException ex){
            throw  ex;
        }finally {
        	cleanup();
        }
    }
}
