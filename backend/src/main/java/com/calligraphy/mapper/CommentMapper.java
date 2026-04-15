package com.calligraphy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.calligraphy.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("SELECT c.*, u.username, u.nickname, u.avatar FROM comment c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "WHERE c.target_type = #{targetType} AND c.target_id = #{targetId} " +
            "ORDER BY c.create_time DESC")
    IPage<Comment> selectPageWithUser(Page<Comment> page,
                                       @Param("targetType") String targetType,
                                       @Param("targetId") Long targetId);
}
