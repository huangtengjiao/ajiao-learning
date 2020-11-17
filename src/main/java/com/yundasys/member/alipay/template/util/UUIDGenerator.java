package com.yundasys.member.alipay.template.util;

import java.util.UUID;

public class UUIDGenerator {

	public UUIDGenerator() {
	}

	/**
	 * 获得一个UUID
	 *
	 * @return String UUID
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18)
				+ s.substring(19, 23) + s.substring(24);
	}

	/**
	 * 获得指定数目的UUID
	 *
	 * @param number
	 *            int 需要获得的UUID数量
	 * @return String[] UUID数组
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

	/***
	 * 获得订单号
	 * 生成规则：//年月日 + 当天时间的毫秒 + 4位随机数 + 手机后四位数字
	 * 			//130530 + 30921687 + 1010 + 0676
	 * 			//年月日 + 当天时间的毫秒 + 手机号码
	 *          当前毫秒数 + 手机后四位
	 * @return
	 */
	public static String getOrderId(String mobile) {
		//SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
		//Calendar cal = Calendar.getInstance();
		//String date = format.format(cal.getTime());
		//long dayMillis = cal.get(Calendar.HOUR) * 3600000 + cal.get(Calendar.MINUTE) * 60000 +
		//				 cal.get(Calendar.SECOND) * 1000 + cal.get(Calendar.MILLISECOND);
		//return date + dayMillis + RandomStringUtils.randomNumeric(4) + mobile.substring(7, 11);
		//return date + dayMillis + mobile;
		mobile = mobile.replaceAll("[^0-9]", "");
		if(mobile.length() < 4){
			mobile = "0000" + mobile;
		}
		return System.currentTimeMillis() + mobile.substring(mobile.length()-4);
	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		System.out.println(getUUID());
		System.out.println(System.currentTimeMillis());
		System.out.println(getUUID());
		System.out.println(getUUID());
		System.out.println(getUUID());
		System.out.println(getUUID());
		System.out.println(getUUID());
		System.out.println(getOrderId("4--111"));
	}
}
