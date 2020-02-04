package com.huanting.quan.design.strategy;

import com.huanting.quan.entity.Friend;
import com.huanting.quan.repository.FriendRepository;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/2/1
 */
public class AddFriendAgree implements AddFriendStrategy{


    @Override
    public String executeStrategy(FriendRepository friendRepository, Friend friend, String applicationReason) {
        return "你们已经是好友了.";
    }
}
