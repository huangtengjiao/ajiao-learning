package com.yundasys.member.alipay.template.enums;


/**
 * 线上数据库缓存操作类型枚举
 * @Description: TODO  
 * @author htj 
 * @date 2020年1月5日
 */
public enum OperateTypeEnum {
	search("search"),
	update("update"),
	delete("delete"),
    insert("insert");

    private String operate;

    private OperateTypeEnum(String operate) {
        this.operate = operate;
    }

    // 显示枚举中的有效operate，用于validta中显示
    public static String showOperates() {
        StringBuilder stb = new StringBuilder();
        for (OperateTypeEnum p : OperateTypeEnum.values()) {
            if (stb.length() > 0) {
                stb.append(",");
            }
            stb.append(p.getOperate());
        }
        return stb.toString();
    }

    // 是否包含xxoperate
    public static boolean hitOperate(String operate) {
        for (OperateTypeEnum p : OperateTypeEnum.values()) {
            if (p.getOperate().equals(operate)) {
                return true;
            }
        }
        return false;
    }
    
    
	// 获取枚举
	public static OperateTypeEnum getEnum(String operate) {
		for (OperateTypeEnum p : OperateTypeEnum.values()) {
			if (p.getOperate().equals(operate)) {
				return p;
			}
		}
		return null;
	}

    public String getOperate() {
        return operate;
    }


    public void setOperate(String operate) {
        this.operate = operate;
    }

}
