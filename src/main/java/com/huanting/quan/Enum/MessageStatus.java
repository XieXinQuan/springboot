package com.huanting.quan.Enum;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/28
 */
public enum MessageStatus implements BaseEnum{

    /**
     * 消息状态
     */
    SendSuccess(1, "Send"),
    OfflineMessage(2, "离线消息"),
    WithDraw(3, "撤回"),
    Remind(4, "提醒"),
    Favorite(5, "收藏");


    Integer key;
    String value;

    MessageStatus(Integer key, String value){
        this.key = key;
        this.value = value;
    }

    @Override
    public Integer getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

}
