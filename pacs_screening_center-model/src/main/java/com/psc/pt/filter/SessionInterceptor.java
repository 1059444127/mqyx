package com.psc.pt.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.psc.pt.constants.ConstantsInterior;


/**
 * action之前的拦截
 * @author YQ
 *
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {
	private final static Logger LOG = Logger.getLogger(SessionInterceptor.class);
	/*被放过的请求list*/
	private List<String> passUrlList;
	public void setPassUrlList(List<String> passUrlList) {
		this.passUrlList = passUrlList;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException{
		String url = request.getRequestURI();
		LOG.info(url);
		for(String passUrl : passUrlList){
			if(passUrl.equals(url)){
				return true;
			}
		}
		if(null == request.getSession().getAttribute(ConstantsInterior.USER_INFO)){
			response.sendRedirect("/login/loginIndex.htm");
			return false;
		}
		 return true;
	}
}
