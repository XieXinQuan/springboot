package com.huanting.quan.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/2/9
 */
public class DateUtil {
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
    private static String timeDefaultFormat = "yyyy-MM-dd HH:mm:ss";
    private static String dateDefaultFormat = "yyyy-MM-dd";

    public static String dateFormat(Date date) throws Exception {
        if (!checkPattern(date)){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateDefaultFormat);
        return simpleDateFormat.format(date);
    }

    public static String dateTimeFormat(Date date) throws Exception {
        if (!checkPattern(date)){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeDefaultFormat);
        return simpleDateFormat.format(date);

    }


    private static boolean checkPattern(Date date) throws Exception {
        if (date == null){
            logger.info("Pattern date Is Not Null.");
            throw new Exception("Pattern date Is Not Null.");
        }
        return true;
    }
}
