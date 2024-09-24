package com.xiang.xiangke.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiang.xiangke.common.ErrorCode;
import com.xiang.xiangke.constant.CommonConstant;
import com.xiang.xiangke.exception.BusinessException;
import com.xiang.xiangke.exception.ThrowUtils;
//import com.xiang.xiangke.mapper.ArticleFavourMapper;
import com.xiang.xiangke.mapper.ArticleFavoriteMapper;
import com.xiang.xiangke.mapper.ArticleLikeMapper;
import com.xiang.xiangke.mapper.ArticleMapper;
//import com.xiang.xiangke.mapper.ArticleThumbMapper;
import com.xiang.xiangke.model.dto.article.ArticleQueryRequest;
import com.xiang.xiangke.model.entity.Article;
//import com.xiang.xiangke.model.entity.ArticleFavour;
//import com.xiang.xiangke.model.entity.ArticleThumb;
import com.xiang.xiangke.model.entity.ArticleFavorite;
import com.xiang.xiangke.model.entity.ArticleLike;
import com.xiang.xiangke.model.entity.User;
import com.xiang.xiangke.model.vo.ArticleVO;
import com.xiang.xiangke.model.vo.UserVO;
import com.xiang.xiangke.service.ArticleService;
import com.xiang.xiangke.service.UserService;
import com.xiang.xiangke.utils.SqlUtils;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 帖子服务实现
 *
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private UserService userService;

    @Resource
    private ArticleLikeMapper articleLikeMapper;

    @Resource
    private ArticleFavoriteMapper articleFavoriteMapper;



    @Override
    public void validArticle(Article article, boolean add) {
        if (article == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = article.getTitle();
        String content = article.getContent();
        String tags = article.getTags();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }

    /**
     * 获取查询包装类
     *
     * @param articleQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Article> getQueryWrapper(ArticleQueryRequest articleQueryRequest) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        if (articleQueryRequest == null) {
            return queryWrapper;
        }

        String sortField = articleQueryRequest.getSortField();
        String sortOrder = articleQueryRequest.getSortOrder();
        Long id = articleQueryRequest.getId();
        String title = articleQueryRequest.getTitle();
        String content = articleQueryRequest.getContent();
        Long userId = articleQueryRequest.getAuthorId();
        String category = articleQueryRequest.getCategory();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(category), "category", category);

        // 处理排序逻辑
        if (StringUtils.isNotBlank(sortField)) {
            // 如果 sortField 是空的或 null，则使用 createdTime 进行默认排序
            if (sortField.equals("createdTime") && StringUtils.isBlank(sortOrder)) {
                // 默认倒序
                queryWrapper.orderByDesc("createdTime");
            } else {
                // 使用传入的排序字段和排序顺序
                boolean isAsc = CommonConstant.SORT_ORDER_ASC.equals(sortOrder);
                queryWrapper.orderBy(true, isAsc, sortField);
            }
        } else {
            // 如果没有指定排序字段，则使用 createdTime 进行默认排序
            queryWrapper.orderByDesc("createdTime");
        }

        return queryWrapper;
    }



    @Override
    public ArticleVO getArticleVO(Article article, HttpServletRequest request) {
        ArticleVO articleVO = ArticleVO.objToVo(article);
        long articleId = article.getId();
        // 1. 关联查询用户信息
        Long userId = article.getAuthorId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        articleVO.setUser(userVO);
        articleVO.setCreatedTime(article.getCreatedTime());
        // 2. 已登录，获取用户点赞、收藏状态
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            // 获取点赞
//            QueryWrapper<ArticleThumb> articleThumbQueryWrapper = new QueryWrapper<>();
//            articleThumbQueryWrapper.in("articleId", articleId);
//            articleThumbQueryWrapper.eq("userId", loginUser.getId());
//            ArticleThumb articleThumb = articleThumbMapper.selectOne(articleThumbQueryWrapper);
//            articleVO.setHasThumb(articleThumb != null);
            // 获取收藏
            QueryWrapper<ArticleFavorite> articleFavourQueryWrapper = new QueryWrapper<>();
            articleFavourQueryWrapper.in("articleId", articleId);
            articleFavourQueryWrapper.eq("userId", loginUser.getId());
            ArticleFavorite articleFavour = articleFavoriteMapper.selectOne(articleFavourQueryWrapper);
            articleVO.setStarred(articleFavour != null);

            // 获取点赞
            QueryWrapper<ArticleLike> articleLikeQueryWrapper = new QueryWrapper<>();
            articleLikeQueryWrapper.in("articleId", articleId);
            articleLikeQueryWrapper.eq("userId", loginUser.getId());
            ArticleLike articleLike = articleLikeMapper.selectOne(articleLikeQueryWrapper);
            articleVO.setStarred(articleFavour != null);
            articleVO.setLiked(articleLike != null);
        }
        return articleVO;
    }

    @Override
    public Page<ArticleVO> getArticleVOPage(Page<Article> articlePage, HttpServletRequest request) {
        List<Article> articleList = articlePage.getRecords();
        Page<ArticleVO> articleVOPage = new Page<>(articlePage.getCurrent(), articlePage.getSize(), articlePage.getTotal());
        if (CollUtil.isEmpty(articleList)) {
            return articleVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = articleList.stream().map(Article::getAuthorId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> articleIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> articleIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
//        if (loginUser != null) {
//            Set<Long> articleIdSet = articleList.stream().map(Article::getId).collect(Collectors.toSet());
//            loginUser = userService.getLoginUser(request);
//            // 获取点赞
//            QueryWrapper<ArticleThumb> articleThumbQueryWrapper = new QueryWrapper<>();
//            articleThumbQueryWrapper.in("articleId", articleIdSet);
//            articleThumbQueryWrapper.eq("userId", loginUser.getId());
//            List<ArticleThumb> articleArticleThumbList = articleThumbMapper.selectList(articleThumbQueryWrapper);
//            articleArticleThumbList.forEach(articleArticleThumb -> articleIdHasThumbMap.put(articleArticleThumb.getArticleId(), true));
//            // 获取收藏
//            QueryWrapper<ArticleFavour> articleFavourQueryWrapper = new QueryWrapper<>();
//            articleFavourQueryWrapper.in("articleId", articleIdSet);
//            articleFavourQueryWrapper.eq("userId", loginUser.getId());
//            List<ArticleFavour> articleFavourList = articleFavourMapper.selectList(articleFavourQueryWrapper);
//            articleFavourList.forEach(articleFavour -> articleIdHasFavourMap.put(articleFavour.getArticleId(), true));
//        }
        // 填充信息
        List<ArticleVO> articleVOList = articleList.stream().map(article -> {
            ArticleVO articleVO = ArticleVO.objToVo(article);
            Long userId = article.getAuthorId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            articleVO.setUser(userService.getUserVO(user));
//            articleVO.setHasThumb(articleIdHasThumbMap.getOrDefault(article.getId(), false));
//            articleVO.setHasFavour(articleIdHasFavourMap.getOrDefault(article.getId(), false));
            return articleVO;
        }).collect(Collectors.toList());
        articleVOPage.setRecords(articleVOList);
        return articleVOPage;
    }

}




