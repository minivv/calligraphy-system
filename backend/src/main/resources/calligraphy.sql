-- 书法交流活动管理系统数据库脚本
CREATE DATABASE IF NOT EXISTS calligraphy DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE calligraphy;

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码(MD5)',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `gender` VARCHAR(10) DEFAULT NULL COMMENT '性别',
    `status` INT DEFAULT 1 COMMENT '状态 1-正常 0-禁用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 管理员表
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码(MD5)',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `role` VARCHAR(20) NOT NULL DEFAULT 'ADMIN' COMMENT '角色 ADMIN-管理员 ACTIVITY_ADMIN-活动管理员',
    `status` INT DEFAULT 1 COMMENT '状态 1-正常 0-禁用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 书法作品表
DROP TABLE IF EXISTS `calligraphy_work`;
CREATE TABLE `calligraphy_work` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(200) NOT NULL COMMENT '作品标题',
    `description` TEXT COMMENT '作品描述',
    `image_url` VARCHAR(500) DEFAULT NULL COMMENT '作品图片',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '书法类别(楷书/行书/草书/隶书/篆书)',
    `user_id` BIGINT NOT NULL COMMENT '作者ID',
    `status` INT DEFAULT 1 COMMENT '状态 1-正常 0-下架',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `dislike_count` INT DEFAULT 0 COMMENT '踩数',
    `comment_count` INT DEFAULT 0 COMMENT '评论数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书法作品表';

-- 书法交流活动表
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(200) NOT NULL COMMENT '活动标题',
    `description` TEXT COMMENT '活动描述',
    `cover_image` VARCHAR(500) DEFAULT NULL COMMENT '封面图片',
    `location` VARCHAR(200) DEFAULT NULL COMMENT '活动地点',
    `max_participants` INT DEFAULT 0 COMMENT '最大参与人数',
    `current_participants` INT DEFAULT 0 COMMENT '当前参与人数',
    `status` INT DEFAULT 0 COMMENT '状态 0-未开始 1-进行中 2-已结束',
    `admin_id` BIGINT DEFAULT NULL COMMENT '创建管理员ID',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `dislike_count` INT DEFAULT 0 COMMENT '踩数',
    `comment_count` INT DEFAULT 0 COMMENT '评论数',
    `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
    `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书法交流活动表';

-- 活动报名表
DROP TABLE IF EXISTS `activity_registration`;
CREATE TABLE `activity_registration` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `activity_id` BIGINT NOT NULL COMMENT '活动ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `status` INT DEFAULT 0 COMMENT '状态 0-待审核 1-通过 2-拒绝 3-已取消',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动报名表';

-- 书法视频表
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(200) NOT NULL COMMENT '视频标题',
    `description` TEXT COMMENT '视频描述',
    `video_url` VARCHAR(500) DEFAULT NULL COMMENT '视频地址',
    `cover_image` VARCHAR(500) DEFAULT NULL COMMENT '封面图片',
    `user_id` BIGINT NOT NULL COMMENT '上传者ID',
    `status` INT DEFAULT 1 COMMENT '状态 1-正常 0-下架',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `dislike_count` INT DEFAULT 0 COMMENT '踩数',
    `comment_count` INT DEFAULT 0 COMMENT '评论数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书法视频表';

-- 投票表
DROP TABLE IF EXISTS `vote`;
CREATE TABLE `vote` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(200) NOT NULL COMMENT '投票标题',
    `description` TEXT COMMENT '投票描述',
    `cover_image` VARCHAR(500) DEFAULT NULL COMMENT '封面图片',
    `status` INT DEFAULT 0 COMMENT '状态 0-未开始 1-进行中 2-已结束',
    `admin_id` BIGINT DEFAULT NULL COMMENT '创建管理员ID',
    `start_time` DATETIME DEFAULT NULL COMMENT '开始时间',
    `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投票表';

-- 投票选项表
DROP TABLE IF EXISTS `vote_item`;
CREATE TABLE `vote_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `vote_id` BIGINT NOT NULL COMMENT '投票ID',
    `work_id` BIGINT NOT NULL COMMENT '作品ID',
    `vote_count` INT DEFAULT 0 COMMENT '得票数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_vote_id` (`vote_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投票选项表';

