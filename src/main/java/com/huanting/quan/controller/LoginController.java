package com.huanting.quan.controller;

import com.huanting.quan.Enum.ResultEnum;
import com.huanting.quan.entity.User;
import com.huanting.quan.service.LoginService;
import com.huanting.quan.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/11
 */
@Api(tags = "用户登录接口")
@Validated
@RestController
@RequestMapping("/login")
public class LoginController {
    @GetMapping("/")
    public String welcome(){
        return "Hello World.";
    }
    @Resource
    LoginService loginService;

    @Resource
    private RedisTemplate redisTemplate;


    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/login")
    @ApiOperation("登录")
    public Object login(@Valid @NotBlank(message = "账号不能空")  @RequestParam("loginName") String loginName,
                         @NotBlank(message = "密码不能为空.") @Size(min = 6, max = 20, message = "密码的长度为6-20之间.") String password){

        User user = loginService.login(loginName, password);
        return user;

    }

    @ApiOperation("设置账号密码")
    @PostMapping("/setName")
    public Object setName(@Size(min = 3, max = 12, message = "账号的长度为3-12之间") @RequestParam("loginName") String loginName,
                          @Size(min = 6, max = 20, message = "密码的长度为6-20之间.") String password){

        User user = loginService.update(loginName, password);
        return user;
    }

    /**
     *  暂时注释emailAddress参数上的 @Email
     */
    @ApiOperation("注册")
    @PostMapping("/add")
    public String add(@NotBlank(message = "邮箱不能为空")  @RequestParam("emailAddress") String emailAddress,
                      @Size(min = 6, max = 6, message = "验证码的长度为6位数.") @RequestParam("verificationCode") String verificationCode) throws UnsupportedEncodingException, MessagingException {

        String redisCode = Optional.ofNullable(redisTemplate.opsForValue().get("code:"+emailAddress)).map(Object::toString).orElse("");
        if(!redisCode.equals(verificationCode)){
            return ResultUtil.CustomException(ResultEnum.CustomException.getKey(), "您的验证码不正确,请重新输入.");
        }
        loginService.register(emailAddress);

        return "注册成功,请设置您的账号密码.";

    }



    /**
     *  暂时注释emailAddress参数上的 @Email
     */
    @ApiOperation("发送邮箱验证码")
    @GetMapping("/sendEmailVerificationCode")
    public String sendEmailVerificationCode(@Valid @NotBlank(message = "Email Address Is Null")
                                            @RequestParam("emailAddress")  String emailAddress) throws UnsupportedEncodingException, MessagingException {
        loginService.sendEmailVerificationCode(emailAddress);

        return "Send Email Success.";

    }



    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public String logout(){
        loginService.logout();
        return "您已退出登录,Bye Bye..";
    }


}
