package com.huanting.quan.Enum;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/2/14
 */
public enum  CommonEnum implements BaseEnum {

    /**
     * 公共
     */
    Normal(1, "正常"),
    Refuse(2, "拒绝");

    private Integer key;
    private String value;

    CommonEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 获取值
     * @return
     */
    @Override
    public Integer getKey() {
        return key;
    }

    /**
     * 获取解释
     * @return
     */
    @Override
    public String getValue() {
        return value;
    }
}
