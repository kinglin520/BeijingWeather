package com.superman.beijingweather.utils;

/**
 * @author wenlin
 *
 */
public interface IEventListener {
	/**
	 * eventProcess:(事件回调处理方法). <br/>
	 * date: 2015年8月24日 <br/>
	 *
	 * @author wl
	 * @param eventType
	 * @param objects
	 * @throws Exception
	 */
	 void eventProcess(int eventType, Object... objects)
			throws Exception;
}
