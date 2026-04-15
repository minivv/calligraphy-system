package com.calligraphy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.calligraphy.entity.VoteItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VoteItemMapper extends BaseMapper<VoteItem> {
    @Select("SELECT vi.*, w.title as work_title, w.image_url as work_image, u.nickname as author_name " +
            "FROM vote_item vi " +
            "LEFT JOIN calligraphy_work w ON vi.work_id = w.id " +
            "LEFT JOIN user u ON w.user_id = u.id " +
            "WHERE vi.vote_id = #{voteId} " +
            "ORDER BY vi.vote_count DESC")
    List<VoteItem> selectByVoteIdWithWork(@Param("voteId") Long voteId);
}
