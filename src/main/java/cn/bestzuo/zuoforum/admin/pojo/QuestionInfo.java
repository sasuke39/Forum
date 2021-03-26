package cn.bestzuo.zuoforum.admin.pojo;

import lombok.Data;


@Data
public class QuestionInfo {
    //问题ID
    private int id;

    //问题标题
    private String title;

    //问题发起人
    private String publisher;

    //问题详情
    private String description;

    //问题发布时间
    private String gmtCreate;

    //问题最后编辑日期
    private String gmtModified;

    //评论数
    private int commentCount;

    //浏览数
    private int viewCount;

    //问题标签
    private String tag;

    //问题是否加精
    private String isJing;

    //问题是否置顶
    private String isDing;
}
