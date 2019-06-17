package xyh.lixue.user.service;

import xyh.lixue.user.entity.PPT;
import xyh.lixue.user.entity.Push;
import xyh.lixue.user.entity.SearchRecords;

import java.util.List;

/**
 * @author XiangYida
 * @version 2019/5/6 19:13
 */
public interface UserService {
    /**
     * 向用户推送文章
     */
    List<Push> pushHtml();

    /**
     * 用户通过userId查询搜题记录
     * @param userId userId
     * @return List
     */
    List<SearchRecords> getSearchRecords(String userId);

    /**
     * 根据用户的code去获取openId
     * @param code
     * @return
     */
    String getOpenId(String code);

    /**
     * 返回ppt信息
     * @return ppt
     */
    List<PPT> getPPT();

    /**
     * 记录搜题
     * @param searchRecords
     */
    void  recordSearch(SearchRecords searchRecords);

    /**
     * 记录用户登录
     * @param userId userId
     */
    void recordLogin(String userId);

    /**
     * 将用户表中的数据加载到redis中
     * 在启动完成后系统会调用
     */
    void importUserToRedis();

}
