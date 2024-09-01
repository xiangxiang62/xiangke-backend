# 数据库初始化

-- 创建库
create database if not exists xiangke;

-- 切换库
use xiangke;

-- 用户表
CREATE TABLE IF NOT EXISTS `user`
(
    `id`           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    `userAccount`  VARCHAR(256)                           NOT NULL COMMENT '账号',
    `userPassword` VARCHAR(512)                           NOT NULL COMMENT '密码',
    `userEmail`    VARCHAR(256)                           NULL COMMENT '用户邮箱',
    `userName`     VARCHAR(256)                           NULL COMMENT '用户昵称',
    `userAvatar`   VARCHAR(1024)                          NULL COMMENT '用户头像',
    `userProfile`  VARCHAR(512)                           NULL COMMENT '用户简介',
    `userRole`     VARCHAR(256) DEFAULT 'user'            NOT NULL COMMENT '用户角色：user/admin/ban',
    `createTime`   DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updateTime`   DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`     TINYINT      DEFAULT 0                 NOT NULL COMMENT '是否删除',
    INDEX idx_user_name (`userName`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户表';


-- 文章表
CREATE TABLE IF NOT EXISTS `article`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文章ID',
    `title`       VARCHAR(512)                                                      NOT NULL COMMENT '文章标题',
    `content`     MEDIUMTEXT                                                        NOT NULL COMMENT '文章内容',
    `authorId`    BIGINT                                                            NOT NULL COMMENT '作者ID',
    `category`    VARCHAR(256)                                                      NULL COMMENT '文章分类',
    `tags`        VARCHAR(512)                                                      NULL COMMENT '文章标签，多个标签用逗号分隔',
    `status`      ENUM ('draft', 'published', 'archived') DEFAULT 'draft'           NOT NULL COMMENT '文章状态：草稿/已发布/归档',
    `createdTime` DATETIME                                DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updatedTime` DATETIME                                DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDeleted`   TINYINT                                 DEFAULT 0                 NOT NULL COMMENT '是否删除',
    INDEX idx_authorId (`authorId`),
    INDEX idx_category (`category`),
    FULLTEXT INDEX idx_fulltext_content (`content`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='文章表';


-- 文章点赞表
CREATE TABLE IF NOT EXISTS `article_like`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '点赞ID',
    `articleId`   BIGINT                             NOT NULL COMMENT '文章ID',
    `userId`      BIGINT                             NOT NULL COMMENT '用户ID',
    `createdTime` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '点赞时间',
    UNIQUE KEY `idx_article_user` (`articleId`, `userId`),
    FOREIGN KEY (`articleId`) REFERENCES `article` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='文章点赞表';


-- 文章收藏表
CREATE TABLE IF NOT EXISTS `article_favorite`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏ID',
    `articleId`   BIGINT                             NOT NULL COMMENT '文章ID',
    `userId`      BIGINT                             NOT NULL COMMENT '用户ID',
    `createdTime` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '收藏时间',
    UNIQUE KEY `idx_article_user` (`articleId`, `userId`),
    FOREIGN KEY (`articleId`) REFERENCES `article` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='文章收藏表';


-- 题目表
CREATE TABLE IF NOT EXISTS `question`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '题目ID',
    `title`       VARCHAR(512)                                                      NOT NULL COMMENT '题目标题',
    `description` MEDIUMTEXT                                                        NOT NULL COMMENT '题目描述',
    `authorId`    BIGINT                                                            NOT NULL COMMENT '作者ID',
    `category`    VARCHAR(256)                                                      NULL COMMENT '题目分类',
    `tags`        VARCHAR(512)                                                      NULL COMMENT '题目标签，多个标签用逗号分隔',
    `status`      ENUM ('draft', 'published', 'archived') DEFAULT 'draft'           NOT NULL COMMENT '题目状态：草稿/已发布/归档',
    `createdTime` DATETIME                                DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `updatedTime` DATETIME                                DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDeleted`   TINYINT                                 DEFAULT 0                 NOT NULL COMMENT '是否删除',
    INDEX idx_authorId (`authorId`),
    INDEX idx_category (`category`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='题目表';


-- 题目点赞表
CREATE TABLE IF NOT EXISTS `question_like`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '点赞ID',
    `questionId`  BIGINT                             NOT NULL COMMENT '题目ID',
    `userId`      BIGINT                             NOT NULL COMMENT '用户ID',
    `createdTime` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '点赞时间',
    UNIQUE KEY `idx_question_user` (`questionId`, `userId`),
    FOREIGN KEY (`questionId`) REFERENCES `question` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='题目点赞表';


-- 题目收藏表
CREATE TABLE IF NOT EXISTS `question_favorite`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '收藏ID',
    `questionId`  BIGINT                             NOT NULL COMMENT '题目ID',
    `userId`      BIGINT                             NOT NULL COMMENT '用户ID',
    `createdTime` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '收藏时间',
    UNIQUE KEY `idx_question_user` (`questionId`, `userId`),
    FOREIGN KEY (`questionId`) REFERENCES `question` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='题目收藏表';


-- 文章评论表
CREATE TABLE IF NOT EXISTS `article_comment`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    `articleId`   BIGINT                             NOT NULL COMMENT '文章ID',
    `authorId`    BIGINT                             NOT NULL COMMENT '评论作者ID',
    `content`     TEXT                               NOT NULL COMMENT '评论内容',
    `createdTime` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `isDeleted`   TINYINT  DEFAULT 0                 NOT NULL COMMENT '是否删除',
    FOREIGN KEY (`articleId`) REFERENCES `article` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`authorId`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='文章评论表';

-- 题目评论表
CREATE TABLE IF NOT EXISTS `question_comment`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    `questionId`  BIGINT                             NOT NULL COMMENT '题目ID',
    `authorId`    BIGINT                             NOT NULL COMMENT '评论作者ID',
    `content`     TEXT                               NOT NULL COMMENT '评论内容',
    `createdTime` DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `isDeleted`   TINYINT  DEFAULT 0                 NOT NULL COMMENT '是否删除',
    FOREIGN KEY (`questionId`) REFERENCES `question` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`authorId`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='题目评论表';
