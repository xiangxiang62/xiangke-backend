package com.xiang.xiangke.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xiang.xiangke.model.entity.Article;
import com.xiang.xiangke.model.entity.Post;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.xiang.xiangke.model.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 帖子视图
 *
 */
@Data
public class ArticleVO implements Serializable {

    /**
     * 文章ID
     */
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 作者ID
     */
    private Long authorId;

    /**
     * 文章分类
     */
    private String category;

    /**
     * 文章标签，多个标签用逗号分隔
     */
    private String tags;


    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 点赞数
     */
    private Integer likeNum;

    private UserVO user;

    private Boolean starred;

    /**
     * 包装类转对象
     *
     * @param articleVO
     * @return
     */
    public static Article voToObj(ArticleVO articleVO) {
        if (articleVO == null) {
            return null;
        }
        Article article = new Article();
        BeanUtils.copyProperties(articleVO, article);
        List<String> tagList = Collections.singletonList(articleVO.getTags());
        article.setTags(JSONUtil.toJsonStr(tagList));
        return article;
    }

    /**
     * 对象转包装类
     *
     * @param article
     * @return
     */
    public static ArticleVO objToVo(Article article) {
        if (article == null) {
            return null;
        }
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article, articleVO);
        articleVO.setTags(article.getTags());
        return articleVO;
    }
}
