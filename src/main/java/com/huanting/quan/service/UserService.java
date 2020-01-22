package com.huanting.quan.service;

import com.alibaba.fastjson.JSONObject;
import com.huanting.quan.Enum.ResultEnum;
import com.huanting.quan.entity.User;
import com.huanting.quan.exception.GlobalException;
import com.huanting.quan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/11
 */
@Service
public class UserService {
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender mailSender;

    private static final String AUTH_TOKEN = "56746fd40433a755a78c98ff2b3376d7";
    private static final String ACCOUNT_SID = "aa4c7065f7cd327da7cecc85afad3969";
    private static final String  QUERY_PATH = "https://openapi.miaodiyun.com/distributor/sendSMS";


    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    UserRepository userRepository;

    public Boolean add(String name, String password){
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        userRepository.save(user);

        return true;
    }
    public Boolean update(String name, String password){
        User allByNameLike = userRepository.findAllById(1L);
        if(allByNameLike != null){
            password = password == null? ""+(int)(Math.random()*10000) : password;
            allByNameLike.setPassword(password);
            userRepository.save(allByNameLike);
        }

        return true;
    }

    public boolean login(String name, String password){
        User user = userRepository.findByName(name);
        if(user == null){
            throw new GlobalException(ResultEnum.UserIsNotExists.getKey());
        }
        if(!password.equals(user.getPassword())){
            throw new GlobalException(ResultEnum.PasswordIsError.getKey());
        }
        return true;
    }

    public boolean sendEmail(String emailAddress, String title, String content){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(emailAddress);
        mailMessage.setSubject(title);
        mailMessage.setText(content);
        mailSender.send(mailMessage);
        return true;
    }
    public boolean sendEmailVerificationCode(String loginEmail) throws UnsupportedEncodingException, MessagingException {

        return sendEmailVerificationCode(loginEmail, "登录验证码", "您的验证码为:" + smsCode(), null);
    }
    public boolean sendEmailVerificationCode(String loginEmail, String title, String content, List<File> files) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(loginEmail);
        helper.setSubject(title);
        helper.setText(content);
        if(files != null) {
            String fileName = null;
            for (File file : files) {
                fileName = MimeUtility.encodeText(file.getName(), "GB2312", "B");
                helper.addAttachment(fileName, file);
            }
        }
        mailSender.send(message);

        redisTemplate.opsForValue().set(loginEmail, content);
        return true;
    }

    /**
     根据相应的手机号发送验证码
     *
      */
    public static String getCode(String phone) {
        String rod = smsCode();
        String timestamp = getTimestamp();
        String sig = getMD5(ACCOUNT_SID, AUTH_TOKEN, timestamp);
        String tamp = "【幻听】尊敬的用户，您好，您的验证码为：" + rod + "，若非本人操作，请忽略此短信。";
        OutputStreamWriter out = null;
        BufferedReader br = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(QUERY_PATH);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            // 设置是否允许数据写入
            connection.setDoInput(true);
            // 设置是否允许参数数据输出
            connection.setDoOutput(true);
            // 设置链接响应时间
            connection.setConnectTimeout(5000);
            // 设置参数读取时间
            connection.setReadTimeout(10000);
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            // 提交请求
            out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            String args = getQueryArgs(ACCOUNT_SID, tamp, phone, timestamp, sig, "JSON");
            out.write(args);
            out.flush();
            // 读取返回参数

            br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String temp = "";
            while ((temp = br.readLine()) != null) {
                result.append(temp);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        JSONObject json = JSONObject.parseObject(result.toString());
        String respCode = json.getString("respCode");
        String defaultRespCode = "00000";
        if (defaultRespCode.equals(respCode)) {
            return rod;
        } else {
            return defaultRespCode;
        }
    }

    /**
     定义一个请求参数拼接方法
     */
    public static String getQueryArgs(String accountSid, String smsContent, String to, String timestamp, String sig,
                                      String respDataType) {
        return "accountSid=" + accountSid + "&smsContent=" + smsContent + "&to=" + to + "&"+"timestamp=" + timestamp
                + "&sig=" + sig + "&respDataType=" + respDataType;
    }

    /**
     *
     获取时间戳
      */
    public static String getTimestamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    /**
     *
     sing签名
      */
    public static String getMD5(String sid, String token, String timestamp) {

        StringBuilder result = new StringBuilder();
        String source = sid + token + timestamp;
        // 获取某个类的实例
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // 要进行加密的东西
            byte[] bytes = digest.digest(source.getBytes());
            for (byte b : bytes) {
                String hex = Integer.toHexString(b & 0xff);
                if (hex.length() == 1) {
                    result.append("0" + hex);
                } else {
                    result.append(hex);
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    /**
     创建验证码
     */
    public static String smsCode() {
        String code = null;
        do{
            code = code == null ?  (int)((Math.random() * 9 + 1) * 100000) + "" : code;
        }while (code.length() != 6);
        return code;
    }


    public static void main(String[] args) {
        String phone = "17526717235";
        getCode(phone);
    }
}
