package com.psc.pt.controller.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/temp")
public class tempController {
	@RequestMapping(method=RequestMethod.GET, value="temphos.htm")
	public String temphos(HttpServletRequest request, HttpServletResponse response, ModelMap map){
		return "/admin/screen/temp/temphos";
	}
	@RequestMapping(method=RequestMethod.GET, value="tempdoc.htm")
	public String tempdoc(HttpServletRequest request, HttpServletResponse response, ModelMap map){
		return "/admin/screen/temp/tempdoc";
	}
}
