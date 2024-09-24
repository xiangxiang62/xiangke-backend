package com.xiang.xiangke.model.dto.articlelike;

import lombok.Data;

import java.io.Serializable;

/**
 * 帖子点赞 / 取消点赞请求
 *
 */
@Data
public class ArticleLikeAddRequest implements Serializable {

    /**
     * 帖子 id
     */
    private Long Id;

    private static final long serialVersionUID = 1L;
}