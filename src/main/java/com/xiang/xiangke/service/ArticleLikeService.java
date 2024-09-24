package com.xiang.xiangke.service;

import com.xiang.xiangke.model.entity.ArticleLike;
import com.xiang.xiangke.model.entity.ArticleLike;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiang.xiangke.model.entity.User;

/**
 * 帖子点赞服务
 *
 */
public interface ArticleLikeService extends IService<ArticleLike> {

    /**
     * 点赞
     *
     * @param articleId
     * @param loginUser
     * @return
     */
    int doArticleLike(long articleId, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param articleId
     * @return
     */
    int doArticleLikeInner(long userId, long articleId);
}
