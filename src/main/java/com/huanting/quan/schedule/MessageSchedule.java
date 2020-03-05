package com.huanting.quan.schedule;

import com.alibaba.fastjson.JSONObject;
import com.huanting.quan.Enum.Constant;
import com.huanting.quan.entity.Message;
import com.huanting.quan.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/28
 */
@Configuration
@EnableScheduling
public class MessageSchedule {

    private Logger logger = LoggerFactory.getLogger(MessageSchedule.class);

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    MessageRepository messageRepository;

    private String redisMessageKey = "messageContent";
    private String redisVipMessageKey = "vipMessage-*";
    public static String redisVipMessageKeyPrefix = "vipMessage-";


    @Scheduled(fixedRate = 10000L)
    private void monitorRedisMessageSize(){
        Long messageSize = redisTemplate.opsForList().size(this.redisMessageKey);
        if(messageSize >= Constant.batchSaveMessageSize){
            redisMessageToDb(redisMessageRead(this.redisMessageKey));
        }
        Set<String> keys = redisTemplate.keys(this.redisVipMessageKey);
        Long messageVipSize = getVipMessageSize(keys);

        if(messageVipSize >= Constant.batchSaveMessageSize){
            redisMessageToDb(getNextValue(keys));
        }
    }

    private Long getVipMessageSize(Set<String> keys){
        Long count = 0L;
        for (String key : keys){
            count += redisTemplate.opsForList().size(key);
        }
        return count;
    }

    private List<Object> redisMessageRead(String key){


        List<Object> list = new ArrayList<>();

        Object messageContent = null;
        while((messageContent = getNextValue(key)) != null){

            list.add(messageContent);
            if(list.size() >= Constant.batchSaveMessageSize){
                break;
            }
        }
        return list;
    }
    private void redisMessageToDb(List<Object> list){

        Long startTime = System.currentTimeMillis();
        logger.info("Message Schedule Start....");

        List<Message> messages = new ArrayList<>();
        Message message ;
        JSONObject jsonObject ;
        for (Object object: list) {

            jsonObject = JSONObject.parseObject(object.toString());
            message = new Message();

            message.setUserId(jsonObject.getLong("userId"));
            message.setStatus(jsonObject.getInteger("status"));
            message.setReceiveUserId(jsonObject.getLong("receiveUserId"));
            message.setContent(jsonObject.getString("content"));
            message.setCreateTime(new Date(jsonObject.getLong("createTime")));

            messages.add(message);
        }

        messageRepository.saveAll(messages);
        Long executeTime = System.currentTimeMillis() - startTime;
        logger.info("Redis Message Save To Db Time:{}ms, Success Size: {}.", executeTime, messages.size());
        logger.info("Message Schedule End....");
    }

    private List<Object> getNextValue(Set<String> keys){
        List<Object> list = new ArrayList<>();
        for (String key : keys){
            Object object = null;
            while ((object = getNextValue(key)) != null){
                if (checkIsBeyond(list)) {
                    list.add(object);
                    return list;
                }
            }
        }
        return list;
    }
    private boolean checkIsBeyond(List list){
        Integer size = Optional.ofNullable(list).map(List::size).orElse(0);
        return size > Constant.batchSaveMessageSize - 1;
    }
    private Object getNextValue(String key){
        return redisTemplate.opsForList().rightPop(key);
    }




}
