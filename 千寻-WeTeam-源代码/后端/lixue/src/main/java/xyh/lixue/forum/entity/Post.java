package xyh.lixue.forum.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author XiangYida
 * @version 2019/5/8 20:12
 * 论坛模块-帖子
 */
@Data
@Document
public class Post implements Serializable {
    //帖子Id
    @Id
    private String id;
    //发帖人的userId
    @Field("user_id")
    private String userId;
    //内容
    private String content;
    //浏览量
    @Field("page_view")
    private Integer pageView=0;
    //发帖时间
    private long time;
    //暂时先不考虑图片，后面再迭代
}
