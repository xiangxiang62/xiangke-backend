package com.xiang.xiangke.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiang.xiangke.model.dto.article.ArticleQueryRequest;
import com.xiang.xiangke.model.entity.Article;
import com.xiang.xiangke.model.vo.ArticleVO;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子服务
 *
 */
public interface ArticleService extends IService<Article> {

    /**
     * 校验
     *
     * @param article
     * @param add
     */
    void validArticle(Article article, boolean add);

    /**
     * 获取查询条件
     *
     * @param articleQueryRequest
     * @return
     */
    QueryWrapper<Article> getQueryWrapper(ArticleQueryRequest articleQueryRequest);

    /**
     * 获取帖子封装
     *
     * @param article
     * @param request
     * @return
     */
    ArticleVO getArticleVO(Article article, HttpServletRequest request);

    /**
     * 分页获取帖子封装
     *
     * @param articlePage
     * @param request
     * @return
     */
    Page<ArticleVO> getArticleVOPage(Page<Article> articlePage, HttpServletRequest request);
}
