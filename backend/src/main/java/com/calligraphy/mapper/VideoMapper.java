package com.calligraphy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.calligraphy.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VideoMapper extends BaseMapper<Video> {
    @Select("<script>" +
            "SELECT v.*, u.username, u.nickname FROM video v " +
            "LEFT JOIN user u ON v.user_id = u.id " +
            "<where>" +
            "<if test='title != null and title != \"\"'> AND v.title LIKE CONCAT('%',#{title},'%')</if>" +
            "<if test='userId != null'> AND v.user_id = #{userId}</if>" +
            "<if test='status != null'> AND v.status = #{status}</if>" +
            "</where>" +
            " ORDER BY v.create_time DESC" +
            "</script>")
    IPage<Video> selectPageWithUser(Page<Video> page,
                                     @Param("title") String title,
                                     @Param("userId") Long userId,
                                     @Param("status") Integer status);
}
