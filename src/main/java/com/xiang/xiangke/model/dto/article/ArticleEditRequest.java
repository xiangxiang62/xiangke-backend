package com.xiang.xiangke.model.dto.article;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 编辑请求
 *
 */
@Data
public class ArticleEditRequest implements Serializable {
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
     * 文章分类
     */
    private String category;

    /**
     * 文章标签，多个标签用逗号分隔
     */
    private String tags;

    private static final long serialVersionUID = 1L;
}