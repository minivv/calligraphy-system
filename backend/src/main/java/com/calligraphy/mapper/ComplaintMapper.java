package com.calligraphy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.calligraphy.entity.Complaint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ComplaintMapper extends BaseMapper<Complaint> {
    @Select("<script>" +
            "SELECT c.*, u.username FROM complaint c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "<where>" +
            "<if test='status != null'> AND c.status = #{status}</if>" +
            "<if test='userId != null'> AND c.user_id = #{userId}</if>" +
            "</where>" +
            " ORDER BY c.create_time DESC" +
            "</script>")
    IPage<Complaint> selectPageWithUser(Page<Complaint> page,
                                         @Param("status") Integer status,
                                         @Param("userId") Long userId);
}
