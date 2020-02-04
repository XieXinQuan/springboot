package com.huanting.quan.Enum;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/26
 */
public class Constant {
    public static Integer pageInitIndex = 1;
    public static Integer pageInitCount = 20;

    /**
     * 批量保存--500条
     */
    public static Integer batchSaveMessageSize = 5;


    /**
     * Redis消息保存 一分钟执行一次
     */
    public static Long messageScheduleTime = 60000L;
}
