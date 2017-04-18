package com.psc.pt.controller.pacs;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rubyeye.xmemcached.KeyIterator;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.utils.AddrUtil;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.psc.pt.constants.ConstantsInterior;
import com.psc.pt.model.pacs.CaseListInfo;
import com.psc.pt.service.pacs.CaseService;
import com.psc.pt.util.ResultUtil;

@SuppressWarnings("deprecation")
@Controller
@RequestMapping("/longpoling")
public class LongPolingController {
	private final static Logger LOG = Logger.getLogger(LongPolingController.class);
	@Autowired private MemcachedClient memcachedClient;
	@Autowired CaseService caseService;
	/**
	 * 获取可抢单列表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method=RequestMethod.GET, value="getCase0List.htm")
	public void getCase0List(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//加长请求停顿时间，防止一直轮询，最长14*500毫秒
		List<CaseListInfo> caseListInfos = new ArrayList<CaseListInfo>();
		Thread.sleep(1000);
		for(int i = 0; i < 14; i++){
			KeyIterator it=memcachedClient.getKeyIterator(AddrUtil.getOneAddress("localhost:11211"));
			while(it.hasNext()){
				String key = it.next();
				if(key.contains(ConstantsInterior.CASE_PACS)){
					caseListInfos.add((CaseListInfo) memcachedClient.get(key));
				}
			}
			if(! caseListInfos.isEmpty()){
				break;
			}
			Thread.sleep(500);
		}
		JSONArray arr = JSONArray.fromObject(caseListInfos);
		ResultUtil.wirteResult(response, arr.toString());
	}
}
