package com.xiang.xiangke.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文章表
 * @TableName article
 */
@TableName(value ="article")
@Data
public class Article implements Serializable {
    /**
     * 文章ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 作者ID
     */
    private Long authorId;

    /**
     * 文章简介
     */
    private String concise;

    /**
     * 文章分类
     */
    private String category;

    /**
     * 文章标签，多个标签用逗号分隔
     */
    private String tags;

    /**
     * 文章状态：草稿/已发布/归档
     */
    private Object status;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 点赞数
     */
    private Integer likeNum;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}