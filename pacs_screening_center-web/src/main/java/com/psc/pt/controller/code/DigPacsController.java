package com.psc.pt.controller.code;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.psc.pt.model.pacs.DigPacs;
import com.psc.pt.service.pacs.DigPacsService;
import com.psc.pt.util.ParamsUtil;
import com.psc.pt.util.ResultUtil;


@Controller
@RequestMapping(value="/DigPacs")
public class DigPacsController {
	private static final Logger LOG = Logger.getLogger(DigPacsController.class);
	@Autowired DigPacsService digPacsService;
	/**
	 * 获取节点
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(method=RequestMethod.GET, value="/getDigPacs.htm")
	public void getDigPacs(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setHeader("Access-Control-Allow-Origin", "*");
		String[] paramsList = {"pId"};
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		ParamsUtil.doParams(paramsList, request, paramsMap);
		List<DigPacs> dpList = digPacsService.getDigPacs(paramsMap);
		JSONArray digPacsArray = new JSONArray();
		for(DigPacs dp : dpList){
			JSONObject digPacsObject = new JSONObject();
			digPacsObject.put("id", dp.getId());
			digPacsObject.put("pId", dp.getpId());
			digPacsObject.put("name", dp.getName());
			digPacsObject.put("type", dp.getType());
			digPacsObject.put("open", true);
			if(!"rept".equals(dp.getType())){
				digPacsObject.put("isParent", true);
			}
			digPacsArray.add(digPacsObject);
		}
		ResultUtil.wirteResult(response, digPacsArray.toString());
	}
	
	/**
	 * 获取报告
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(method=RequestMethod.GET, value="/getDigPacsDetails.htm")
	public void getDigPacsDetails(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setHeader("Access-Control-Allow-Origin", "*");
		String[] paramsList = {"id"};
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		ParamsUtil.doParams(paramsList, request, paramsMap);
		DigPacs dp = digPacsService.getDigPacsDetails(Long.parseLong((String)paramsMap.get("id")));
		JSONObject resultJson = new JSONObject();
		resultJson.put("A", dp.getTemp1());
		resultJson.put("B", dp.getTemp2());
		ResultUtil.wirteResult(response, resultJson.toString());
	}
}
