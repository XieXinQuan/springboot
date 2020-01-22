package com.huanting.quan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/11
 */
@Controller
public class WelcomeController {
    @GetMapping("/")
    public String welocme(){
        return "forward:index.html";
    }
}
