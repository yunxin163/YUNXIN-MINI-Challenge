package xyh.lixue.forum.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyh.lixue.common.enums.ResultEnum;
import xyh.lixue.common.result.ApiResult;
import xyh.lixue.common.result.ResultUtil;
import xyh.lixue.forum.entity.Post;
import xyh.lixue.forum.entity.Reply;
import xyh.lixue.forum.service.ForumService;
import java.util.List;
import java.util.UUID;

/**
 * @author XiangYida
 * @version 2019/5/9 21:48
 */
@RestController
@Slf4j
@RequestMapping("/forum")
public class ForumController {

    private ForumService forumService;

    @Autowired
    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @RequestMapping("/posts")
    public ApiResult<List<Post>> getAllPost() {
        return ResultUtil.success(forumService.getAllPost());
    }

    @PostMapping(value = "/posting")
    public ApiResult posting(Post post) {
        post.setId(UUID.randomUUID().toString());
        post.setTime(System.currentTimeMillis());
        forumService.posting(post);
        return ResultUtil.success();
    }

    @PostMapping("/reply")
    public ApiResult reply(Reply reply) {
        reply.setTime(System.currentTimeMillis());
        forumService.reply(reply);
        return ResultUtil.success();
    }

    @RequestMapping("/replies/{postId}")
    public ApiResult<List<Reply>> getReplyByPostId(@PathVariable String postId) {
        List<Reply> list = forumService.getReplyByPostId(postId);
        return ResultUtil.success(list);
    }

    @RequestMapping("/deletePost")
    public ApiResult deletePost(String postId, String userId) {
        if (!forumService.getPostById(postId).getUserId().equals(userId)) return ResultUtil.failed(ResultEnum.AUTHORITY_ERR);
        forumService.deletePost(postId);
        return ResultUtil.success();
    }

    @RequestMapping("/deleteReply")
    public ApiResult deleteReply(String replyId, String userId) {
        if (!forumService.getReplyById(replyId).getUserId().equals(userId))return ResultUtil.failed(ResultEnum.AUTHORITY_ERR);
        forumService.deleteReply(replyId);
        return ResultUtil.success();
    }

    @RequestMapping("userPosts/{userId}")
    public ApiResult<List<Post>> getPostsByUserId(@PathVariable String userId){
        List<Post> list=forumService.getPostByUserId(userId);
        return ResultUtil.success(list);
    }

    @RequestMapping("/userReplies/{userId}")
    public ApiResult<List<Reply>> getRepliesByUserId(@PathVariable String userId){
        List<Reply> list=forumService.getRepliesByUserId(userId);
        return ResultUtil.success(list);
    }


}
