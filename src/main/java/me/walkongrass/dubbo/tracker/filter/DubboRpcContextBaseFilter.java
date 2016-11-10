/**
 * 
 */
package me.walkongrass.dubbo.tracker.filter;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.RpcContext;

/**
 * @author Cacti
 * 
 *	2016年11月9日
 * 
 */
public abstract class DubboRpcContextBaseFilter implements Filter {

	 protected void cleanup() {
		 RpcContext.removeContext();
		 
	 }
}
