package com.xiang.xiangke.model.dto.article;

import com.xiang.xiangke.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleQueryRequest extends PageRequest implements Serializable {
    /**
     * 文章ID
     */
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
     * 文章分类
     */
    private String category;

    /**
     * 文章简介
     */
    private String concise;

    /**
     * 文章标签，多个标签用逗号分隔
     */
    private String tags;

    /**
     * 文章状态：草稿/已发布/归档
     */
    private Object status;



    private static final long serialVersionUID = 1L;
}