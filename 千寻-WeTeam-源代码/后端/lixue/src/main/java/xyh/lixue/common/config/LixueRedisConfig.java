package xyh.lixue.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import xyh.lixue.user.entity.User;

/**
 * @author XiangYida
 * @version 2019/5/5 13:58
 */
@Configuration
public class LixueRedisConfig {
//    @Bean
//    public RedisTemplate<String, RootEntity> redisTemplate1(RedisConnectionFactory factory){
//        RedisTemplate<String,RootEntity>template=new RedisTemplate<>();
//        //关联
//        template.setConnectionFactory(factory);
//        //设置key的序列化器
//        template.setKeySerializer(new StringRedisSerializer());
//        //设置value的序列化器
//        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(RootEntity.class));
//        return template;
//    }

    @Bean
    public RedisTemplate<String,User> RedisTemplate(RedisConnectionFactory factory){

        RedisTemplate<String,User>template=new RedisTemplate<>();
        //关联
        template.setConnectionFactory(factory);
        //设置key的序列化器
        template.setKeySerializer(new StringRedisSerializer());
        //设置hashKey的序列化
        template.setHashKeySerializer(new StringRedisSerializer());
        //设置value的序列化器
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
        return template;
    }

}
