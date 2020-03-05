package com.huanting.quan.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huanting.quan.Enum.FriendStatus;
import com.huanting.quan.Enum.MessageStatus;
import com.huanting.quan.Enum.NoticeType;
import com.huanting.quan.Enum.ResultEnum;
import com.huanting.quan.entity.Message;
import com.huanting.quan.exception.GlobalException;
import com.huanting.quan.repository.FriendRepository;
import com.huanting.quan.repository.MessageRepository;
import com.huanting.quan.schedule.MessageSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
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

    public Long sendMessage(Long toUserId, String messageContent, boolean isMember) throws IOException {

        ConcurrentHashMap<Long, WebSocketServer> webSocketMap = WebSocketServer.webSocketMap;

        if(getCurrentUserId() == null){
            throw new GlobalException(ResultEnum.GoToLogin.getKey());
        }

        Long currentTime = System.currentTimeMillis();

        JSONObject jsonObject = new JSONObject(7);
        jsonObject.put("userId", getCurrentUserId());
        jsonObject.put("receiveUserId", toUserId);
        jsonObject.put("content", messageContent);
        jsonObject.put("createTime", currentTime);

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

        String key = "messageContent";
        if (isMember){
            key = "vipMessage-" + getCurrentUserId();
        }
        listOperations.leftPush(key, jsonObject.toString());

        return currentTime;

    }

    public List<Map<String, Object>> loadAllAddFriendMessage(){
        List<Map<String, Object>> allByFriendIdAndStatus = friendRepository.findAllByFriendIdAndStatus(getCurrentUserId(), FriendStatus.WaitToAgree.getKey());

        List<Map<String, Object>> tempList = new ArrayList<>();
        allByFriendIdAndStatus.stream().forEach((stringObjectMap -> {
            Set<String> strings = stringObjectMap.keySet();
            Map<String, Object> map = new HashMap<>();
            for (String str : strings){
                map.put(str, stringObjectMap.get(str));
            }
            map.put("type", NoticeType.FriendApplication.getKey());
            tempList.add(map);
        }));
//        AbstractJpaQuery->TupleBackedMap 没有Override put
//        allByFriendIdAndStatus.stream().forEach(stringObjectMap -> {
//            stringObjectMap.put("type", NoticeType.FriendApplication.getValue());
//        });

        return tempList;
    }

    public String withdraw(Long receiveUserId, String content, Long time) throws IOException {

        Long size = redisTemplate.opsForList().size(MessageSchedule.redisVipMessageKeyPrefix + getCurrentUserId());
        List<Message> list = new ArrayList<>();
        if (size > 0){
            Object obj = null;
            boolean isExistsRedis = false;
            while ((obj = redisTemplate.opsForList().rightPop(MessageSchedule.redisVipMessageKeyPrefix + getCurrentUserId())) != null){
                JSONObject jsonObject = JSON.parseObject(obj.toString());
                Message message = new Message();

                message.setUserId(jsonObject.getLong("userId"));
                message.setStatus(jsonObject.getInteger("status"));
                message.setReceiveUserId(jsonObject.getLong("receiveUserId"));
                message.setContent(jsonObject.getString("content"));
                message.setCreateTime(new Date(jsonObject.getLong("createTime")));

                if (message.getCreateTime().getTime() == time
                    && message.getContent().equals(content)){
                    message.setStatus(MessageStatus.WithDraw.getKey());
                    isExistsRedis = true;
                }

                list.add(message);
            }
            if (!isExistsRedis){
                Message dbMessage = messageRepository.findAllByUserIdAndReceiveUserIdAndContentAndCreateTime(getCurrentUserId(), receiveUserId, content, new Date(time));
                if (dbMessage == null){
                    throw new GlobalException(ResultEnum.CustomException.getKey(), "消息撤回失败!");
                }
                dbMessage.setStatus(MessageStatus.WithDraw.getKey());
                messageRepository.save(dbMessage);

                ConcurrentHashMap<Long, WebSocketServer> webSocketMap = WebSocketServer.webSocketMap;

                if (webSocketMap.containsKey(receiveUserId)){
                    webSocketMap.get(receiveUserId).sendMessage("");
                }
            }
            messageRepository.saveAll(list);

        }

        return "撤回成功!";
    }

    public static void main(String[] args) {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(new HashMap<>());
        list.stream().forEach(map -> {
            map.put("hhh", "a");
        });
        list.stream().forEach(map -> {
            System.out.println(map.toString());
        });
    }


//    public static void main(String[] args) {
//        System.out.println(LocalDateTime.now());
//    }
}
