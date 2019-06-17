package xyh.lixue.user.entity;

import lombok.Data;

/**
 * @author XiangYida
 * @version 2019/5/8 16:57
 * 用户登录成功后返回的对象
 */
@Data
public class LoginResult {
    //用户的openId
    private String userId;

    //图片保存的Uri
    private String cosUri;

}
