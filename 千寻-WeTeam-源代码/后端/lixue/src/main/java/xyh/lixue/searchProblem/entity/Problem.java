package xyh.lixue.searchProblem.entity;

import lombok.Data;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * @author XiangYida
 * @version 2019/5/4 17:51
 * 题目信息
 */

@Document(indexName = "lixue",type = "problem")
@Data
public class Problem implements Serializable {
    //题目id
    @Id
    private String id;
    @Field(type = FieldType.Text, searchAnalyzer = "ik_smart", analyzer = "ik_max_word")
    private String knowledgePoint;
    //题目标题
    @Field(type = FieldType.Text, searchAnalyzer = "ik_smart", analyzer = "ik_max_word")
    private String title;
    //出版社
    private String publisher;
    //题目图片名字
    private String problemPictureName;
    //答案图片名字
    private String answerPictureName;
    //热度
    private int hotPoint;
}
