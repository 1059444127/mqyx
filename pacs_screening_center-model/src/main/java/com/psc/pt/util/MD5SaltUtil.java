package com.psc.pt.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 加密类
 * @author YQ
 *
 */
public class MD5SaltUtil {
	public static String getMSH(String salt, String msg){
		int saltLen = salt.length();
		String md5Str = DigestUtils.md5Hex(salt.substring(0, saltLen/2) + msg + salt.substring(saltLen/2));
		System.out.println(md5Str.length());
		return md5Str;
	}
}
