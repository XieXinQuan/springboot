package com.huanting.quan.config;


import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/2/4
 */
@Configuration
public class ActivitiConfig extends AbstractProcessEngineAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(ActivitiConfig.class);


    /**
     * 没卵用
     * @return
     */
    @Bean
    public DataSource activitiDataSource(){
        return DataSourceBuilder.create()
                .url("jdbc:mysql://127.0.0.1:3306/springboot?useSSL=false&useUnicode=true&characterEncoding=UTF8&serverTimezone=UTC&serverTimezone=GMT%2b8")
                .username("root")
                .password("137286")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
//
//    @Bean
//    public SpringProcessEngineConfiguration springProcessEngineConfiguration(PlatformTransactionManager transactionManager,
//            SpringAsyncExecutor springAsyncExecutor) throws IOException {
//
//        return baseSpringProcessEngineConfiguration(
//                activitiDataSource(),
//                transactionManager,
//                springAsyncExecutor);
//    }

}
