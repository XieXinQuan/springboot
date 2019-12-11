package com.example.Quan.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
//    @NotNull(message = "Primary key is not null")
    private Long id;

    @NotNull(message = "Name is not null")
    private String name;

    private String password;

    private Integer type;

    private Integer status;

    private Date createTime;

    @LastModifiedDate
    private Date updateTime;

    private String createUser;

    private String updateUser;

    private Date loginTime;

    private Date suspendTime;

    private Integer score;
}