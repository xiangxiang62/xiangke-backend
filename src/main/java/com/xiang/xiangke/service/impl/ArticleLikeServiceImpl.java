package com.xiang.xiangke.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiang.xiangke.common.ErrorCode;
import com.xiang.xiangke.exception.BusinessException;
import com.xiang.xiangke.mapper.ArticleLikeMapper;
import com.xiang.xiangke.mapper.ArticleLikeMapper;
import com.xiang.xiangke.model.entity.Article;
import com.xiang.xiangke.model.entity.ArticleLike;
import com.xiang.xiangke.model.entity.User;
import com.xiang.xiangke.service.ArticleLikeService;
import com.xiang.xiangke.service.ArticleService;
import com.xiang.xiangke.service.ArticleLikeService;
import javax.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 帖子点赞服务实现
 *
 */
@Service
public class ArticleLikeServiceImpl extends ServiceImpl<ArticleLikeMapper, ArticleLike>
        implements ArticleLikeService {

    @Resource
    private ArticleService articleService;

    /**
     * 点赞
     *
     * @param articleId
     * @param loginUser
     * @return
     */
    @Override
    public int doArticleLike(long articleId, User loginUser) {
        // 判断实体是否存在，根据类别获取实体
        Article article = articleService.getById(articleId);
        if (article == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已点赞
        long userId = loginUser.getId();
        // 每个用户串行点赞
        // 锁必须要包裹住事务方法
        ArticleLikeService articleLikeService = (ArticleLikeService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return articleLikeService.doArticleLikeInner(userId, articleId);
        }
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
    public int doArticleLikeInner(long userId, long articleId) {
        ArticleLike articleLike = new ArticleLike();
        articleLike.setUserId(userId);
        articleLike.setArticleId(articleId);
        QueryWrapper<ArticleLike> thumbQueryWrapper = new QueryWrapper<>(articleLike);
        ArticleLike oldArticleLike = this.getOne(thumbQueryWrapper);
        boolean result;
        // 已点赞
        if (oldArticleLike != null) {
            result = this.remove(thumbQueryWrapper);
            if (result) {
                // 点赞数 - 1
                result = articleService.update()
                        .eq("id", articleId)
                        .gt("LikeNum", 0)
                        .setSql("LikeNum = LikeNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            result = this.save(articleLike);
            if (result) {
                // 点赞数 + 1
                result = articleService.update()
                        .eq("id", articleId)
                        .setSql("LikeNum = LikeNum + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }

}




