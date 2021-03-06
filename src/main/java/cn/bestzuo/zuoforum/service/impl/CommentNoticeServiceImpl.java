package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.CommentNoticeInfoMapper;
import cn.bestzuo.zuoforum.mapper.QuestionMapper;
import cn.bestzuo.zuoforum.mapper.UserInfoMapper;
import cn.bestzuo.zuoforum.pojo.CommentNoticeInfo;
import cn.bestzuo.zuoforum.pojo.Question;
import cn.bestzuo.zuoforum.pojo.UserInfo;
import cn.bestzuo.zuoforum.pojo.vo.CommentNoticeVO;
import cn.bestzuo.zuoforum.service.CommentNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论通知Service
 */
@Service
public class CommentNoticeServiceImpl implements CommentNoticeService {

    @Autowired
    private CommentNoticeInfoMapper commentNoticeInfoMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private QuestionMapper questionMapper;


    @Override
    @Transactional
    public int insertCommentNoticeInfo(CommentNoticeInfo record) {
        return commentNoticeInfoMapper.insert(record);
    }

    /**
     * 根据被通知者用户名查询通知消息
     * @param noticeName
     * @return
     */
    @Override
    public List<CommentNoticeInfo> selectCommentNoticeByName(String noticeName) {
        return commentNoticeInfoMapper.selectCommentNoticeByName(noticeName);
    }

    /**
     * 将评论信息转换成前端展示的VO
     *
     * @param commentNoticeInfo
     * @return
     */
    private CommentNoticeVO convertCommentNoticeToVO(CommentNoticeInfo commentNoticeInfo) {
        CommentNoticeVO commentNoticeVO = new CommentNoticeVO();
        commentNoticeVO.setId(commentNoticeInfo.getId());

        //获取回复者头像
        UserInfo info = userInfoMapper.selectUserInfoByName(commentNoticeInfo.getCommentName());
        commentNoticeVO.setCommentAvatar(info.getAvatar());
        commentNoticeVO.setParentCommentId(commentNoticeInfo.getParentCommentId());
        commentNoticeVO.setUsername(commentNoticeInfo.getCommentName());
        commentNoticeVO.setContent(commentNoticeInfo.getContent());
        commentNoticeVO.setStatus(commentNoticeInfo.getStatus());
        commentNoticeVO.setQuestionId(commentNoticeInfo.getQuestionId());
        commentNoticeVO.setTime(commentNoticeInfo.getTime());

        //获取文章标题
        Question question = questionMapper.selectByPrimaryKey(commentNoticeInfo.getQuestionId());
        if (question.getTitle().length() > 20) {
            commentNoticeVO.setTitle(question.getTitle().substring(0, 20) + "...");
        } else {
            commentNoticeVO.setTitle(question.getTitle());
        }
        return commentNoticeVO;
    }
}
