package com.huanting.quan.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 *
 * @author Administrator
 */
@Entity
@Data
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User {
    public User() {
    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String loginName;

    private String phone;

    private String emailAddress;

    private String password;

    private String encryptionPassword;

    private Integer type;

    private Integer status;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;

    private String createUser;

    private String updateUser;

    private Date loginTime;

    private Date suspendTime;

    private Integer score;

}