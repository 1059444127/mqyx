package com.psc.pt.controller.pacs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.psc.pt.constants.ConstantsInterior;
import com.psc.pt.model.pacs.CaseDig;
import com.psc.pt.model.pacs.CaseListInfo;
import com.psc.pt.model.pacs.CasePacs;
import com.psc.pt.model.user.UserInfo;
import com.psc.pt.service.pacs.CaseService;
import com.psc.pt.thread.CaseInvalidThread;
import com.psc.pt.util.ParamsUtil;
import com.psc.pt.util.ResultUtil;
import com.psc.pt.util.ThreadUtil;

@Controller
@RequestMapping(value="/case")
/**
 * 病例类
 * @author YQ
 *
 */
public class CaseController {
	@Autowired private MemcachedClient memcachedClient;
	@Autowired CaseService caseService;
	/**
	 * 一般用户病例列表页
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET, value="mainCaseList.htm")
	public String mainCaseList(HttpServletRequest request, HttpServletResponse response, ModelMap map){
		UserInfo uInfo = (UserInfo) request.getSession().getAttribute(ConstantsInterior.USER_INFO);
		map.put("ui", uInfo);
		return "/admin/screen/case/caseList";
	}
	/**
	 * 获取个人列表
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(method=RequestMethod.GET, value="getCaseList.htm")
	public void getCaseList(HttpServletRequest request, HttpServletResponse response) throws IOException{
		UserInfo uInfo = (UserInfo) request.getSession().getAttribute(ConstantsInterior.USER_INFO);
		JSONObject resJson = new JSONObject();
		String[] paramsList = {"status", "pageNo","pageSize"};
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		ParamsUtil.doParams(paramsList, request, paramsMap);
		//医生信息
		paramsMap.put("doctorId", uInfo.getUserCheckId());
		//limit处理
		Long pageNo = paramsMap.get("pageNo") == null? 1L : Long.parseLong((String)paramsMap.get("pageNo"));
		Long pageSize = paramsMap.get("pageSize") == null? 10L : Long.parseLong((String)paramsMap.get("pageSize"));
		Long limitS = (pageNo - 1L) * pageSize;
		Long limitE = pageSize;
		paramsMap.put("limitS", limitS);
		paramsMap.put("limitE", limitE);
		//总数,总页数
		Long count = caseService.selectListCount(paramsMap);
		Long pageCount = count % pageSize == 0 ? count/pageSize : count/pageSize + 1;
		if(pageCount == 0L){
			pageCount = 1L;
		}
		//取信息
		List<CaseListInfo> clis = caseService.selectList(paramsMap);
		//包装好return
		JSONArray cliArray = JSONArray.fromObject(clis);
		JSONObject obj = new JSONObject();
		obj.put("pageCount", pageCount);
		obj.put("cliArray", cliArray);
		obj.put("count", count);
		resJson = ResultUtil.getResJson(1, "成功", obj);
		ResultUtil.wirteResult(response, resJson.toString());
	}
	
	/**
	 * 获取个人统计
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(method=RequestMethod.GET, value="getPersonAllCount.htm")
	public void getPersonAllCount(HttpServletRequest request, HttpServletResponse response) throws IOException{
		UserInfo uInfo = (UserInfo) request.getSession().getAttribute(ConstantsInterior.USER_INFO);
		JSONObject resJson = new JSONObject();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		//医生信息
		paramsMap.put("doctorId", uInfo.getUserCheckId());
		//取信息temp1~4
		CaseListInfo cli = caseService.selectAllCount(paramsMap);
		JSONObject obj = JSONObject.fromObject(cli);
		resJson = ResultUtil.getResJson(1, "成功", obj);
		ResultUtil.wirteResult(response, resJson.toString());
	}
	
	/**
	 * 抢单
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws MemcachedException 
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 */
	@RequestMapping(method=RequestMethod.GET, value="getCase.htm")
	public void getCase(HttpServletRequest request, HttpServletResponse response) throws IOException, TimeoutException, InterruptedException, MemcachedException{
		//医生信息
		UserInfo ui = (UserInfo) request.getSession().getAttribute(ConstantsInterior.USER_INFO);
		//数据处理
		JSONObject resJson = new JSONObject();
		String[] paramsList = {"caseId"};
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		ParamsUtil.doParams(paramsList, request, paramsMap);
		CaseListInfo cli = caseService.selectCaseListInfoByCaseId(Long.parseLong((String)paramsMap.get("caseId")));
		//判断号还在否？是否需要同步锁？？后面再考虑....
		if( ! "0".equals(cli.getStatus())){
			resJson = ResultUtil.getResJson(0, "手慢了，此单已被抢走！", null);
			ResultUtil.wirteResult(response, resJson.toString());
			return;
		}
		cli.setStatus("1");
		cli.setDoctorId(ui.getUserCheckId());
		cli.setGetDate(new Date());
		cli.setMissingTime(new Date().getTime() + 20 * 60000);//20分钟诊断时间
		//cli.setMissingTime(new Date().getTime() + 10000);//20分钟诊断时间
		caseService.updateCaseListInfo(cli);
		/*跑计时线程*/
		CaseInvalidThread cit = new CaseInvalidThread(caseService, cli, memcachedClient);
		cit.setName(ConstantsInterior.CASE_PACS + cli.getId());
		cit.start();
		//删除缓存
		memcachedClient.delete(ConstantsInterior.CASE_PACS + cli.getId());
		resJson = ResultUtil.getResJson(1, "抢单成功", null);
		ResultUtil.wirteResult(response, resJson.toString());
	}
	
