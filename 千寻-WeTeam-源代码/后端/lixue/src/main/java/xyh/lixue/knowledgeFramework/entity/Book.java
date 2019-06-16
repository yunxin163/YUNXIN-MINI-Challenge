package xyh.lixue.knowledgeFramework.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author XiangYida
 * @version 2019/5/6 20:26
 */
@Data
public class Book implements Serializable {
    //书id
    private String id;
    //书名
    private String bookName;
    //书的封面URL
    private String bookPictureUrl;
}
