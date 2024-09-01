package com.xiang.xiangke.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiang.xiangke.common.BaseResponse;
import com.xiang.xiangke.common.ErrorCode;
import com.xiang.xiangke.common.ResultUtils;
import com.xiang.xiangke.exception.BusinessException;
import com.xiang.xiangke.exception.ThrowUtils;
import com.xiang.xiangke.model.dto.article.ArticleQueryRequest;
import com.xiang.xiangke.model.dto.articlefavour.ArticleFavourAddRequest;
import com.xiang.xiangke.model.dto.postfavour.ArticleFavourQueryRequest;
import com.xiang.xiangke.model.entity.Article;
import com.xiang.xiangke.model.entity.User;
import com.xiang.xiangke.model.vo.ArticleVO;
import com.xiang.xiangke.service.ArticleFavourService;
import com.xiang.xiangke.service.ArticleService;
import com.xiang.xiangke.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

//import com.xiang.xiangke.model.dto.article.ArticleQueryRequest;
//import com.xiang.xiangke.model.vo.ArticleVO;
//import com.xiang.xiangke.service.ArticleService;

/**
 * 帖子收藏接口
 *
 */
@RestController
@RequestMapping("/article_favour")
@Slf4j
public class ArticleFavourController {

    @Resource
    private ArticleFavourService articleFavourService;

    @Resource
    private ArticleService articleService;

    @Resource
    private UserService userService;

    /**
     * 收藏 / 取消收藏
     *
     * @param articleFavourAddRequest
     * @param request
     * @return resultNum 收藏变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doArticleFavour(@RequestBody ArticleFavourAddRequest articleFavourAddRequest,
            HttpServletRequest request) {
        if (articleFavourAddRequest == null || articleFavourAddRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能操作
        final User loginUser = userService.getLoginUser(request);
        long articleId = articleFavourAddRequest.getId();
        int result = articleFavourService.doArticleFavour(articleId, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 获取我收藏的帖子列表
     *
     * @param articleQueryRequest
     * @param request
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<ArticleVO>> listMyFavourArticleByPage(@RequestBody ArticleQueryRequest articleQueryRequest,
                                                                   HttpServletRequest request) {
        if (articleQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        long current = articleQueryRequest.getCurrent();
        long size = articleQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Article> articlePage = articleFavourService.listFavoriteArticleByPage(new Page<>(current, size),
                articleService.getQueryWrapper(articleQueryRequest), loginUser.getId());
        return ResultUtils.success(articleService.getArticleVOPage(articlePage, request));
    }

    /**
     * 获取用户收藏的帖子列表
     *
     * @param articleFavourQueryRequest
     * @param request
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<ArticleVO>> listFavourArticleByPage(@RequestBody ArticleFavourQueryRequest articleFavourQueryRequest,
            HttpServletRequest request) {
        if (articleFavourQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = articleFavourQueryRequest.getCurrent();
        long size = articleFavourQueryRequest.getPageSize();
        Long userId = articleFavourQueryRequest.getUserId();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20 || userId == null, ErrorCode.PARAMS_ERROR);
        Page<Article> articlePage = articleFavourService.listFavoriteArticleByPage(new Page<>(current, size),
                articleService.getQueryWrapper(articleFavourQueryRequest.getArticleQueryRequest()), userId);
        return ResultUtils.success(articleService.getArticleVOPage(articlePage, request));
    }
}
