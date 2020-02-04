package com.huanting.quan.service;

import com.huanting.quan.repository.UserRepository;
import com.huanting.quan.util.PageUtil;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/26
 */
@Service
public class UserService extends BaseService{
    @Resource
    RedisTemplate redisTemplate;

    @Resource
    UserRepository userRepository;


    /**
     * 使用map形式
     */
    public List<Map<String, Object>> loadAllUserByPage(Integer pageIndex, Integer pageCount){

        return userRepository.loadAllByPage(getCurrentUserId(), PageUtil.getSimplePageable(pageIndex, pageCount, Sort.by("id")));
    }



}
