package xyh.lixue.user.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author XiangYida
 * @version 2019/5/4 17:51
 * PPT相关信息
 */
@Data
public class PPT implements Serializable {
    //对应书的id
    private Integer bookId;
    //章节
    private Integer chapterId;
    //章节title
    private String title;
    //图片url
    private String avatar;
    //duration
    private Integer duration;

}
