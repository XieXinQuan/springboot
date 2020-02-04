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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Scheduled(fixedRate = 60000L)
    private void monitorRedisMessageSize(){
        Long messageContent = redisTemplate.opsForList().size("messageContent");
        if(messageContent >= Constant.batchSaveMessageSize){
            redisMessageToDb();
        }


    }

    private void redisMessageToDb(){

        Long startTime = System.currentTimeMillis();
        logger.info("Message Schedule Start....");

        List<Object> list = new ArrayList<>();

        Object messageContent = null;
        while((messageContent = getNextValue()) != null){

            list.add(messageContent);
            if(list.size() >= Constant.batchSaveMessageSize){
                break;
            }
        }

        Date date ;
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

    private Object getNextValue(){
        return redisTemplate.opsForList().rightPop("messageContent");
    }



}
