package com.calligraphy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.calligraphy.entity.ActivityRegistration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ActivityRegistrationMapper extends BaseMapper<ActivityRegistration> {
    @Select("<script>" +
            "SELECT r.*, u.username, u.nickname, a.title as activity_title " +
            "FROM activity_registration r " +
            "LEFT JOIN user u ON r.user_id = u.id " +
            "LEFT JOIN activity a ON r.activity_id = a.id " +
            "<where>" +
            "<if test='activityId != null'> AND r.activity_id = #{activityId}</if>" +
            "<if test='userId != null'> AND r.user_id = #{userId}</if>" +
            "<if test='status != null'> AND r.status = #{status}</if>" +
            "</where>" +
            " ORDER BY r.create_time DESC" +
            "</script>")
    IPage<ActivityRegistration> selectPageWithDetail(Page<ActivityRegistration> page,
                                                      @Param("activityId") Long activityId,
                                                      @Param("userId") Long userId,
                                                      @Param("status") Integer status);
}
