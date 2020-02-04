package com.huanting.quan.design.strategy;

import com.huanting.quan.entity.Friend;
import com.huanting.quan.repository.FriendRepository;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/2/1
 */
public class AddFriendContext {
    private AddFriendStrategy friendStrategy;


    public String execute(FriendRepository friendRepository, Friend friend, String applicationReason){
        return friendStrategy.executeStrategy(friendRepository, friend, applicationReason);
    }

    public void setFriendStrategy(AddFriendStrategy friendStrategy) {
        this.friendStrategy = friendStrategy;
    }
}
