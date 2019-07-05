package xyh.lixue.user.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyh.lixue.common.result.ApiResult;
import xyh.lixue.common.result.ResultUtil;
import xyh.lixue.user.entity.LoginResult;
import xyh.lixue.user.entity.PPT;
import xyh.lixue.user.entity.Push;
import xyh.lixue.user.entity.SearchRecords;
import xyh.lixue.user.service.UserService;

import java.util.List;

/**
 * @author XiangYida
 * @version 2019/5/6 21:23
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Value("${cos.uri}")
    private String cosUri;
    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    @RequestMapping("/ppts")
    public ApiResult<List<PPT>>getPPT(){
       return ResultUtil.success(userService.getPPT());
    }

    @RequestMapping("/searchRecords/{userId}")
    public ApiResult<List<SearchRecords>>getSearchRecords(@PathVariable String userId){
        return ResultUtil.success(userService.getSearchRecords(userId));
    }

    @RequestMapping("/push")
    public ApiResult<List<Push>>getPush(){
        return ResultUtil.success(userService.pushHtml());
    }

    @RequestMapping("/login/{code}")
    public ApiResult<LoginResult> login(@PathVariable String code){
        //获取openid
        String userId=userService.getOpenId(code);
        //记录登陆
        userService.recordLogin(userId);
        LoginResult loginResult=new LoginResult();
        //返回用户的openId
        loginResult.setUserId(userId);
        //返回云对象存储的uri，后面加装图片会用到
        loginResult.setCosUri(this.cosUri);
        return ResultUtil.success(loginResult);
    }

    @RequestMapping("/recordSearch")
    public ApiResult recordSearch(String userId,String problemId){
        SearchRecords searchRecords=new SearchRecords();
        searchRecords.setUserId(userId);
        searchRecords.setProblemId(problemId);
        userService.recordSearch(searchRecords);
        return ResultUtil.success();
    }



}
