package com.huanting.quan.repository;

import com.huanting.quan.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/2/10
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findAllByUserId(Long userId);

    @Override
    Member save(Member member);
}
