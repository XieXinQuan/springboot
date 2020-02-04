package com.huanting.quan.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/30
 */
@Table(name = "friend")
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Friend {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long friendId;

    private String noteName;

    private Integer type;

    private Integer status;

    private String ApplicationReason;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;

    private String createUser;

    private String updateUser;

}