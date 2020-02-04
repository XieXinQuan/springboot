package com.huanting.quan.service;

import com.alibaba.fastjson.JSONObject;
import com.huanting.quan.Enum.FriendStatus;
import com.huanting.quan.Enum.MessageStatus;
import com.huanting.quan.Enum.ResultEnum;
import com.huanting.quan.exception.GlobalException;
import com.huanting.quan.repository.FriendRepository;
import com.huanting.quan.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/28
 */
@Service
public class MessageService extends BaseService{
    private Logger logger = LoggerFactory.getLogger(MessageService.class);
    @Resource
    MessageRepository messageRepository;

    @Resource
    FriendRepository friendRepository;

    @Resource
    RedisTemplate redisTemplate;

    /**
     * 发送消息 先存入Redis, 定时器 定时扫描入库
     */

    public void sendMessage(Long toUserId, String messageContent) throws IOException {

        ConcurrentHashMap<Long, WebSocketServer> webSocketMap = WebSocketServer.webSocketMap;

        if(getCurrentUserId() == null){
            throw new GlobalException(ResultEnum.GoToLogin.getKey());
        }

        JSONObject jsonObject = new JSONObject(7);
        jsonObject.put("userId", getCurrentUserId());
        jsonObject.put("receiveUserId", toUserId);
        jsonObject.put("content", messageContent);
        jsonObject.put("createTime", System.currentTimeMillis());

        //在线 --发送 并设置存入Redis Status
        if (!webSocketMap.isEmpty() && webSocketMap.containsKey(toUserId)){

            jsonObject.put("userName", getCurrentUserName());
            webSocketMap.get(toUserId).sendMessage(jsonObject.toString());
            //此处移除userName 存入Redis数据量少一点
            jsonObject.remove("userName");
            jsonObject.put("status", MessageStatus.SendSuccess.getKey());
        }else {
            logger.info("{}不在线, 消息:{}, 存入Redis", toUserId, messageContent);
            jsonObject.put("status", MessageStatus.OfflineMessage.getKey());
        }

        ListOperations<String, String> listOperations = redisTemplate.opsForList();

        listOperations.leftPush("messageContent", jsonObject.toString());

        Long messageContent1 = redisTemplate.opsForList().size("messageContent");


    }

    public List<Map<String, Object>> loadAllAddFriendMessage(){
        List<Map<String, Object>> allByFriendIdAndStatus = friendRepository.findAllByFriendIdAndStatus(getCurrentUserId(), FriendStatus.WaitToAgree.getKey());


        return allByFriendIdAndStatus;
    }

    public String withdraw(Long id){
        List<String> messageContent = redisTemplate.opsForList().range("messageContent", 0, -1);
        return "撤回成功!";
    }

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now());
    }
}
