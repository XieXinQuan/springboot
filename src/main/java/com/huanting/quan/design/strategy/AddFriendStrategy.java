package com.huanting.quan.design.strategy;

import com.huanting.quan.entity.Friend;
import com.huanting.quan.repository.FriendRepository;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/2/1
 */
public interface AddFriendStrategy {
    /**
     *  策略 添加好友已存在记录的情况
     * @param friend
     * @return
     */
    String executeStrategy(FriendRepository friendRepository, Friend friend, String applicationReason);
}
