package com.psc.pt.controller.pacs;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.psc.pt.constants.ConstantsInterior;
import com.psc.pt.model.pacs.CaseListInfo;
import com.psc.pt.model.pacs.CasePacs;
import com.psc.pt.service.pacs.CaseService;
import com.psc.pt.util.ParamsUtil;
import com.psc.pt.util.ResultUtil;

@Controller
@RequestMapping(value="/caseapi")
public class CaseApiController {
	@Autowired private MemcachedClient memcachedClient;
	@Autowired CaseService caseService;
	/**
	 * 可抢单
	 * @param request
	 * @param response
	 * @param map
	 * @throws IOException 
	 * @throws MemcachedException 
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 */
	@RequestMapping(method=RequestMethod.GET, value="caseIn.htm")
	public void caseIn(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws IOException, TimeoutException, InterruptedException, MemcachedException{
		JSONObject resJson = new JSONObject();
		String[] paramsList = {"xxxxId"};
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		ParamsUtil.doParams(paramsList, request, paramsMap);
		//==========================================================================
		CaseListInfo cli = new CaseListInfo();
		cli.setStatus("0");
		int number = new Random().nextInt(4) + 1;
		switch (number) {
		case 1:
			cli.setPatientName("王小明");
			cli.setPatientSex("M");
			cli.setPatientAge(32);
			cli.setVisitType("门诊");
			cli.setPatientPhone("13000000000");
			cli.setPatientNo("PN10010");
			cli.setCheckNo("CK10010");
			cli.setCaseNo("CN10010");
			cli.setItem("头部正扫");
			cli.setModality("MR");
			cli.setSendDoctorName("李三光");
			cli.setSendHospitalName("影像中心");
			cli.setSendDate(new Date());
			cli.setInid(1L);
			break;
		case 2:
			cli.setPatientName("张大宇");
			cli.setPatientSex("M");
			cli.setPatientAge(23);
			cli.setVisitType("门诊");
			cli.setPatientPhone("13000000000");
			cli.setPatientNo("PN10086");
			cli.setCheckNo("CK10086");
			cli.setCaseNo("CN10086");
			cli.setItem("胸部平扫");
			cli.setModality("CT");
			cli.setSendDoctorName("李三光");
			cli.setSendHospitalName("影像中心");
			cli.setSendDate(new Date());
			cli.setInid(2L);
			break;
		case 3:
			cli.setPatientName("葛澄澄");
			cli.setPatientSex("F");
			cli.setPatientAge(23);
			cli.setVisitType("门诊");
			cli.setPatientPhone("13000000000");
			cli.setPatientNo("PN19527");
			cli.setCheckNo("CK19527");
			cli.setCaseNo("CN19527");
			cli.setItem("胸部平扫");
			cli.setModality("CT");
			cli.setSendDoctorName("李三光");
			cli.setSendHospitalName("影像中心");
			cli.setSendDate(new Date());
			cli.setInid(3L);
			break;
		default:
			cli.setPatientName("齐豫");
			cli.setPatientSex("M");
			cli.setPatientAge(26);
			cli.setVisitType("门诊");
			cli.setPatientPhone("13000000000");
			cli.setPatientNo("PN1AE86");
			cli.setCheckNo("CK1AE86");
			cli.setCaseNo("CN1AE86");
			cli.setItem("胸部正扫");
			cli.setModality("CT");
			cli.setSendDoctorName("李三光");
			cli.setSendHospitalName("影像中心");
			cli.setSendDate(new Date());
			cli.setInid(4L);
			break;
		}
		
		
		//dosomething
		caseService.addCaseListInfo(cli);
		
		memcachedClient.set(ConstantsInterior.CASE_PACS + cli.getId(), 0, cli);
		
		CasePacs cp = new CasePacs();
		cp.setCaseId(cli.getId());
		cp.setItem("头部正扫");
		cp.setModality("CT");
		cp.setReadUrl("http://118.178.186.253:8888/Gallery/Index/DJ20160621A0075?hospId=470890343&nsukey=1Un98IXKrCaaTPYJMnCPm2rXpEbtQBozgqBV7HminzteQto5RRR5U%2FucR%2F8C0pCrBz3FDvXeXTleDuI%2B5uNQOVumQg5OjRRFiR%2BdeVo64AbW5CAF6eWMZXDqNydmGfb8swY4WMMYYBKcBMtmEoqOJUKEklMHdrflIIIrscoTiVb108t8P%2BIhpk7UnaAzEXow&from=singlemessage&isappinstalled=0");
		cp.setImgUrl("xxx");
		caseService.addCasePacs(cp);
		//dosomething
		//==========================================================================
		resJson = ResultUtil.getResJson(1, "病例导入成功！", null);
		ResultUtil.wirteResult(response, resJson.toString());
	}
}
