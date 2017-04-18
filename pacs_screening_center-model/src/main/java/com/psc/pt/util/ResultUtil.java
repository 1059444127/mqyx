package com.psc.pt.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

/**
 * 返回工具
 * @author YQ
 *
 */
public class ResultUtil {
	/**
	 * 返回json数据生成
	 * @param R
	 * @param I
	 * @param obj
	 * @return
	 */
	public static JSONObject getResJson(int R, String I, JSONObject obj){
		JSONObject resJson = new JSONObject();
		resJson.put("R", R);
		resJson.put("I", I);
		if(null != obj){
			resJson.put("data", obj);
		}
		return resJson;
	}
	/**
	 * PrintWriter输出
	 * @param response
	 * @param msg
	 * @throws IOException 
	 */
	public static void wirteResult(HttpServletResponse response, String msg) throws IOException{
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.write(msg);
		writer.flush();
	}
}
