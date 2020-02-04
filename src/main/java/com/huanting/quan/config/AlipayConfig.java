package com.huanting.quan.config;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/29
 */

public class AlipayConfig {
    //发起请求的应用ID。沙箱与线上不同，请更换代码中配置;
    public static String app_id ="2016102000727127";
    //支付宝私匙
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCdibllK1/tHg9HA3LvDx2KmhM1MTRa1+stxBtfqq1beMs7i3U3ozms9Ena6UFd9KcIDzE8y7DfJnBs2jHdKSCEDP51JrwQQDhlqdFqZ8XiI3BlveQgWzfIAX09VvdXJAP/C/0NeqHhBXfzG5AUIJYjpajtv8TLzgLLQRZ0Z4f0C62oXPFXjE08FKNbsAvIbsKgC9JKHH17UfQnNrtALHJuqPxCriIvDS4ctBDluWCcsS5iMdveFfleIwGz3RyjrwLmHbjb/8z/gNiq7rX74wC9WoDDk0snGgdBLmLOtlyGRPPFb1beKYHEJPsSTJkuJeU6BzSN1LxVloRqfXCXhPV9AgMBAAECggEATxvv+3vSifIEXfa/aoi+nvFMH/P7hHrU4ICP9nRw5XQ9KkOXsz7GIbHyvHlRl+rZTdTDNvJaeVSGNENFsveKGoWj6hRvcAGv0G18eeBuoGaHdK/stMrZshf9WiYnwpyVXSgaBQDF9YeXen8m1NSgUHpJ4Eniey2SfZrLudybitLgTBgt6d8V/+pct+VQISaudKkIG6QNwlGG7a1xIOXZZZAQefmncBdnMukOoskFoyy61qDAMOGfUqbkOqrYbAFRzN6KAyZAsWHQRsHWA/bbgBaPf7E0qKn4YiJGwrzx0sfs2RLwbThcYVhmUbQyjmukDo1jVM8WFG2AH4eqTGujCQKBgQDXXEHBwQazMeFfYumGoRbWfkL/X6CB5Qtg39H6LH8UR15d0/peOCXaFjWGkqy5Wc5RlVnFkNMFhNtGQxmTYfEx8ZJp7M7oOwHRFJkmL2mIvtSd/o5TqCM2nCfGwLv2YPv980mzIqtccT0HUgtcLdmpqajNblUuSd2KLbMrVJSe9wKBgQC7RCcOZzwP4Xe6wHzmxh4o/5PMNJV0h1yMaJsrGWEWME2y7+Az10y0+k+LXjT1s3k59sZkS3lPI3UDccRGQl2kCf7VAxJPA1xG0wn+FoWTofhwg0SAT3JFlxTCkTljJSmZWrqLigN2Ob5OnzzVXgfad7LhozCf1PaQz6+7DBlOKwKBgAvmIAf5h0o9Vktz5p02p1FglokPmomywwcuNqbQ8JCJRPCANtQf3A+JrJlr3zk6egjN7LDayJ4Eci0WpgIbpjzxMPrDR8dTA02vCqsucXFJweuc1fZPUurAg/4aRghd4W5tk8PfBYLNCuctpE0Kh301T4tSLkipciEhV0XsnW2PAoGBAIWO7OAL5r1YzACoiiw4/oe1CB8rHbC3Xd1SOeC6QBEIMFI/XML8edvVbmfyr4jRENOzWflsDr/PhQQI3rhRWuHEszgO8b7ioaxtlYHfZDNP4JW2OWPdxZ02THscAjbg6VNTbxpYO3HVaxI95bAzWRyKJE1jCynjRFsxlJ6t2VpzAoGAL1gczJG4judMCEJOMjDDl1dMwKDYzbVEPy73QLyYrgcxH1O8gVqhVqnP4xn0bCMltrhJD7WSW2IAqaIXPQbLC3Ujw2NkqAJ2B37ncfdrYmIG85Nsdv4JdMmFiGs9pUPFLHauIeEI/UP8cPkri5JntWp0DZUDgFxEsIG6DcfeliA=";
    //支付宝公匙
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnYm5ZStf7R4PRwNy7w8dipoTNTE0WtfrLcQbX6qtW3jLO4t1N6M5rPRJ2ulBXfSnCA8xPMuw3yZwbNox3SkghAz+dSa8EEA4ZanRamfF4iNwZb3kIFs3yAF9PVb3VyQD/wv9DXqh4QV38xuQFCCWI6Wo7b/Ey84Cy0EWdGeH9AutqFzxV4xNPBSjW7ALyG7CoAvSShx9e1H0Jza7QCxybqj8Qq4iLw0uHLQQ5blgnLEuYjHb3hX5XiMBs90co68C5h242//M/4DYqu61++MAvVqAw5NLJxoHQS5izrZchkTzxW9W3imBxCT7EkyZLiXlOgc0jdS8VZaEan1wl4T1fQIDAQAB";
    //服务器异步通知路径
    public static String notify_url = "http://118.89.133.157:8080/alipay/alipayNotifyNotice";
    //服务器同步通知路径
    public static String return_url = "http://118.89.133.157:8080/alipay/alipayReturnNotice";
    //公匙类型/签名类型
    public static String sign_type = "RSA2";
    //编码格式
    public static String charset = "utf-8";
    //向支付宝发起请求的网关。沙箱与线上不同，请更换代码中配置;沙箱:https://openapi.alipaydev.com/gateway.do上线https://openapi.alipay.com/gateway.do
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public static String FORMAT = "JSON";
}