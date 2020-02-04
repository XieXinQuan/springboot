package com.huanting.quan.Enum;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/30
 */
public enum FriendStatus implements BaseEnum{
    /**
     * 返回枚举
     */
    Normal(1, "正常"),
    WaitToAgree(2, "申请中"),
    Refuse(3, "拒绝");


    private Integer key;
    private String value;

    FriendStatus(Integer key, String value) {
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
