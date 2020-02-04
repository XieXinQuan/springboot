package com.huanting.quan.util;

import com.huanting.quan.Enum.BaseEnum;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/30
 */
public class EnumUtil {
    public static boolean isContain(BaseEnum[] baseEnum, Integer value){
        for(BaseEnum baseEnum1 : baseEnum){
            if(baseEnum1.getKey().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
