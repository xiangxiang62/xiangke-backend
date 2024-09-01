package com.xiang.xiangke.model.dto.postfavour;

import com.xiang.xiangke.common.PageRequest;
import com.xiang.xiangke.model.dto.article.ArticleQueryRequest;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 帖子收藏查询请求
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleFavourQueryRequest extends PageRequest implements Serializable {

    /**
     * 帖子查询请求
     */
    private ArticleQueryRequest ArticleQueryRequest;

    /**
     * 用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}