package com.example.Quan.controller;

import com.example.Quan.entity.User;
import com.example.Quan.exception.GlobalException;
import com.example.Quan.repository.UserRepository;
import com.example.Quan.service.UserService;
import com.example.Quan.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class LoginController {
    @Resource
    UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @GetMapping("/")
    public String welcome() throws GlobalException {
        if (1==1)throw new GlobalException(1,"555");
        return ResultUtil.Success("Success");
    }

    @PostMapping("/login")
    public String login(){

        return "success";
    }
    @GetMapping("/test")
    public String test(String name, String password){


        userService.add(name, password);
        return ResultUtil.Success("Success");
    }
    @GetMapping("/update")
    public String update(String name, String password){


        userService.update(name, password);
        return ResultUtil.Success("Success");
    }
}