-- 投票记录表
DROP TABLE IF EXISTS `vote_record`;
CREATE TABLE `vote_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `vote_id` BIGINT NOT NULL COMMENT '投票ID',
    `vote_item_id` BIGINT NOT NULL COMMENT '投票选项ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_vote_id` (`vote_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投票记录表';

-- 投诉表
DROP TABLE IF EXISTS `complaint`;
CREATE TABLE `complaint` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '投诉用户ID',
    `target_type` VARCHAR(20) DEFAULT NULL COMMENT '投诉对象类型',
    `target_id` BIGINT DEFAULT NULL COMMENT '投诉对象ID',
    `title` VARCHAR(200) DEFAULT NULL COMMENT '投诉标题',
    `content` TEXT COMMENT '投诉内容',
    `status` INT DEFAULT 0 COMMENT '状态 0-待处理 1-已处理 2-已驳回',
    `reply` TEXT COMMENT '处理回复',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投诉表';

-- 评论表
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `target_type` VARCHAR(20) NOT NULL COMMENT '评论对象类型(work/activity/video)',
    `target_id` BIGINT NOT NULL COMMENT '评论对象ID',
    `user_id` BIGINT NOT NULL COMMENT '评论用户ID',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_target` (`target_type`, `target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 点赞/踩记录表
DROP TABLE IF EXISTS `like_record`;
CREATE TABLE `like_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `target_type` VARCHAR(20) NOT NULL COMMENT '对象类型(work/activity/video)',
    `target_id` BIGINT NOT NULL COMMENT '对象ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `type` INT NOT NULL COMMENT '类型 1-点赞 2-踩',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_target_user` (`target_type`, `target_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞踩记录表';

-- 初始化管理员数据 密码: admin123
INSERT INTO `admin` (`username`, `password`, `nickname`, `role`, `status`) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', 'ADMIN', 1),
('activity_admin', 'e10adc3949ba59abbe56e057f20f883e', '活动管理员', 'ACTIVITY_ADMIN', 1);

-- 初始化测试用户 密码: 123456
INSERT INTO `user` (`username`, `password`, `nickname`, `email`, `phone`, `gender`, `status`) VALUES
('user1', 'e10adc3949ba59abbe56e057f20f883e', '书法爱好者小明', 'xiaoming@test.com', '13800138001', '男', 1),
('user2', 'e10adc3949ba59abbe56e057f20f883e', '墨韵清风', 'moyu@test.com', '13800138002', '女', 1);

-- 初始化书法作品
INSERT INTO `calligraphy_work` (`title`, `description`, `image_url`, `category`, `user_id`, `status`, `like_count`) VALUES
('临摹兰亭序', '王羲之兰亭序临摹作品，用笔流畅，结构严谨', NULL, '行书', 1, 1, 5),
('楷书心经', '心经楷书作品，端庄大气，笔力遒劲', NULL, '楷书', 1, 1, 3),
('草书唐诗三首', '草书书写唐诗三首，飘逸洒脱', NULL, '草书', 2, 1, 8),
('隶书对联', '隶书春联一副，古朴典雅', NULL, '隶书', 2, 1, 2),
('篆书百寿图', '篆书百寿图，构思精巧', NULL, '篆书', 1, 1, 6);

-- 初始化活动
INSERT INTO `activity` (`title`, `description`, `cover_image`, `location`, `max_participants`, `current_participants`, `status`, `admin_id`, `start_time`, `end_time`) VALUES
('2024年春季书法交流会', '邀请各位书法爱好者参加春季书法交流活动，现场有名家指导', NULL, '北京市朝阳区文化中心', 50, 2, 1, 1, '2024-03-15 09:00:00', '2024-03-15 17:00:00'),
('少儿书法启蒙班', '面向6-12岁少儿的书法启蒙课程，培养书法兴趣', NULL, '上海市浦东新区青少年活动中心', 30, 0, 0, 2, '2024-04-01 14:00:00', '2024-04-01 16:00:00'),
('名家书法讲座', '著名书法家李老师主讲，分享书法创作心得与技巧', NULL, '线上直播', 200, 0, 0, 1, '2024-05-10 19:00:00', '2024-05-10 21:00:00');

-- 初始化视频
INSERT INTO `video` (`title`, `description`, `video_url`, `cover_image`, `user_id`, `status`, `like_count`) VALUES
('楷书入门教学', '从基本笔画开始，教你写好楷书', NULL, NULL, 1, 1, 10),
('行书运笔技巧', '行书运笔的基本技巧和注意事项', NULL, NULL, 2, 1, 7),
('草书欣赏与创作', '草书的艺术魅力与创作方法', NULL, NULL, 1, 1, 5);

-- 初始化投票
INSERT INTO `vote` (`title`, `description`, `status`, `admin_id`, `start_time`, `end_time`) VALUES
('最佳书法作品评选', '请为您喜欢的书法作品投票', 1, 1, '2024-01-01 00:00:00', '2024-12-31 23:59:59');

INSERT INTO `vote_item` (`vote_id`, `work_id`, `vote_count`) VALUES
(1, 1, 15),
(1, 2, 10),
(1, 3, 20),
(1, 4, 8),
(1, 5, 12);
