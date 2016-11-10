package me.walkongrass.dubbo.tracker.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import me.walkongrass.dubbo.tracker.Constants;

public class BaseTraceData implements Serializable {
	private static final ThreadLocal<BaseTraceData> Container = new ThreadLocal<BaseTraceData>();
	
	/**
	 * 应用名称
	 */
	private String applicationName;
	
	/**
	 * 本地IP地址
	 */
	private String ipAddress;
	
	/**
	 * 服务接口名称
	 */
	private String service;
	
	/**
	 * 服务接口版本
	 */
	private String version;
	
	/**
	 * 服务接口方法
	 */
	private String method;
	
	/**
	 * 时间戳
	 */
	private Date timestamp = new Date();
	
	/**
	 * 数据类型,provider or consumer
	 */
	private String type;
	
	/**
	 * 记录调用深度,从1开始
	 */
	private Integer hop = 1;
	
	/**
	 * 用来方便所有调用进行分组
	 */
	private String groupId = UUID.randomUUID().toString();

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getHop() {
		return hop;
	}

	public void setHop(Integer hop) {
		this.hop = hop;
	}


	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public Map<String,String> toRpcContextMap(){
		Map<String,String> map = new HashMap<String, String>();
		map.put(Constants.groupId,getGroupId());
		map.put(Constants.hop,String.valueOf(getHop()));
		return map;
	}
	
	public static BaseTraceData fromRpcContextMap(Map<String, String> map){
		if(map == null) {
			return null;
		}
		else {
			BaseTraceData parentData = new BaseTraceData();
			parentData.setGroupId(map.get(Constants.groupId));
			parentData.setHop(Integer.valueOf(map.get(Constants.hop)));
			return parentData;
		}
	}
	
	public String toString(){
		return ReflectionToStringBuilder.toString(this);
	}
	
	public static BaseTraceData get(){
		return Container.get();
	}
	
	public static void clean(){
		Container.remove();
	}
	
	public static void add(BaseTraceData traceData){
		Container.set(traceData);
	}
}
