package com.calligraphy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.calligraphy.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {
    @Select("<script>" +
            "SELECT a.*, ad.nickname as admin_name FROM activity a " +
            "LEFT JOIN admin ad ON a.admin_id = ad.id " +
            "<where>" +
            "<if test='title != null and title != \"\"'> AND a.title LIKE CONCAT('%',#{title},'%')</if>" +
            "<if test='status != null'> AND a.status = #{status}</if>" +
            "</where>" +
            " ORDER BY a.create_time DESC" +
            "</script>")
    IPage<Activity> selectPageWithAdmin(Page<Activity> page,
                                         @Param("title") String title,
                                         @Param("status") Integer status);
}
