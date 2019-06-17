package xyh.lixue.forum.service;

import xyh.lixue.forum.entity.Post;
import xyh.lixue.forum.entity.Reply;

import java.util.List;

/**
 * @author XiangYida
 * @version 2019/5/9 19:57
 */
public interface ForumService {

    /**
     * 得到所有的帖子
     * @return list
     */
    public List<Post> getAllPost();

    /**
     * 根据帖子id得到下面的回复
     * @param postId postId
     * @return list
     */
    public List<Reply> getReplyByPostId(String postId);

    /**
     * 发帖
     * @param post userId
     */
    public void posting(Post post);

    /**
     * 回复
     * @param reply reply
     */
    public void reply(Reply reply);

    /**
     * 删帖
     * @param postId postId
     */
    public void deletePost(String postId);

    /**
     * 删除回复
     * @param replyId 删除回复
     */
    public void deleteReply(String replyId);

    /**
     * 得到用户的发帖
     * @param userId userId
     * @return list
     */
    public List<Post>getPostByUserId(String userId);

    /**
     * 根据postId得到post
     * @param postId id
     * @return post
     */
    public Post getPostById(String postId);

    /**
     * 根据id得到reply
     * @param replyId id
     * @return reply
     */
    public Reply getReplyById(String replyId);

    /**
     * 根据用户id得到评论
     * @param userId userid
     * @return list
     */
    public List<Reply> getRepliesByUserId(String userId);



}
