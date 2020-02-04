package com.huanting.quan.design.strategy;

import com.huanting.quan.entity.Friend;
import com.huanting.quan.repository.FriendRepository;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/2/1
 */
public class AddFriendApplying implements AddFriendStrategy{


    @Override
    public String executeStrategy(FriendRepository friendRepository, Friend friend, String applicationReason) {

        friend.setApplicationReason(applicationReason);
        friendRepository.save(friend);

        return "正在为您催单..";
    }
}
