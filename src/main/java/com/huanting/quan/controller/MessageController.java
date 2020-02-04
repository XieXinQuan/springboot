package com.huanting.quan.controller;

import com.huanting.quan.service.MessageService;
import com.huanting.quan.service.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/24
 */
@Api(tags = "消息")
@RequestMapping("/message")
@RestController
public class MessageController {
    @Resource
    MessageService messageService;

    private Logger logger = LoggerFactory.getLogger(MessageController.class);
    @RequestMapping("/send/{toUserId}")
    public String sendToUserId(String message, @PathVariable String toUserId) throws IOException {

        logger.info("User {} Send Message To {}, Message Content: {}", "Quan", toUserId, message);

        //判断对方是否在线

        WebSocketServer.sendInfo(message, toUserId);
        return "Send Success.";
    }

    @ApiOperation("发送消息")
    @PostMapping("/send")
    public String send(@NotBlank(message = "消息内容不能为空") @RequestParam("content") String content,
                       @NotBlank(message = "对方Id为空") @RequestParam("toUserId") Long toUserId) throws IOException {

        messageService.sendMessage(toUserId, content);

        return "发送成功";
    }

    @ApiOperation("登录加载所有通知")
    @PostMapping("/loadAllNotice")
    public Object loadAllNotice(){


        return messageService.loadAllAddFriendMessage();

    }

    @ApiOperation("撤回消息")
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("id") Long id){

        return messageService.withdraw(id);
    }

}
