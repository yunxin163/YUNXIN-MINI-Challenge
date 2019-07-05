package xyh.lixue.user.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import xyh.lixue.user.entity.*;


import java.util.List;

/**
 * @author XiangYida
 * @version 2019/5/6 19:24
 */
@Component
public interface UserMapper {
    //得到推送的HTML相关
    List<Push> getPush();
    //得到搜索记录
    List<SearchRecords> getSearchRecords(@Param("userId") String userId);
    //得到ppt
    List<PPT> getPPT();
    //插入搜索记录
    void insertSearchRecords(@Param("searchRecords") SearchRecords searchRecords);
    //插入登录记录
    void insertLoginRecords(@Param("userId") String userId);
    //获取user表中所有的记录
    List<User> getAllUser();
    //向用户表中添加数据
    void insertUser(@Param("user") User user);
}
