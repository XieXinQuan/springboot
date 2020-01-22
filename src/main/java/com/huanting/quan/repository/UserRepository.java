package com.huanting.quan.repository;

import com.huanting.quan.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
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

    User findByNameAndPassword(String name, String password);

    @Query(value = "select * from user", nativeQuery = true)
    Page<User> findAllByCustomize(Pageable pageable);

    @Override
    User save(User user);
}
