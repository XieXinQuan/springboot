package com.huanting.quan.entity;

import lombok.Data;

import javax.persistence.*;
/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/11
 */
@Entity
@Data
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String nickName;

    private String trueName;

    private String idCrad;

    private Integer age;

    private String sex;

    private String phone;

    private String addressHome;

    private String addressWork;

    private String addressNow;

    private String hobby;

    private Double height;

    private Double weight;

    private String headImage;
}