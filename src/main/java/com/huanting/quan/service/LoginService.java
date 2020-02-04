package com.huanting.quan.service;

import com.huanting.quan.Enum.ResultEnum;
import com.huanting.quan.entity.User;
import com.huanting.quan.exception.GlobalException;
import com.huanting.quan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/11
 */
@Service
public class LoginService extends BaseService{
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
    public User update(String name, String password){

        Object user = request.getSession().getAttribute("User");
        if(!(user instanceof User)) {
            throw new GlobalException(ResultEnum.CustomException.getKey(), "信息已过期,请重新验证.");
        }
        Long id = ((User) user).getId();

        User allById = userRepository.findAllById(id);
        allById.setLoginName(name);
        allById.setName(name);
        allById.setPassword(password);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        allById.setEncryptionPassword(bCryptPasswordEncoder.encode(password));

        userRepository.save(allById);


        return allById;
    }

    public User login(String name, String password){
        User user = userRepository.findAllByLoginName(name);
        if(user == null){
            throw new GlobalException(ResultEnum.UserIsNotExists.getKey());
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if(!password.equals(user.getPassword()) ||
                !encoder.matches(password, user.getEncryptionPassword())){
            throw new GlobalException(ResultEnum.PasswordIsError.getKey());
        }

        request.getSession().setAttribute("User", user);
        return user;
    }

    public void sendEmailVerificationCode(String loginEmail) throws UnsupportedEncodingException, MessagingException {

        String code = smsCode();

//        boolean isSendSuccess = sendEmail(loginEmail, "登录验证码", "您的验证码为:" + code, null);
//        if(!isSendSuccess) {
//            throw new GlobalException(ResultEnum.SystemException.getKey(), "发送验证码错误,请重试...");
//        }

        redisTemplate.opsForValue().set("code:"+loginEmail, code, 5, TimeUnit.MINUTES);
    }


    public boolean sendEmail(String loginEmail, String title, String content, List<File> files) throws MessagingException, UnsupportedEncodingException {
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

        return true;
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

    /**
     * 校验验证码
     */
    public static boolean checkVerificationCode(String emailAddress, String code) {

        return true;
    }

    /**
     * 提交注册--验证码校验通过
     */
    public boolean register(String email) throws UnsupportedEncodingException, MessagingException {
        if(userRepository.countByEmailAddress(email) > 0) {
            throw new GlobalException(ResultEnum.CustomException.getKey(), "该邮箱已被注册.");
        }

        User user = new User();
        user.setEmailAddress(email);

        userRepository.save(user);
        request.getSession().setAttribute("User", user);
        return true;
    }


    public void logout(){
        if(checkSessionOutDue()){
            return;
        }
        request.getSession().removeAttribute("User");
    }
    public static void main(String[] args) {





    }


}
