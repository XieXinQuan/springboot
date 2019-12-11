package com.example.Quan.repository;

import com.example.Quan.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findAllById(Long id);
    User findAllByName(String name);
    User findAllByNameLike(String name);

    @Query(value = "select * from user", nativeQuery = true)
    Page<User> findAllByCustomize(Pageable pageable);

    @Override
    User save(User user);
}
