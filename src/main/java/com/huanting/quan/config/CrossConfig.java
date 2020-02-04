package com.huanting.quan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/11
 */
@Configuration
public class CrossConfig  {
    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", config);

        return new CorsFilter(configurationSource);
    }
}
