package com.huanting.quan.service;

import com.huanting.quan.repository.MemberRepository;
import com.huanting.quan.repository.UserRepository;
import com.huanting.quan.util.PageUtil;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Resource
    MemberRepository memberRepository;


    /**
     * 使用map形式
     */
    public List<Map<String, Object>> loadAllUserByPage(Integer pageIndex, Integer pageCount){

        return userRepository.loadAllByPage(getCurrentUserId(), PageUtil.getSimplePageable(pageIndex, pageCount, Sort.by("id")));
    }

    /**
     * 判断是否是会员
     */
    public boolean isMember(){
        //判断session是否 有值 是会员
        Object isMember = getSession().getAttribute("isMember");
        if (isMember != null){
            return Boolean.parseBoolean(isMember.toString());
        }

        //如果无 查数据库 更新session
        Integer count = Optional.ofNullable(memberRepository.countByUserId(getCurrentUserId())).orElse(0);
        if (count > 0){
            getSession().setAttribute("isMember", true);
        }
        return count > 0;

    }



}
