package com.psc.pt.util;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;

public class ParamsUtil {
	private static final Logger LOG = Logger.getLogger(ParamsUtil.class);
	/**
	 * 参数处理、源数据request、转码
	 * @throws UnsupportedEncodingException 
	 */
	public static void doParams(String[] paramsList, HttpServletRequest request, Map<String, Object> paramsMap) throws UnsupportedEncodingException{
		paramsMap.clear();
		for (int i = 0; i < paramsList.length; i++) {
			if(StringUtils.isNotBlank(request.getParameter(paramsList[i]))){
				paramsMap.put(paramsList[i],new String(request.getParameter(paramsList[i]).getBytes("iso-8859-1"), "UTF-8"));
			}
		}
		LOG.info(paramsMap.toString());
	}
	
	/**
	 * 参数处理、源数据request、不转码
	 * @throws UnsupportedEncodingException 
	 */
	public static void doParams1(String[] paramsList, HttpServletRequest request, Map<String, Object> paramsMap) throws UnsupportedEncodingException{
		paramsMap.clear();
		for (int i = 0; i < paramsList.length; i++) {
			if(StringUtils.isNotBlank(request.getParameter(paramsList[i]))){
				paramsMap.put(paramsList[i],request.getParameter(paramsList[i]));
			}
		}
		LOG.info(paramsMap.toString());
	}
	
	/**
	 * 参数处理带ModelMap、源数据request、转码
	 * @throws UnsupportedEncodingException 
	 */
	public static void doParams(String[] paramsList, HttpServletRequest request, Map<String, Object> paramsMap, ModelMap map) throws UnsupportedEncodingException{
		paramsMap.clear();
		map.clear();
		for (int i = 0; i < paramsList.length; i++) {
			if(StringUtils.isNotBlank(request.getParameter(paramsList[i]))){
				paramsMap.put(paramsList[i],new String(request.getParameter(paramsList[i]).getBytes("iso-8859-1"), "UTF-8"));
				map.put(paramsList[i],new String(request.getParameter(paramsList[i]).getBytes("iso-8859-1"), "UTF-8"));
			}
		}
		LOG.info(paramsMap.toString());
	}
	/**
	 * 数据处理带ModelMap、源数据json、不转码
	 * @param paramsList
	 * @param dataObj
	 * @param paramsMap
	 * @param map
	 */
	public static void doParams(String[] paramsList, JSONObject dataObj, Map<String, Object> paramsMap, ModelMap map) {
		paramsMap.clear();
		map.clear();
		for(int i = 0; i < paramsList.length; i++){
			if(StringUtils.isNotBlank(dataObj.optString(paramsList[i]))){
				paramsMap.put(paramsList[i], dataObj.optString(paramsList[i]));
				map.put(paramsList[i], dataObj.optString(paramsList[i]));
			}
		}
	}
	/**
	 * 数据处理源数据json、不转码
	 * @param paramsList
	 * @param dataObj
	 * @param paramsMap
	 * @param map
	 */
	public static void doParams(String[] paramsList, JSONObject dataObj, Map<String, Object> paramsMap) {
		paramsMap.clear();
		for(int i = 0; i < paramsList.length; i++){
			if(StringUtils.isNotBlank(dataObj.optString(paramsList[i]))){
				paramsMap.put(paramsList[i], dataObj.optString(paramsList[i]));
			}
		}
	}
}
