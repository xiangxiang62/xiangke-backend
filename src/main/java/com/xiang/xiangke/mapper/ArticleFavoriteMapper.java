package com.xiang.xiangke.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiang.xiangke.model.entity.Article;
import com.xiang.xiangke.model.entity.ArticleFavorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiang.xiangke.model.entity.Post;
import org.apache.ibatis.annotations.Param;

/**
* @author 73148
* @description 针对表【article_favorite(文章收藏表)】的数据库操作Mapper
* @createDate 2024-08-31 15:20:51
* @Entity com.xiang.xiangke.model.entity.ArticleFavorite
*/
public interface ArticleFavoriteMapper extends BaseMapper<ArticleFavorite> {

    /**
     * 分页查询收藏帖子列表
     *
     * @param page
     * @param queryWrapper
     * @param favourUserId
     * @return
     */
    Page<Article> listFavoritePostByPage(IPage<Article> page, @Param(Constants.WRAPPER) Wrapper<Article> queryWrapper,
                                         long favourUserId);

}




