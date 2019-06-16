package xyh.lixue.forum.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author XiangYida
 * @version 2019/5/8 20:21
 * 论坛模块-回复
 */
@Data
@Document
public class Reply implements Serializable {
    //回复id
    @Id
    private String id;
    //用户id
    @Field("user_id")
    private String userId;
    //帖子id
    @Field("post_id")
    private String postId;
    //评论内容
    private String content;
    //点赞数量
    private Integer praise=0;
    //评论时间
    private long time;
}
