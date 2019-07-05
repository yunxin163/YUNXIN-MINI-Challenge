package xyh.lixue.searchProblem.service;

import org.elasticsearch.action.search.SearchType;
import xyh.lixue.common.enums.SearchTypeEnum;
import xyh.lixue.searchProblem.entity.Problem;

import java.util.List;


public interface ProblemService {
    /**
     * 文本搜题
     * @param title  标题
     * @return 题目
     */
    List<Problem> searchProblemByString(SearchTypeEnum typeEnum,String title);


    /**
     * 拍照搜题
     * @param imageData 图片的byte[]
     * @return 题
     */
    List<Problem> searchProblemByPicture(byte[] imageData);

    /**
     * 将MySQL中的数据导入到elasticsearch中
     */
    void transfer();

    /**
     * 根据题目id返回题目
     * @param id 题目id
     * @return 题目
     */
    Problem getProblemById(String id);



}
