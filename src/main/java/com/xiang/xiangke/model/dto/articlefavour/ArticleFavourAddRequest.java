package com.xiang.xiangke.model.dto.articlefavour;

import lombok.Data;

import java.io.Serializable;

/**
 * 帖子收藏 / 取消收藏请求
 *
 */
@Data
public class ArticleFavourAddRequest implements Serializable {

    /**
     * 帖子 id
     */
    private Long Id;

    private static final long serialVersionUID = 1L;
}