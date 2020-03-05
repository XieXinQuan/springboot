package com.huanting.quan.config;

import com.huanting.quan.util.SecurityUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * 设置 jpa的CreateUser and UpdateUser
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/2/10
 */
@Configuration
public class AuditorUserConfig implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        String currentUserName = SecurityUtils.getCurrentUserName();
        if (currentUserName != null){
            return Optional.of(currentUserName);
        }
        return Optional.ofNullable("");
    }
}
