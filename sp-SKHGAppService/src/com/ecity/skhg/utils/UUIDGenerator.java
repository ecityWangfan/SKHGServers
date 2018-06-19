package com.ecity.skhg.utils;


import java.util.UUID;

/**
 * 
 * GUID 生成
 * @author DYLEE
 *
 */
public class UUIDGenerator {

	/**
	 * Creates a new instance of UUIDGenerator.
	 *
	 */
	public UUIDGenerator() {}

	/**
	 * getUUID:获得一个UUID. <br/>
	 *
	 * @author Tkiyer
	 * @return	String UUID
	 * @since JDK 1.6
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return s.substring(0, 8).concat(s.substring(9, 13)).concat(s.substring(14, 18))
				.concat(s.substring(19, 23)).concat( s.substring(24));
	}
	
	/**
	 * getUUID:获得指定数目的UUID. <br/>
	 *
	 * @author Tkiyer
	 * @param number	int 需要获得的UUID数量
	 * @return	String[] UUID数组
	 * @since JDK 1.6
	 */
	public static String[] getUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] ss = new String[number];
		for (int i = 0; i < number; i++) {
			ss[i] = getUUID();
		}
		return ss;
	}

}
