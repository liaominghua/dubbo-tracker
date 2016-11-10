package me.walkongrass.dubbo.tracker;
/**
 * 
 */

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import me.walkongrass.dubbo.tracker.model.BaseTraceData;

/**
 * @author Cacti
 * 
 *	2016年11月10日
 * 
 */
public final class TraceDataUtil {
	private TraceDataUtil(){}
	
	public static BaseTraceData fromRpcContextMap(Map<String, String> map){
		if(map == null) {
			return null;
		}
		else {
			BaseTraceData parentData = new BaseTraceData();
			if(!StringUtils.isEmpty(map.get(Constants.groupId))){
				parentData.setGroupId(map.get(Constants.groupId));
			}
			if(!StringUtils.isEmpty(map.get(Constants.hop))){
				parentData.setHop(Integer.valueOf(map.get(Constants.hop)));
			}
			
			return parentData;
		}
	}
	
	public static Map<String,String> toRpcContextMap(BaseTraceData traceData){
		if(traceData == null) {
			return new HashMap<String, String>();
		}
		
		Map<String,String> map = new HashMap<String, String>();
		map.put(Constants.groupId,traceData.getGroupId());
		map.put(Constants.hop,String.valueOf(traceData.getHop()));
		return map;
	}
}
