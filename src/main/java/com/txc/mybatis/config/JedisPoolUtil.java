package com.txc.mybatis.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

@Slf4j
@Component
public class JedisPoolUtil {

    private static final String PROPERTIES_PATH = "redis.properties";
    private static JedisPool jedisPool;

    static {
        if (jedisPool == null) {
            try {
                init();
            } catch (FileNotFoundException e) {
                log.error("初始化JedisPool出错！错误信息："+e.getMessage());
            } catch (IOException e) {
                log.error("初始化JedisPool出错！错误信息："+e.getMessage());
            }
        }
    }

    /**
     * 初始化Jedis连接池
     *
     * @throws IOException
     */
    private static void init() throws IOException {
//        URL resource = JedisPoolUtil.class.getClassLoader().getResource(PROPERTIES_PATH);
//        if (resource == null) {
//            throw new FileNotFoundException("没有找到指定redis的配置文件:" + PROPERTIES_PATH);
//        }
//        //加载配置文件
//        InputStream input = new FileInputStream(resource.getFile());
//        Properties p = new Properties();
//        p.load(input);

        /**
         * 获取redis.properties获取配置内容
         */
        String host = "182.92.209.153";
        int port = 6379;

        int poolTimeOut = 2000;

        /**
         * 是否使用redis默认配置
         */
        boolean isSetDefault = true;

        if (isSetDefault) {
            jedisPool = new JedisPool(new GenericObjectPoolConfig(), host, port, poolTimeOut);
        }

    }

    /**
     * 从JedisPool连接池中获取一个连接
     * @return
     */
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 将一个使用完的连接还给连接池
     * @param jedis
     */
    public static void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
