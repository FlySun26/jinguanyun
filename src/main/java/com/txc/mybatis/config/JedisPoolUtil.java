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
        URL resource = JedisPoolUtil.class.getClassLoader().getResource(PROPERTIES_PATH);
        if (resource == null) {
            throw new FileNotFoundException("没有找到指定redis的配置文件:" + PROPERTIES_PATH);
        }
        //加载配置文件
        InputStream input = new FileInputStream(resource.getFile());
        Properties p = new Properties();
        p.load(input);

        /**
         * 获取redis.properties获取配置内容
         */
        String host = p.getProperty("redis.host") == null ? "localhost" : p.getProperty("redis.host");
        int port = p.getProperty("redis.port") == null ? 6379 : Integer.parseInt(p.getProperty("redis.port"));
        String auth = p.getProperty("redis.auth");
        int poolTimeOut = p.getProperty("connectionTimeOut") == null ? 2000
                : Integer.parseInt(p.getProperty("connectionTimeOut"));

        /**
         * 是否使用redis默认配置
         */
        boolean isSetDefault = p.getProperty("defaultSetting") == null ? true
                : Boolean.parseBoolean(p.getProperty("defaultSetting"));

        if (isSetDefault) {
            jedisPool = new JedisPool(new GenericObjectPoolConfig(), host, port, poolTimeOut, auth);
        } else {
            JedisPoolConfig config = new JedisPoolConfig();
            String blockWhenExhausted = p.getProperty("redis.blockWhenExhausted");
            if (blockWhenExhausted != null) {
                config.setBlockWhenExhausted(Boolean.parseBoolean(blockWhenExhausted));
            }
            String evictionPolicyClassName = p.getProperty("redis.evictionPolicyClassName");
            if (evictionPolicyClassName != null) {
                config.setEvictionPolicyClassName(evictionPolicyClassName);
            }
            String jmxEnabled = p.getProperty("redis.jmxEnabled");
            if (jmxEnabled != null) {
                config.setJmxEnabled(Boolean.parseBoolean(jmxEnabled));
            }
            String lifo = p.getProperty("redis.lifo");
            if (lifo != null) {
                config.setLifo(Boolean.parseBoolean(lifo));
            }
            String maxIdle = p.getProperty("redis.maxIdle");
            if (maxIdle != null) {
                config.setMaxIdle(Integer.parseInt(maxIdle));
            }
            String maxTotal = p.getProperty("redis.maxTotal");
            if (maxTotal != null) {
                config.setMaxTotal(Integer.parseInt(maxTotal));
            }
            String maxWaitMillis = p.getProperty("redis.maxWaitMillis");
            if (maxWaitMillis != null) {
                config.setMaxWaitMillis(Long.parseLong(maxWaitMillis));
            }
            String minEvictableIdleTimeMillis = p.getProperty("redis.minEvictableIdleTimeMillis");
            if (minEvictableIdleTimeMillis != null) {
                config.setMinEvictableIdleTimeMillis(Long.parseLong(minEvictableIdleTimeMillis));
            }
            String minIdle = p.getProperty("redis.minIdle");
            if (minIdle != null) {
                config.setMinIdle(Integer.parseInt(minIdle));
            }
            String numTestsPerEvictionRun = p.getProperty("redis.numTestsPerEvictionRun");
            if (numTestsPerEvictionRun != null) {
                config.setNumTestsPerEvictionRun(Integer.parseInt(numTestsPerEvictionRun));
            }
            String softMinEvictableIdleTimeMillis = p.getProperty("redis.softMinEvictableIdleTimeMillis");
            if (softMinEvictableIdleTimeMillis != null) {
                config.setSoftMinEvictableIdleTimeMillis(Long.parseLong(softMinEvictableIdleTimeMillis));
            }
            String testOnBorrow = p.getProperty("redis.testOnBorrow");
            if (testOnBorrow != null) {
                config.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
            }
            String testWhileIdle = p.getProperty("redis.testWhileIdle");
            if (testWhileIdle != null) {
                config.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle));
            }
            String timeBetweenEvictionRunsMillis = p.getProperty("redus.timeBetweenEvictionRunsMillis");
            if (timeBetweenEvictionRunsMillis != null) {
                config.setTimeBetweenEvictionRunsMillis(Long.parseLong(timeBetweenEvictionRunsMillis));
            }
            jedisPool = new JedisPool(config, host, port, poolTimeOut, auth);
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
