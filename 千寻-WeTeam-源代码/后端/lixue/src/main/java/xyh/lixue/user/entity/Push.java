package xyh.lixue.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author XiangYida
 * @version 2019/5/6 7:49
 * 推送文章的HTML
 */
@Data
public class Push implements Serializable {
    //图片的url
    private String imgUrl;
    //html的url
    private String url;
    //文章的标题
    private String title;
}
