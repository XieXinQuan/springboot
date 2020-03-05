package com.huanting.quan.repository;

import com.huanting.quan.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/28
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Message findAllByUserIdAndReceiveUserIdAndContentAndCreateTime(Long userId, Long receiveUserId, String Content, Date createTime);

    /**
     * 用于保存Redis存的消息
     * @param iterable
     * @param <S>
     * @return
     */
    @Override
    <S extends Message> List<S> saveAll(Iterable<S> iterable);



    /**
     * 保存消息
     * @param message
     * @return
     */
    @Override
    Message save(Message message);

}
