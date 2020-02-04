package com.huanting.quan.repository;

import com.huanting.quan.dto.UserDto;
import com.huanting.quan.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/11
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findAllById(Long id);
    User findAllByName(String name);
    User findAllByNameLike(String name);
    User findByName(String name);

    @Query(value = "select name from User where id = ?1")
    String getUserNameById(Long id);

    Integer countByEmailAddress(String email);

    User findByNameAndPassword(String name, String password);

    User findAllByLoginName(String loginName);

    /**
     * Dto形式, 但会存在target 以及 targetClass
     * @param pageable
     * @return
     */
    @Query(value = "select id, name  from user", nativeQuery = true)
    Page<UserDto> loadAllByPageByDto(Pageable pageable);

    /**
     * Map形式
     * @param id
     * @param pageable
     * @return
     */
    @Query(value = "select id, name from user " +
            "where STATUS = 1 " +
            "and if(?1 is Not Null, id <> ?1, 1 = 1) ", nativeQuery = true)
    List<Map<String, Object>> loadAllByPage(Long id, Pageable pageable);

    /**
     * 分页查询
     * @param pageable
     * @return
     */
    @Override
    Page<User> findAll(Pageable pageable);

    /**
     * 保存或更新
     * @param user
     * @return
     */
    @Override
    User save(User user);
}
