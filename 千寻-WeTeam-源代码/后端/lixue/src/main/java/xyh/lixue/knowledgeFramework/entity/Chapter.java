package xyh.lixue.knowledgeFramework.entity;

import lombok.Data;

/**
 * @author XiangYida
 * @version 2019/5/6 19:46
 */
@Data
public class Chapter {
    //id
    private String id;
    //章节标题
    private String chapter;
    //对应书的id
    private String bookId;
}
