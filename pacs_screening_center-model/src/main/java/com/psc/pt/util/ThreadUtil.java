package com.psc.pt.util;

import org.apache.log4j.Logger;

public class ThreadUtil {
	private static final Logger LOG = Logger.getLogger(ThreadUtil.class);
	
	/**
	 * 根据线程名获取线程对象
	 * @param threadName
	 * @return
	 */
	public static Thread getThreadByName(String threadName){
		Thread thread = null;
		for(Thread T : Thread.getAllStackTraces().keySet()){
			if(threadName.equals(T.getName())){
				thread = T;
				break;
			}
		}
		return thread;
	}

}
