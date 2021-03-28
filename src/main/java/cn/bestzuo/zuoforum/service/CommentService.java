package cn.bestzuo.zuoforum.service;

import cn.bestzuo.zuoforum.pojo.Comment;
import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.pojo.UserInfo;

import java.util.List;

public interface CommentService {

    /**
     * 插入一条评论
     * @param comment
     * @return
     */
    Comment insertCommentByQuestionId(UserInfo userInfo, String comment, Question question);

    /**
     * 根据问题ID查询评论信息
     * @param questionId
     * @return
     */
    List<Comment> queryCommentByQuestionId(Integer questionId);

    /**
     * 根据评论者查询评论信息
     */
    List<Comment> selectCommentsByUname(String uname);

    /**
     * 根据评论ID查询评论信息
     * @param id
     * @return
     */
    Comment selectCommentByPrimaryKey(Integer id);
}
