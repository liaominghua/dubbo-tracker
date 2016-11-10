/**
 * 
 */
package me.walkongrass.dubbo.tracker.service;

import me.walkongrass.dubbo.tracker.model.BaseTraceData;

/**
 * @author Cacti
 * 
 *	2016年11月10日
 * 
 */
public interface DubboTracerService {
	public void trace(BaseTraceData tradeData);
}
