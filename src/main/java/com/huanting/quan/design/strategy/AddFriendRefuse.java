package com.huanting.quan.design.strategy;

import com.huanting.quan.Enum.FriendStatus;
import com.huanting.quan.entity.Friend;
import com.huanting.quan.repository.FriendRepository;

import javax.annotation.Resource;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/2/1
 */
public class AddFriendRefuse implements AddFriendStrategy{
    @Resource
    FriendRepository friendRepository;


    @Override
    public String executeStrategy(FriendRepository friendRepository, Friend friend, String applicationReason) {
        friend.setStatus(FriendStatus.WaitToAgree.getKey());
        friendRepository.save(friend);
        return "申请发送中.";
    }
}
