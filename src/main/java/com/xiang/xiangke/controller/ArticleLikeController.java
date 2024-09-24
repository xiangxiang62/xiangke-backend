package com.xiang.xiangke.controller;

import com.xiang.xiangke.common.BaseResponse;
import com.xiang.xiangke.common.ErrorCode;
import com.xiang.xiangke.common.ResultUtils;
import com.xiang.xiangke.exception.BusinessException;
import com.xiang.xiangke.model.dto.articlelike.ArticleLikeAddRequest;
import com.xiang.xiangke.model.entity.User;
import com.xiang.xiangke.service.ArticleLikeService;
import com.xiang.xiangke.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子点赞接口
 *
 */
@RestController
@RequestMapping("/article_like")
@Slf4j
public class ArticleLikeController {

    @Resource
    private ArticleLikeService articleLikeService;

    @Resource
    private UserService userService;

    /**
     * 点赞 / 取消点赞
     *
     * @param articleLikeAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doThumb(@RequestBody ArticleLikeAddRequest articleLikeAddRequest,
            HttpServletRequest request) {
        if (articleLikeAddRequest == null || articleLikeAddRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long articleId = articleLikeAddRequest.getId();
        int result = articleLikeService.doArticleLike(articleId, loginUser);
        return ResultUtils.success(result);
    }

}
