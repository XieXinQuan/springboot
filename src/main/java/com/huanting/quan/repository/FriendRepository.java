package com.huanting.quan.repository;

import com.huanting.quan.entity.Friend;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/30
 */
@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    Friend findAllById(Long id);


    Friend findAllByUserIdAndFriendId(Long userId, Long friendId);

    @Query(value = "SELECT u.id as id, u.NAME as name FROM friend f " +
            " LEFT JOIN user u on f.FRIEND_ID = u.ID " +
            " left JOIN user ut on f.USER_ID = ut.id " +
            " where ut.id = ?1 and f.STATUS = '1' ", nativeQuery = true)
    List<Map<String, Object>> loadFriendList(Long userId, Pageable pageable);


    /**
     * 加载申请消息
     * @param userId
     * @param status
     * @return
     */
    @Query(value = "SELECT f.id as id, f.APPLICATION_REASON as content, " +
            "ut.NAME as name, f.CREATE_TIME as time FROM friend f " +
            " LEFT JOIN user u on f.FRIEND_ID = u.ID " +
            " left JOIN user ut on f.USER_ID = ut.id " +
            "  where u.id = ?1 and f.STATUS = ?2  ", nativeQuery = true)
    List<Map<String, Object>> findAllByFriendIdAndStatus(Long userId, Integer status);

    /**
     * 保存接口
     * @param friend
     * @return
     */
    @Override
    Friend save(Friend friend);
}
