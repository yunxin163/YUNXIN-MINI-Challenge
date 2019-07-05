package xyh.lixue.user.service.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyh.lixue.common.utils.HttpUtil;
import xyh.lixue.user.entity.User;
import xyh.lixue.user.mapper.UserMapper;
import xyh.lixue.user.entity.PPT;
import xyh.lixue.user.entity.Push;
import xyh.lixue.user.entity.SearchRecords;
import xyh.lixue.user.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author XiangYida
 * @version 2019/5/6 19:18
 */
@Service
public class UserServiceImpl implements UserService {
    @Value("${wechat.url}")
    private String url;

    private UserMapper userMapper;

    private RedisTemplate<String, User> redisTemplate;
    @Autowired
    public UserServiceImpl(UserMapper userMapper,RedisTemplate<String,User> redisTemplate){
        this.redisTemplate=redisTemplate;
        this.userMapper=userMapper;
    }

    @Override
    public List<Push> pushHtml() {
       return userMapper.getPush();
    }

    @Override
    public List<SearchRecords> getSearchRecords(String userId) {
        return userMapper.getSearchRecords(userId);
    }

    @Override
    public String getOpenId(String code) {
       String url=this.url+code;
       String json=HttpUtil.get(url);
       JSONObject jsonObject = new JSONObject(json);
       return jsonObject.getString("openid");
        //use by test
//        return "openId";
    }

    @Override
    public List<PPT> getPPT() {
        return userMapper.getPPT();
    }

    @Override
    public void recordSearch(SearchRecords searchRecords) {
        userMapper.insertSearchRecords(searchRecords);
    }

    @Override
    public void recordLogin(String userId) {
        HashOperations<String,String,User> hashOperations=redisTemplate.opsForHash();
        //查询用户是否为新用户
        if(null==hashOperations.get("user",userId)){
            User user=new User();
            user.setId(userId);
            //添加到redis中
            hashOperations.put("user",userId,user);
            //添加到mysql中
            userMapper.insertUser(user);
        }
        userMapper.insertLoginRecords(userId);
    }

    @Override
    public void importUserToRedis() {
        HashOperations<String,String,User> hashOperations=redisTemplate.opsForHash();
        List<User> list=userMapper.getAllUser();
        Map<String,User>map=new HashMap<>();
        list.forEach(user->map.put(user.getId(),user));
        hashOperations.putAll("user",map);
    }
}
