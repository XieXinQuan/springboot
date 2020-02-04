package com.huanting.quan.controller;

import com.huanting.quan.Enum.Constant;
import com.huanting.quan.Enum.FriendStatus;
import com.huanting.quan.annotation.EnumValue;
import com.huanting.quan.service.FriendService;
import com.huanting.quan.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/26
 */
@Validated
@Api(tags = "用户操作")
@RequestMapping("/user")
@RestController
public class UserController {
    @Resource
    UserService userService;

    @Resource
    FriendService friendService;

    @ApiOperation("加载所有用户-分页")
    @PostMapping("/allUser")
    public Object allUser(@RequestParam("pageIndex") Integer pageIndex,
                          @RequestParam("pageCount") Integer pageCount){

        pageIndex = Optional.ofNullable(pageIndex).orElse(Constant.pageInitIndex);
        pageCount = Optional.ofNullable(pageCount).orElse(Constant.pageInitCount);

        List<Map<String, Object>> maps = userService.loadAllUserByPage(pageIndex, pageCount);

        return maps;
    }

    @ApiOperation("添加好友")
    @PostMapping("addFriend")
    public String addFriend(@RequestParam("userId") Long userId,
                            @Size(max = 200) @RequestParam("applicationReason") String applicationReason){


        return friendService.addFriend(userId, applicationReason);

    }
    @ApiOperation("处理通知")
    @PostMapping("/dealNotice")
    public String dealNotice(@RequestParam("id") Long id,
                             @EnumValue(FriendStatus.class) Integer status) throws Exception {
        friendService.dealFriendApplication(id, status);
        return "操作成功";
    }


    @GetMapping("/test")
    public String allUserTest(@Valid @EnumValue (FriendStatus.class) @RequestParam("status") Integer status) throws Exception {

//        Friend friend = friendService.addFriend(7L, "我是阿爸");
//
//        friendService.agreeFriendApplication(friend.getId(), FriendStatus.Normal.getKey());

        return null;

    }

    @ApiOperation("我的好友列表")
    @PostMapping("/loadMyFriendList")
    public Object loadFriendList(Integer pageIndex, Integer pageCount){
        return friendService.loadFriendList(pageIndex, pageCount);
    }

}
