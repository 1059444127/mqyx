package com.psc.pt.controller.user;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.psc.pt.controller.login.LoginController;
import com.psc.pt.model.user.UserCheck;
import com.psc.pt.service.user.UserCheckService;
import com.psc.pt.util.MD5SaltUtil;
import com.psc.pt.util.ParamsUtil;
import com.psc.pt.util.ResultUtil;

@Controller
@RequestMapping(value="/user")
/**
 * 用户类
 * @author YQ
 *
 */
public class UserController {
	private static final Logger LOG = Logger.getLogger(UserController.class);
	@Autowired UserCheckService userCheckService;
	/**
	 * 用户注册
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(method=RequestMethod.GET, value="/userRegistration.htm")
	public void userRegistration(HttpServletRequest request, HttpServletResponse response, @ModelAttribute UserCheck userCheck) throws IOException{
		JSONObject resJson = new JSONObject();
		//验证用户名重复性
		if(checkUserName(userCheck.getUserName())){
			//生成salt
			Random random = new Random();
			byte[] saltByte = new byte[18];
			random.nextBytes(saltByte);
			String salt = new Base64().encodeToString(saltByte);
			String md5Salt = MD5SaltUtil.getMSH(salt, userCheck.getPassword());
			//对象入库
			userCheck.setSalt(salt);
			userCheck.setPassword(md5Salt);
			userCheckService.insertUserCheck(userCheck);
			resJson = ResultUtil.getResJson(1, "注册成功！", null);
		}else {
			resJson = ResultUtil.getResJson(-1, "注册失败，用户名重复！", null);
		}
		ResultUtil.wirteResult(response, resJson.toString());
	}
	/**
	 * 用户名检测
	 * @param request
	 * @param response
	 * @param userName
	 * @throws IOException 
	 */
	@RequestMapping(method=RequestMethod.GET, value="/checkUserName.htm")
	public void checkUserName(HttpServletRequest request, HttpServletResponse response) throws IOException{
		JSONObject resJson = new JSONObject();
		String[] paramsList = {"userName"};
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		ParamsUtil.doParams(paramsList, request, paramsMap);
		Boolean isPass = checkUserName((String)paramsMap.get("userName"));
		if(isPass){
			resJson = ResultUtil.getResJson(1, "该用户名可用", null);
		}else {
			resJson = ResultUtil.getResJson(0, "该用户名已被注册！", null);
		}
		ResultUtil.wirteResult(response, resJson.toString());
	}
	/**
	 * 用户名检测function
	 * @param userName
	 * @return
	 */
	private Boolean checkUserName(String userName) {
		boolean res = false;
		UserCheck userCheck = userCheckService.selectByUserName(userName);
		if(null == userCheck){
			res = true;
		}
		return res;
	}
	/**
	 * 修改用户密码
	 * @param request
	 * @param response
	 * @param userCheck
	 * @throws IOException 
	 */
	@RequestMapping(method=RequestMethod.GET, value="/editUserPassword.htm")
	public void editUserPassword(HttpServletRequest request, HttpServletResponse response, @ModelAttribute UserCheck userCheck) throws IOException{
		JSONObject resJson = new JSONObject();
		//新密码
		String newPassword = new String(request.getParameter("newPassword").getBytes("iso-8859-1"), "UTF-8");
		//用户验证
		UserCheck ucTr = userCheckService.selectByUserName(userCheck.getUserName());
		if(null == ucTr){
			resJson = ResultUtil.getResJson(-1, "密码修改失败，用户名不存在！", null);
		}else {
			String salt = ucTr.getSalt();
			String password = MD5SaltUtil.getMSH(salt, userCheck.getPassword());
			if(ucTr.getPassword().equals(password)){
				//生成salt
				Random random = new Random();
				byte[] saltByte = new byte[18];
				random.nextBytes(saltByte);
				String newSalt = new Base64().encodeToString(saltByte);
				String md5Salt = MD5SaltUtil.getMSH(newSalt, newPassword);
				//对象入库
				ucTr.setSalt(newSalt);
				ucTr.setPassword(md5Salt);
				userCheckService.updateUserCheck(ucTr);
				resJson = ResultUtil.getResJson(1, "密码修改成功", null);
			}else {
				resJson = ResultUtil.getResJson(-2, "密码修改失败，原密码错误！", null);
			}
		}
		ResultUtil.wirteResult(response, resJson.toString());
	}
	/**
	 * 修改用户信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(method=RequestMethod.POST, value="/editUserInfo.htm")
	public void editUserInfo(HttpServletRequest request, HttpServletResponse response){
		
	}
}
