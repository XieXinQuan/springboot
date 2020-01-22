package com.huanting.quan.entity;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/11
 */
@Entity
@Data
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name is not null")
    @Size(min = 6, max = 20, message = "LoginName Length Is 6-20 Char")
    private String name;

    @NotNull(message = "Name is not null")
    @Size(min = 6, max = 20, message = "Password Length Is 6-20 Char")
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