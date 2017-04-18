package com.psc.pt.controller.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.psc.pt.constants.ConstantsInterior;
import com.psc.pt.controller.code.CodeController;
import com.psc.pt.model.user.UserCheck;
import com.psc.pt.model.user.UserGroup;
import com.psc.pt.model.user.UserInfo;
import com.psc.pt.service.user.UserCheckService;
import com.psc.pt.service.user.UserGroupService;
import com.psc.pt.service.user.UserInfoService;
import com.psc.pt.util.MD5SaltUtil;
import com.psc.pt.util.ParamsUtil;
import com.psc.pt.util.ResultUtil;

@Controller
@RequestMapping(value="/login")
/**
 * 登录类
 * @author YQ
 *
 */
public class LoginController {
	private static final Logger LOG = Logger.getLogger(LoginController.class);
	@Autowired UserCheckService userCheckService;
	@Autowired UserGroupService userGroupService;
	@Autowired UserInfoService userInfoService;
	/**
	 * 登录页
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET, value="/loginIndex.htm")
	public String loginIndex(HttpServletRequest request, HttpServletResponse response, ModelMap map){
		/*清除session*/
		request.getSession().invalidate();
		//dosomething
		return "/admin/screen/login/loginIndex";
//		return "/admin/screen/case/caseList";
	}
	/**
	 * 用户验证
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(method=RequestMethod.GET, value="/checkPassword.htm")
	public void checkPassword(HttpServletRequest request, HttpServletResponse response, @ModelAttribute UserCheck userCheck) throws IOException{
		JSONObject resJson = new JSONObject();
		String[] paramsList = {"authCode"};
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		ParamsUtil.doParams(paramsList, request, paramsMap);
		//验证码验证
		String authCode = (String) request.getSession().getAttribute(ConstantsInterior.AUTHCODE);
		if(!((String)paramsMap.get("authCode")).equalsIgnoreCase(authCode)){
			resJson = ResultUtil.getResJson(0, "登录失败，验证码输入错误！", null);
			ResultUtil.wirteResult(response, resJson.toString());
			return;
		}
		//用户验证
		UserCheck ucTr = userCheckService.selectByUserName(userCheck.getUserName());
		if(null == ucTr){
			resJson = ResultUtil.getResJson(-1, "登录失败，用户名不存在！", null);
		}else {
			String salt = ucTr.getSalt();
			String password = MD5SaltUtil.getMSH(salt, userCheck.getPassword());
			if(ucTr.getPassword().equals(password)){
				request.getSession().setAttribute(ConstantsInterior.USER_CHECK, ucTr);
				//组
				UserGroup ug = userGroupService.selectById(ucTr.getGroupId());
				request.getSession().setAttribute(ConstantsInterior.USER_GROUP, ug);
				//用户信息
				UserInfo ui = userInfoService.selectByCheckId(ucTr.getId());
				request.getSession().setAttribute(ConstantsInterior.USER_INFO, ui);
				JSONObject obj = new JSONObject();
				obj.put("nexturl", ug.getMainSrc());
				resJson = ResultUtil.getResJson(1, "登录成功！", obj);
			}else {
				resJson = ResultUtil.getResJson(-2, "登录失败，密码错误！", null);
			}
		}
		ResultUtil.wirteResult(response, resJson.toString());
	}
	
	/**
	 * 已登录页
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET, value="/alreadyLogin.htm")
	public String alreadyLogin(HttpServletRequest request, HttpServletResponse response, ModelMap map){
		UserInfo uInfo = (UserInfo) request.getSession().getAttribute(ConstantsInterior.USER_INFO);
		UserGroup ug = (UserGroup) request.getSession().getAttribute(ConstantsInterior.USER_GROUP);
		UserCheck uc = (UserCheck) request.getSession().getAttribute(ConstantsInterior.USER_CHECK);
		//dosomething
		map.put("ug", ug);
		map.put("ui", uInfo);
		map.put("uc", uc);
		return "/admin/screen/login/alreadyLogin";
	}
}
