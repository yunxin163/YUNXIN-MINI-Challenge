package xyh.lixue.searchProblem.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyh.lixue.common.enums.SearchTypeEnum;
import xyh.lixue.common.utils.Base64Util;
import xyh.lixue.common.utils.GetToken;
import xyh.lixue.common.utils.HttpUtil;
import xyh.lixue.searchProblem.entity.Problem;
import xyh.lixue.searchProblem.mapper.SearchMapper;
import xyh.lixue.searchProblem.service.ProblemRepository;
import xyh.lixue.searchProblem.service.ProblemService;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author XiangYida
 * @version 2019/4/29 10:15
 */
@Service
@Slf4j
public class ProblemServiceImpl implements ProblemService {

    @Value("${ocr.api}")
    private String api;
    private RedisTemplate<String, String> redisTemplate;
    private ProblemRepository problemRepository;
    private SearchMapper searchMapper;

    @Autowired
    public ProblemServiceImpl(RedisTemplate<String, String> redisTemplate, ProblemRepository problemRepository, SearchMapper searchMapper) {
        this.redisTemplate = redisTemplate;
        this.problemRepository = problemRepository;
        this.searchMapper = searchMapper;
    }

    @Override
    public List<Problem> searchProblemByString(SearchTypeEnum typeEnum,String title) {
        QueryBuilder queryBuilder = new MatchQueryBuilder(typeEnum.getType(), title);
        Page<Problem> page = problemRepository.search(queryBuilder, PageRequest.of(0, 5));
        List<Problem> list = new ArrayList<>();
        //page.forEach(problem -> list.add(problem));
        for (Problem problem:page){
            list.add(problem);
        }
        return list;
    }

    @Override
    public List<Problem> searchProblemByPicture(byte[] imageData) {
        return searchProblemByString(SearchTypeEnum.TITLE,getString(ocr(imageData)));
    }

    @Override
    public void transfer() {
        problemRepository.saveAll(searchMapper.getAll());
    }

    @Override
    public Problem getProblemById(String id) {
        return searchMapper.getProblemById(id);
    }


    /**
     * 传入图片的字节数组，调用百度api返回识别后的json
     *
     * @param imgData 图片的字节数组
     * @return 识别后的json
     */
    private String ocr(byte[] imgData) {
        try {
            String imgStr = Base64Util.encode(imgData);
            String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8");
            String result = HttpUtil.post(api, this.getAccessToken(), params);
            log.info("baidu OCR return------> " + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析百度返回的json得到前几个字符
     *
     * @param json json
     * @return string
     */
    private String getString(String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("words_result");
        if(jsonArray.length()==0)return "";
        String str = (String) jsonArray.getJSONObject(0).get("words");
        log.info("get string from json------> " + str);
        return str;
    }

    /**
     * 从百度获取token放入redis中
     *
     * @return accessToken
     */
    private String setAccessToken() {
        String accessToken = GetToken.getAuth();
        redisTemplate.opsForValue().set("ACCESS_TOKEN", accessToken);
        //设置29天后失效
        this.redisTemplate.expire("ACCESS_TOKEN", 29, TimeUnit.DAYS);
        return accessToken;
    }

    /**
     * 从redis中得到token
     *
     * @return AccessToken
     */
    private String getAccessToken() {
        String accessToken = redisTemplate.opsForValue().get("ACCESS_TOKEN");
        //过期后重新获取
        if (accessToken == null) return this.setAccessToken();
        return accessToken;
    }
}
