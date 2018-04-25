package com.ecity.skhg.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

/**
 * base64数据解析
 * @author dylee
 *
 */
public class Base64Utils {
	
	/**
	 * 编码
	 * @param str
	 * @return
	 */
	public static String encode(String str){
		Base64 base64 = new Base64();  
        byte[] debytes = base64.encodeBase64(new String(str).getBytes());  
        return new String(debytes);
	}
	
	/**
	 * 解码
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String decode(String str) throws UnsupportedEncodingException  
    {  
        Base64 base64 = new Base64();  
        byte[] debytes = base64.decodeBase64(new String(str).getBytes());  
        return new String(debytes,"utf-8");
    }
	
	/**
	 * 判断是否是base64编码
	 * @param str
	 * @return
	 */
	public static boolean isBase64(String str){
        return Base64.isBase64(new String(str).getBytes());
	}
}
