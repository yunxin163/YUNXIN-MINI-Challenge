package xyh.lixue.knowledgeFramework.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import xyh.lixue.knowledgeFramework.entity.Book;
import xyh.lixue.knowledgeFramework.entity.Chapter;
import xyh.lixue.knowledgeFramework.entity.Knowledge;

import java.util.List;

/**
 * @author XiangYida
 * @version 2019/5/6 19:24
 */
@Component
public interface KnowledgeMapper {
    //返回所有的书
    public List<Book>getAllBooks();

    //根据书的id返回所有章节
    public List<Chapter>getChaptersByBookId(@Param("bookId") String bookId);

    //根据章节的id返回对应的知识点
    public List<Knowledge>getKnowledgeByChapterId(@Param("chapterId") String chapterId);

}