	/**
	 * 病例详情
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws MemcachedException 
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 */
	@RequestMapping(method=RequestMethod.GET, value="getCaseDetail.htm")
	public void getCaseDetail(HttpServletRequest request, HttpServletResponse response) throws IOException, TimeoutException, InterruptedException, MemcachedException{
		UserInfo uInfo = (UserInfo) request.getSession().getAttribute(ConstantsInterior.USER_INFO);
		JSONObject resJson = new JSONObject();
		String[] paramsList = {"caseId"};
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		ParamsUtil.doParams(paramsList, request, paramsMap);
		//取case
		CaseListInfo cli = caseService.selectCaseListInfoByCaseId(Long.parseLong((String)paramsMap.get("caseId")));
		CasePacs cp = caseService.selectCasePacsByCaseId(Long.parseLong((String)paramsMap.get("caseId")));
		CaseDig cd = caseService.selectCaseDigByCaseId(Long.parseLong((String)paramsMap.get("caseId")));
		//判断是否超时
		if("1".equals(cli.getStatus())){
			if(cli.getMissingTime() < new Date().getTime()){
				//设置已违约
				cli.setStatus("3");
				caseService.updateCaseListInfo(cli);
				//生成新的可抢case，放入db与cache
				cli.setId(null);
				cli.setStatus("0");
				cli.setDoctorId(null);
				caseService.addCaseListInfo(cli);
				cp.setCaseId(cli.getId());
				caseService.addCasePacs(cp);
				memcachedClient.set(ConstantsInterior.CASE_PACS + cli.getId(), 0, cli);
				resJson = ResultUtil.getResJson(0, "病例已逾期！", null);
				ResultUtil.wirteResult(response, resJson.toString());
				return;
			}
		}
		//包装返回
		JSONObject obj = new JSONObject();
		obj.put("cli", JSONObject.fromObject(cli));
		obj.put("cp", JSONObject.fromObject(cp));
		obj.put("cd", JSONObject.fromObject(cd));
		resJson = ResultUtil.getResJson(1, "成功", obj);
		ResultUtil.wirteResult(response, resJson.toString());
	}
	
	/**
	 * 提交诊断
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(method=RequestMethod.POST, value="commitDiag.htm")
	public void commitDiag(HttpServletRequest request, HttpServletResponse response, @ModelAttribute CaseDig cd) throws IOException{
		UserInfo uInfo = (UserInfo) request.getSession().getAttribute(ConstantsInterior.USER_INFO);
		JSONObject resJson = new JSONObject();
		//判断是否诊断
		CaseListInfo cli = caseService.selectCaseListInfoByCaseId(cd.getCaseId());
		if( !"1".equals(cli.getStatus())){
			resJson = ResultUtil.getResJson(0, "此病例已被诊断", null);
			ResultUtil.wirteResult(response, resJson.toString());
			return;
		}
		//存储
		Date date = new Date();
		cd.setDigDoctorId(uInfo.getUserCheckId());
		cd.setDigDoctorName(uInfo.getRealName());
		cd.setDigDate(date);
		cli.setStatus("5");
		cli.setDigDate(date);
		caseService.updateCaseListInfo(cli);
		caseService.addCaseDig(cd);
		//关闭倒计时线程
		CaseInvalidThread cit = (CaseInvalidThread)ThreadUtil.getThreadByName(ConstantsInterior.CASE_PACS + cli.getId());
		cit.setTimePass(true);
		//结果
		resJson = ResultUtil.getResJson(1, "诊断成功", null);
		ResultUtil.wirteResult(response, resJson.toString());
	}
}
