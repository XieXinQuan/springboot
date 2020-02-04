package com.huanting.quan.service;

import com.huanting.quan.Enum.FriendStatus;
import com.huanting.quan.design.strategy.AddFriendAgree;
import com.huanting.quan.design.strategy.AddFriendApplying;
import com.huanting.quan.design.strategy.AddFriendContext;
import com.huanting.quan.design.strategy.AddFriendRefuse;
import com.huanting.quan.entity.Friend;
import com.huanting.quan.repository.FriendRepository;
import com.huanting.quan.util.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/30
 */
@Service
public class FriendService extends BaseService{
    @Resource
    FriendRepository friendRepository;

    public String addFriend(Long friendUserId, String applicationReason) {

        //先检查是否已添加 / 已申请
        Friend isExists = friendRepository.findAllByUserIdAndFriendId(getCurrentUserId(), friendUserId);
        if (isExists != null){
            AddFriendContext context = new AddFriendContext();
            if (isExists.getStatus().equals(FriendStatus.Normal.getKey())){
                context.setFriendStrategy(new AddFriendAgree());
            }else if (isExists.getStatus().equals(FriendStatus.WaitToAgree.getKey())){
                context.setFriendStrategy(new AddFriendApplying());
            }else if (isExists.getStatus().equals(FriendStatus.Refuse.getKey())){
                context.setFriendStrategy(new AddFriendRefuse());
            }
            return context.execute(friendRepository, isExists, applicationReason);
        }

        Friend friend = new Friend();
        friend.setUserId(getCurrentUserId());
        friend.setFriendId(friendUserId);
        friend.setApplicationReason(applicationReason);

        friend.setStatus(FriendStatus.WaitToAgree.getKey());

        friendRepository.save(friend);
        return "已发送申请.";
    }

    /**
     * 事务处理, 避免一方添加 一方没添加
     * @param id
     * @param status
     * @throws Exception
     */
    @Transactional(rollbackFor=Exception.class)
    public void dealFriendApplication(Long id, Integer status) throws Exception{

        //校验status
        //2020/1/31 使用@EnumValue 注解
//        if(!EnumUtil.isContain(FriendStatus.values(), status)) throw new GlobalException(ResultEnum.ParameterError.getKey());

        Friend friend = friendRepository.findAllById(id);
        friend.setStatus(status);
        friendRepository.save(friend);

        if(status.equals(FriendStatus.Normal.getKey())){
            Friend addFriend = new Friend();
            addFriend.setStatus(status);
            addFriend.setUserId(friend.getFriendId());
            addFriend.setFriendId(friend.getUserId());
            friendRepository.save(addFriend);
        }
    }
    public List<Map<String, Object>> loadFriendList(Integer pageIndex, Integer pageCount){

        return friendRepository.loadFriendList(getCurrentUserId(), PageUtil.getSimplePageable(pageIndex, pageCount));
    }
}
