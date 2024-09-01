package com.xiang.xiangke.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiang.xiangke.model.entity.Article;
import com.xiang.xiangke.model.entity.ArticleFavorite;
import com.xiang.xiangke.model.entity.User;

/**
 * 帖子收藏服务
 *
 */
public interface ArticleFavourService extends IService<ArticleFavorite> {

    /**
     * 帖子收藏
     *
     * @param articleId
     * @param loginUser
     * @return
     */
    int doArticleFavour(long articleId, User loginUser);

    /**
     * 分页获取用户收藏的帖子列表
     *
     * @param page
     * @param queryWrapper
     * @param favourUserId
     * @return
     */
    Page<Article> listFavoriteArticleByPage(IPage<Article> page, Wrapper<Article> queryWrapper,
            long favourUserId);

    /**
     * 帖子收藏（内部服务）
     *
     * @param userId
     * @param articleId
     * @return
     */
    int doArticleFavoriteInner(long userId, long articleId);
}
