package com.txc.mybatis.service;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @ClassName SocketProperties
 * @Description TODO
 * @Date 2023/8/15 10:24
 * @Vertion 1.0
 **/
@ToString
@Component
@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "socket")
@Data
public class SocketProperties {
    private Integer port;
    private String host;

}

