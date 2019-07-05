package xyh.lixue.knowledgeFramework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyh.lixue.knowledgeFramework.entity.Book;
import xyh.lixue.knowledgeFramework.entity.Chapter;
import xyh.lixue.knowledgeFramework.entity.Knowledge;
import xyh.lixue.knowledgeFramework.mapper.KnowledgeMapper;
import xyh.lixue.knowledgeFramework.service.KnowledgeFrameworkService;

import java.util.List;

/**
 * @author XiangYida
 * @version 2019/5/6 20:47
 */
@Service
public class KnowledgeFrameworkServiceImpl implements KnowledgeFrameworkService {

    private KnowledgeMapper knowledgeMapper;

    @Autowired
    public KnowledgeFrameworkServiceImpl(KnowledgeMapper knowledgeMapper){
        this.knowledgeMapper=knowledgeMapper;
    }

    @Override
    public List<Book> getAllBooks() {
        return knowledgeMapper.getAllBooks();
    }

    @Override
    public List<Chapter> getChaptersByBookId(String bookId) {
        return knowledgeMapper.getChaptersByBookId(bookId);
    }

    @Override
    public List<Knowledge> getKnowledgeByChapterId(String chapterId) {
        return knowledgeMapper.getKnowledgeByChapterId(chapterId);
    }
}
