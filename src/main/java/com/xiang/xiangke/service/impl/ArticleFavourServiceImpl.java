package com.xiang.xiangke.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiang.xiangke.common.ErrorCode;
import com.xiang.xiangke.exception.BusinessException;
import com.xiang.xiangke.mapper.ArticleFavoriteMapper;
import com.xiang.xiangke.model.entity.Article;
import com.xiang.xiangke.model.entity.ArticleFavorite;
import com.xiang.xiangke.model.entity.User;
import com.xiang.xiangke.service.ArticleFavourService;
import com.xiang.xiangke.service.ArticleService;
import javax.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 帖子收藏服务实现
 *
 */
@Service
public class ArticleFavourServiceImpl extends ServiceImpl<ArticleFavoriteMapper, ArticleFavorite>
        implements ArticleFavourService {

    @Resource
    private ArticleService articleService;

    /**
     * 帖子收藏
     *
     * @param articleId
     * @param loginUser
     * @return
     */
    @Override
    public int doArticleFavour(long articleId, User loginUser) {
        // 判断是否存在
        Article article = articleService.getById(articleId);
        if (article == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已帖子收藏
        long userId = loginUser.getId();
        // 每个用户串行帖子收藏
        // 锁必须要包裹住事务方法
        ArticleFavourService articleFavourService = (ArticleFavourService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return articleFavourService.doArticleFavoriteInner(userId, articleId);
        }
    }

    @Override
    public Page<Article> listFavoriteArticleByPage(IPage<Article> page, Wrapper<Article> queryWrapper, long favourUserId) {
        if (favourUserId <= 0) {
            return new Page<>();
        }
        return baseMapper.listFavoritePostByPage(page, queryWrapper, favourUserId);
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param articleId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doArticleFavoriteInner(long userId, long articleId) {
        ArticleFavorite articleFavour = new ArticleFavorite();
        articleFavour.setUserId(userId);
        articleFavour.setArticleId(articleId);
        QueryWrapper<ArticleFavorite> articleFavourQueryWrapper = new QueryWrapper<>(articleFavour);
        ArticleFavorite oldArticleFavorite = this.getOne(articleFavourQueryWrapper);
        boolean result;
        // 已收藏
        if (oldArticleFavorite != null) {
            result = this.remove(articleFavourQueryWrapper);
            if (result) {
                // 帖子收藏数 - 1
                result = articleService.update()
                        .eq("id", articleId)
                        .gt("favourNum", 0)
                        .setSql("favourNum = favourNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未帖子收藏
            result = this.save(articleFavour);
            if (result) {
                // 帖子收藏数 + 1
                result = articleService.update()
                        .eq("id", articleId)
                        .setSql("favourNum = favourNum + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }

}




