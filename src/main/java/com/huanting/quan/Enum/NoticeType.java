package com.huanting.quan.Enum;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/2/12
 */
public enum NoticeType implements BaseEnum{

    /**
     * 消息类型
     */
    FriendApplication(1, "好友申请"),
    NeedApprove(2, "需要审批");

    private Integer key;
    private String value;

    NoticeType(Integer key, String value) {
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
