package xyh.lixue.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author XiangYida
 * @version 2019/5/6 7:54
 * 用户登录记录
 */
@Data
public class LoginRecords implements Serializable {
    //用户id
    private String userId;
    //登录时间
    private String time;

}
