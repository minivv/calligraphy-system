package com.calligraphy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("vote_item")
public class VoteItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long voteId;
    private Long workId;
    private Integer voteCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String workTitle;
    @TableField(exist = false)
    private String workImage;
    @TableField(exist = false)
    private String authorName;
}
