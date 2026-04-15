package com.calligraphy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.calligraphy.entity.CalligraphyWork;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CalligraphyWorkMapper extends BaseMapper<CalligraphyWork> {
    @Select("<script>" +
            "SELECT w.*, u.username, u.nickname FROM calligraphy_work w " +
            "LEFT JOIN user u ON w.user_id = u.id " +
            "<where>" +
            "<if test='title != null and title != \"\"'> AND w.title LIKE CONCAT('%',#{title},'%')</if>" +
            "<if test='category != null and category != \"\"'> AND w.category = #{category}</if>" +
            "<if test='userId != null'> AND w.user_id = #{userId}</if>" +
            "</where>" +
            " ORDER BY w.create_time DESC" +
            "</script>")
    IPage<CalligraphyWork> selectPageWithUser(Page<CalligraphyWork> page,
                                               @Param("title") String title,
                                               @Param("category") String category,
                                               @Param("userId") Long userId);
}
