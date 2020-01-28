package com.huanting.quan.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/28
 */
@Table(name = "message")
@Entity
@Data
public class Message implements Iterable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long receiveUserId;

    private String content;

    private Integer type;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private String createUser;

    private String updateUser;

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer action) {

    }

    @Override
    public Spliterator spliterator() {
        return null;
    }
}