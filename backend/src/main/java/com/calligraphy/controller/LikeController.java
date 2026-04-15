package com.calligraphy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.calligraphy.common.Result;
import com.calligraphy.entity.*;
import com.calligraphy.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/like")
public class LikeController {

    @Autowired
    private LikeRecordMapper likeRecordMapper;

    @Autowired
    private CalligraphyWorkMapper workMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private VideoMapper videoMapper;

    @PostMapping
    public Result<?> like(@RequestBody LikeRecord likeRecord, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        likeRecord.setUserId(userId);

        // Check if already liked/disliked
        LikeRecord exist = likeRecordMapper.selectOne(new QueryWrapper<LikeRecord>()
                .eq("target_type", likeRecord.getTargetType())
                .eq("target_id", likeRecord.getTargetId())
                .eq("user_id", userId));

        if (exist != null) {
            if (exist.getType().equals(likeRecord.getType())) {
                // Cancel like/dislike
                likeRecordMapper.deleteById(exist.getId());
                updateCount(likeRecord.getTargetType(), likeRecord.getTargetId(), likeRecord.getType(), -1);
                return Result.success("取消成功");
            } else {
                // Switch like/dislike
                updateCount(likeRecord.getTargetType(), likeRecord.getTargetId(), exist.getType(), -1);
                exist.setType(likeRecord.getType());
                likeRecordMapper.updateById(exist);
                updateCount(likeRecord.getTargetType(), likeRecord.getTargetId(), likeRecord.getType(), 1);
                return Result.success("操作成功");
            }
        }

        likeRecordMapper.insert(likeRecord);
        updateCount(likeRecord.getTargetType(), likeRecord.getTargetId(), likeRecord.getType(), 1);
        return Result.success("操作成功");
    }

    @GetMapping("/status")
    public Result<?> getStatus(@RequestParam String targetType,
                                @RequestParam Long targetId,
                                HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        LikeRecord record = likeRecordMapper.selectOne(new QueryWrapper<LikeRecord>()
                .eq("target_type", targetType)
                .eq("target_id", targetId)
                .eq("user_id", userId));
        return Result.success(record);
    }

    private void updateCount(String targetType, Long targetId, int type, int delta) {
        switch (targetType) {
            case "work":
                CalligraphyWork work = workMapper.selectById(targetId);
                if (work != null) {
                    if (type == 1) work.setLikeCount(Math.max(0, work.getLikeCount() + delta));
                    else work.setDislikeCount(Math.max(0, work.getDislikeCount() + delta));
                    workMapper.updateById(work);
                }
                break;
            case "activity":
                Activity activity = activityMapper.selectById(targetId);
                if (activity != null) {
                    if (type == 1) activity.setLikeCount(Math.max(0, activity.getLikeCount() + delta));
                    else activity.setDislikeCount(Math.max(0, activity.getDislikeCount() + delta));
                    activityMapper.updateById(activity);
                }
                break;
            case "video":
                Video video = videoMapper.selectById(targetId);
                if (video != null) {
                    if (type == 1) video.setLikeCount(Math.max(0, video.getLikeCount() + delta));
                    else video.setDislikeCount(Math.max(0, video.getDislikeCount() + delta));
                    videoMapper.updateById(video);
                }
                break;
        }
    }
}
