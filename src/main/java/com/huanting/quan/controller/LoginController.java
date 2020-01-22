package com.huanting.quan.controller;

import com.huanting.quan.exception.GlobalException;
import com.huanting.quan.service.UserService;
import com.huanting.quan.util.ResultUtil;
import com.huanting.quan.util.StringUtils;
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
    UserService userService;

    @Resource
    private RedisTemplate redisTemplate;


    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @CrossOrigin
    @PostMapping("/login")
    @ApiOperation("登录")
    public String login(@Valid @NotBlank(message = "User Name Not Is Null") @Size(min = 6, max = 20, message = "User Name Length Is 6-20.") @RequestParam("loginName") String loginName,
                         @NotBlank(message = "Password Not Is Null") @Size(min = 6, max = 20, message = "Password Length Is 6-20.") String password,
                        @RequestParam("verificationCode") String verificationCode){
        if(!StringUtils.isEmpty(verificationCode) && !verificationCode.equals(redisTemplate.opsForValue().get(loginName))){
            throw new GlobalException(500, "验证码错误..");
        }
        if(userService.login(loginName, password)) {
            return ResultUtil.Success("Login Success...");
        }
        return ResultUtil.SystemException();
    }

    @ApiOperation("注册")
    @GetMapping("/add")
    public String add(@Valid @NotBlank(message = "User Name Not Is Null") @Size(min = 6, max = 20, message = "User Name Length Is 6-20.") @RequestParam("loginName") String loginName,
                      @NotBlank(message = "Password Not Is Null") @Size(min = 6, max = 20, message = "Password Length Is 6-20.") String password){
        if(!userService.add(loginName, password)) {
            return ResultUtil.SystemException();
        }

        return ResultUtil.Success("Add User Success.");

    }

    @ApiOperation("发送邮箱验证码")
    @CrossOrigin
    @GetMapping("/sendEmailVerificationCode")
    public String sendEmailVerificationCode(@Valid @NotBlank(message = "Email Address Is Null")
                                            @RequestParam("loginEmail") String loginEmail) throws UnsupportedEncodingException, MessagingException {
        userService.sendEmailVerificationCode(loginEmail);
        return ResultUtil.Success("Send Email Success.");

    }
}
