package com.huanting.quan.controller;

import com.huanting.quan.Enum.ResultEnum;
import com.huanting.quan.exception.GlobalException;
import com.huanting.quan.service.ActivitiService;
import com.huanting.quan.service.MessageService;
import com.huanting.quan.service.UserService;
import com.huanting.quan.service.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Resource
    UserService userService;
    @Resource
    ActivitiService activitiService;

    private Logger logger = LoggerFactory.getLogger(MessageController.class);

    /**
     * 舍弃
     */
    @RequestMapping("/send/{toUserId}")
    public String sendToUserId(String message, @PathVariable String toUserId) throws IOException {

        logger.info("User {} Send Message To {}, Message Content: {}", "Quan", toUserId, message);

        //判断对方是否在线

        WebSocketServer.sendInfo(message, toUserId);
        return "Send Success.";
    }

    @ApiOperation("发送消息")
    @PostMapping("/send")
    public Object send(@NotBlank(message = "消息内容不能为空") @RequestParam("content") String content,
                       @NotBlank(message = "对方Id为空") @RequestParam("toUserId") Long toUserId) throws IOException {
        return messageService.sendMessage(toUserId, content, userService.isMember());
    }

    @ApiOperation("登录加载所有通知")
    @PostMapping("/loadAllNotice")
    public Object loadAllNotice(){
        List<Object> result = new ArrayList<>();

        //加载好友请求
        List<Map<String, Object>> list = messageService.loadAllAddFriendMessage();

        //加载需要我审批的申请
        List<Map<String, Object>> list1 = activitiService.loadNeedMyApproveTask();


        //合并所有
        if (list != null) {
            result.addAll(list);
        }
        if (list1 != null) {
            result.addAll(list1);
        }

        return result;

    }

    @ApiOperation("撤回消息")
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("receiveUserId") Long receiveUserId,
                           @RequestParam("time") Long time, @RequestParam("content") String content) throws IOException {

        boolean isMember = userService.isMember();
        if (!isMember){
            throw new GlobalException(ResultEnum.ApplicationMember.getKey(), "只有会员才能撤回消息, 请先注册会员..");
        }
        return messageService.withdraw(receiveUserId, content, time);
    }

}
