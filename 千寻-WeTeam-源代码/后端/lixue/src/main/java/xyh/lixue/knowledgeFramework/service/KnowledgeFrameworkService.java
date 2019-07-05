package xyh.lixue.knowledgeFramework.service;


import xyh.lixue.knowledgeFramework.entity.Book;
import xyh.lixue.knowledgeFramework.entity.Chapter;
import xyh.lixue.knowledgeFramework.entity.Knowledge;

import java.util.List;

/**
 * @author XiangYida
 * @version 2019/5/6 20:42
 */
public interface KnowledgeFrameworkService {
    /**
     * 返回所有的书
     * @return books
     */
    public List<Book> getAllBooks();

    /**
     * 根据书的id返回所有章节
     * @param bookId bookId
     * @return chapters
     */
    public List<Chapter>getChaptersByBookId(String bookId);

    /**
     * 根据章节的id返回对应的知识点
     * @param chapterId chapterId
     * @return knowledge
     */
    public List<Knowledge>getKnowledgeByChapterId(String chapterId);

}
