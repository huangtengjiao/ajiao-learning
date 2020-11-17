package com.yundasys.member.alipay.template.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期处理
 */
public class DateUtils {
	private final static Logger logger = LoggerFactory.getLogger(DateUtils.class);
	/**
	 * 时间格式(yyyy-MM-dd)
	 */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/**
	 * 时间格式(yyyy-MM-dd HH:mm:ss)
	 */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static String format(Date date) {
		return format(date, DATE_PATTERN);
	}

	public static String format(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return df.format(date);
		}
		return null;
	}

	/**
	 * 计算距离现在多久，非精确
	 *
	 * @param date
	 * @return
	 */
	public static String getTimeBefore(Date date) {
		Date now = new Date();
		long l = now.getTime() - date.getTime();
		long day = l / (24 * 60 * 60 * 1000);
		long hour = (l / (60 * 60 * 1000) - day * 24);
		long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		String r = "";
		if (day > 0) {
			r += day + "天";
		} else if (hour > 0) {
			r += hour + "小时";
		} else if (min > 0) {
			r += min + "分";
		} else if (s > 0) {
			r += s + "秒";
		}
		r += "前";
		return r;
	}

	/**
	 * 计算距离现在多久，精确
	 *
	 * @param date
	 * @return
	 */
	public static String getTimeBeforeAccurate(Date date) {
		Date now = new Date();
		long l = now.getTime() - date.getTime();
		long day = l / (24 * 60 * 60 * 1000);
		long hour = (l / (60 * 60 * 1000) - day * 24);
		long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		String r = "";
		if (day > 0) {
			r += day + "天";
		}
		if (hour > 0) {
			r += hour + "小时";
		}
		if (min > 0) {
			r += min + "分";
		}
		if (s > 0) {
			r += s + "秒";
		}
		r += "前";
		return r;
	}

	/**
	 * 输入目标年月的第几周，得到这周的起止日期
	 * 
	 * @param firstDayOfWeek
	 *            一周的起点，例如 Calendar.SUNDAY
	 * @param lastDayOfWeek
	 *            一周的终点，例如 Calendar.SATURDAY
	 * @param targetYear
	 *            目标年
	 * @param targetMonth
	 *            目标月
	 * @param targetWeek
	 *            目标周
	 * @return
	 */
	public static Calendar[] getTargetWeek(int firstDayOfWeek, int lastDayOfWeek, int targetYear, int targetMonth,
			int targetWeek) {
		// 格式化输入打印
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar[] result = new Calendar[2];

		Calendar calendar = new GregorianCalendar();
		// 设置周的起始点
		calendar.setFirstDayOfWeek(firstDayOfWeek);
		// 按参数设定年
		calendar.set(Calendar.YEAR, targetYear);
		// 按参数设定月
		calendar.set(Calendar.MONTH, targetMonth - 1);
		// 按参数设定周
		calendar.set(Calendar.WEEK_OF_MONTH, targetWeek);
		// 修改当前天
		calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);

		Calendar from = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DATE));
		result[0] = from;

		Calendar to = new GregorianCalendar(from.get(Calendar.YEAR), from.get(Calendar.MONTH), from.get(Calendar.DATE));
		// 下一周
		to.add(Calendar.WEEK_OF_YEAR, 1);
		// 回退一天
		to.add(Calendar.DATE, -1);
		result[1] = to;

		System.out.println(targetYear + "年" + targetMonth + "月第" + targetWeek + "周的起点是"
				+ sdf.format(result[0].getTime()) + "终点是" + sdf.format(result[1].getTime()));

		return result;
	}

	/**
	 * 指定日期是所在月份的第几周
	 * 
	 * @param firstDayOfWeek
	 *            一周的起始日
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static int whichWeek(int firstDayOfWeek, int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		// 设置周的起始点
		cal.setFirstDayOfWeek(firstDayOfWeek);
		cal.set(Calendar.YEAR, year); // year 为 int
		cal.set(Calendar.MONTH, month - 1);// 注意,Calendar对象默认一月为0
		cal.set(Calendar.DAY_OF_MONTH, day);

		int week = cal.get(Calendar.WEEK_OF_MONTH);
		// Calendar.DAY_OF_WEEK_IN_MONTH 某月中第几周,按这个月1号算,1号起就是第1周,8号起就是第2周
		// Calendar.WEEK_OF_MONTH 日历式的第几周
		return week;
	}

	// 得到指定日期上周的起始日
	public static Date getLastWeekBegin(Date date, int firstDayOfWeek) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.DAY_OF_MONTH, -7);
		return cal.getTime();
	}

	// 得到指定日期上周的结束日
	public static Date getLastWeekEnd(Date date, int firstDayOfWeek) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}

	// 以date为基准前后x天的date
	public static Date getDate(Date date, int value) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Timestamp(date.getTime()));
		if (value != 0) {
			calendar.add(Calendar.DAY_OF_MONTH, value);
		}
		calendar.get(Calendar.DAY_OF_MONTH);

		return new Date(calendar.getTimeInMillis());
	}

	/**
	 * 得到相对今天的前x天, x可正可负 正数是明天，负数是昨天
	 * 
	 * @param value
	 *            int 前x天
	 * @return Date
	 */
	public static Date getDate(int value) {
		return getDate(new Date(), value);
	}

	/**
	 * 将时间转换为int表现形式
	 * 
	 * @param time
	 * @return
	 */
	public static int convertDate2Int4Day(Date time) {
		return Integer.parseInt(formatDate(time, "yyyyMMdd"));
	}

	/**
	 * 将时间转换为int表现形式
	 * 
	 * @param time
	 * @return
	 */
	public static int convertDate2Int4Month(Date time) {
		return Integer.parseInt(formatDate(time, "yyyyMM"));
	}

	/**
	 * 将Date格式化显示为String
	 * 
	 * @param date
	 *            Date
	 * @param style
	 *            String 样式
	 * @return String
	 */
	public static String formatDate(Date date, String style) {
		if (style == null) {
			style = "yyyyMMdd";
		}
		if (date == null) {
			return "";
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(style);
		return simpleDateFormat.format(date);
	}

	public static void main(String[] args) {
		Date date = new Date();
		Date from = getLastWeekBegin(date, Calendar.MONDAY);
		Date to = getLastWeekEnd(date, Calendar.MONDAY);

		System.out.println("上周是从" + format(from) + "到" + format(to));

		// // 周起点
		// int firstDayOfWeek = Calendar.SUNDAY;
		// // 周终点
		// int lastDayOfWeek = Calendar.SATURDAY;
		//
		// int endMonth = 2019;
		//
		// // 以循环最近几年的情况来演示方法的调用
		// for (int i = 2015; i < endMonth; i++) {
		// // 目标年
		// int targetYear = i;
		//
		// for (int j = 0; j < 12; j++) {
		// // 目标月
		// int targetMonth = j + 1;
		//
		// for (int k = 1; k <= 5; k++) {
		// // 目标周
		// int targetWeek = k;
		//
		// getTargetWeek(firstDayOfWeek, lastDayOfWeek, targetYear, targetMonth,
		// targetWeek);
		// }
		// System.out.println();
		// }
		// System.out.println();
		// }
	}

	public static Date getMonthBegin(Date commitDate) {
		GregorianCalendar date = new GregorianCalendar();
		date.setTime(commitDate);
		date.set(Calendar.DAY_OF_MONTH, 1);
		date.get(Calendar.DAY_OF_MONTH);
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.get(Calendar.HOUR_OF_DAY);
		date.set(Calendar.MINUTE, 0);
		date.get(Calendar.MINUTE);
		date.set(Calendar.SECOND, 0);
		date.get(Calendar.SECOND);

		return new Date(date.getTimeInMillis());
	}

	public static Date getMonthEnd(Date commitDate) {
		GregorianCalendar date = new GregorianCalendar();
		date.setTime(commitDate);
		date.set(Calendar.DAY_OF_MONTH, date.getActualMaximum(Calendar.DAY_OF_MONTH));
		date.get(Calendar.DAY_OF_MONTH);
		date.set(Calendar.HOUR_OF_DAY, 23);
		date.get(Calendar.HOUR_OF_DAY);
		date.set(Calendar.MINUTE, 59);
		date.get(Calendar.MINUTE);
		date.set(Calendar.SECOND, 59);
		date.get(Calendar.SECOND);
		return new Date(date.getTimeInMillis());
	}

	/**
	 * 功能描述：常用的格式化日期
	 * 
	 * @param date
	 *            Date 日期
	 * @return String 日期字符串 yyyy-MM-dd格式
	 */
	public static String formatDate(Date date) {
		return formatDateByFormat(date, "yyyy-MM-dd");
	}

	/**
	 * 功能描述：以指定的格式来格式化日期
	 * 
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return String 日期字符串
	 */
	public static String formatDateByFormat(Date date, String format) {
		String result = "";
		if (date != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				result = sdf.format(date);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 功能描述：格式化日期
	 * 
	 * @param dateStr
	 *            String 字符型日期：YYYY-MM-DD 格式
	 * @return Date
	 */
	public static Date parseDate(String dateStr) {
		return parseDate(dateStr, "yyyy-MM-dd");
	}

	public static String getTimeStamp(Timestamp ts) {
		String format = "yyyy-MM-dd hh:mm:ss";
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.format(ts);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 功能描述：格式化日期
	 * 
	 * @param dateStr
	 *            String 字符型日期
	 * @param format
	 *            String 格式
	 * @return Date 日期
	 */
	public static Date parseDate(String dateStr, String format) {
		Date date = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			String dt = dateStr.replaceAll("/", "-");
			if ((!dt.equals("")) && (dt.length() < format.length())) {
				dt += format.substring(dt.length()).replaceAll("[YyMmDdHhSs]", "0");
			}
			date = (Date) dateFormat.parse(dt);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * 指定日期的开始时间
	 * 
	 * @param date
	 *            Date
	 * @return Date
	 */
	@SuppressWarnings("deprecation")
	public static Date getDateBegin(Date date) {
		if (date == null) {
			return null;
		}
		Date result = (Date) date.clone();
		result.setHours(0);
		result.setMinutes(0);
		result.setSeconds(0);
		return result;
	}

	public static Date getDateBegin(GregorianCalendar date) {
		if (date == null) {
			return null;
		}
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.get(Calendar.HOUR_OF_DAY);
		date.set(Calendar.MINUTE, 0);
		date.get(Calendar.MINUTE);
		date.set(Calendar.SECOND, 0);
		date.get(Calendar.SECOND);
		return date.getTime();
	}

	/**
	 * 指定日期的最后时间
	 * 
	 * @param date
	 *            Date
	 * @return Date
	 */
	public static Date getDateEnd(Date date) {
		GregorianCalendar toCalendar = new GregorianCalendar();
		toCalendar.setTime(date);
		return getDateEnd(toCalendar);
	}

	/**
	 * 指定日期的最后时间
	 * 
	 * @param date
	 *            Date
	 * @return Date
	 */
	public static Date getDateEnd(GregorianCalendar date) {
		if (date == null) {
			return null;
		}
		date.set(Calendar.HOUR_OF_DAY, 23);
		date.get(Calendar.HOUR_OF_DAY);
		date.set(Calendar.MINUTE, 59);
		date.get(Calendar.MINUTE);
		date.set(Calendar.SECOND, 59);
		date.get(Calendar.SECOND);
		return date.getTime();
	}
	
	public static Date getMonth(int dt) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		if (dt != 0) {
			gregorianCalendar.add(Calendar.MONTH, dt);// 减一为上月最后一天
		}
		return new Date(gregorianCalendar.getTimeInMillis());
	}
}
