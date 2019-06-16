package xyh.lixue.knowledgeFramework.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author XiangYida
 * @version 2019/5/6 20:26
 */
@Data
public class Knowledge implements Serializable {
    //章节id
    String chapterId;
    //知识点
    String knowledge;

}
