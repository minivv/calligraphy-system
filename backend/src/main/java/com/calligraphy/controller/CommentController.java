package com.calligraphy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.calligraphy.common.Result;
import com.calligraphy.entity.CalligraphyWork;
import com.calligraphy.entity.Activity;
import com.calligraphy.entity.Video;
import com.calligraphy.entity.Comment;
import com.calligraphy.mapper.CalligraphyWorkMapper;
import com.calligraphy.mapper.ActivityMapper;
import com.calligraphy.mapper.VideoMapper;
import com.calligraphy.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CalligraphyWorkMapper workMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private VideoMapper videoMapper;

    @GetMapping("/page")
    public Result<?> page(@RequestParam String targetType,
                          @RequestParam Long targetId,
                          @RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<Comment> page = commentMapper.selectPageWithUser(
                new Page<>(pageNum, pageSize), targetType, targetId);
        return Result.success(page);
    }

    @PostMapping
    public Result<?> add(@RequestBody Comment comment, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        comment.setUserId(userId);
        commentMapper.insert(comment);
        // Update comment count
        updateCommentCount(comment.getTargetType(), comment.getTargetId(), 1);
        return Result.success("评论成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        Comment comment = commentMapper.selectById(id);
        if (comment != null) {
            commentMapper.deleteById(id);
            updateCommentCount(comment.getTargetType(), comment.getTargetId(), -1);
        }
        return Result.success();
    }

    private void updateCommentCount(String targetType, Long targetId, int delta) {
        switch (targetType) {
            case "work":
                CalligraphyWork work = workMapper.selectById(targetId);
                if (work != null) {
                    work.setCommentCount(Math.max(0, work.getCommentCount() + delta));
                    workMapper.updateById(work);
                }
                break;
            case "activity":
                Activity activity = activityMapper.selectById(targetId);
                if (activity != null) {
                    activity.setCommentCount(Math.max(0, activity.getCommentCount() + delta));
                    activityMapper.updateById(activity);
                }
                break;
            case "video":
                Video video = videoMapper.selectById(targetId);
                if (video != null) {
                    video.setCommentCount(Math.max(0, video.getCommentCount() + delta));
                    videoMapper.updateById(video);
                }
                break;
        }
    }
}
