package com.xiang.xiangke.model.dto.article;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 */
@Data
public class ArticleAddRequest implements Serializable {

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
     * 文章标签，多个标签用逗号分隔
     */
    private String tags;

    private static final long serialVersionUID = 1L;
}