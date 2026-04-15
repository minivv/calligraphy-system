package com.calligraphy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.calligraphy.common.Result;
import com.calligraphy.entity.*;
import com.calligraphy.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/front")
public class FrontController {

    @Autowired
    private CalligraphyWorkMapper workMapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private VoteMapper voteMapper;
    @Autowired
    private VoteItemMapper voteItemMapper;
    @Autowired
    private CommentMapper commentMapper;

    @GetMapping("/home")
    public Result<?> home() {
        Map<String, Object> result = new HashMap<>();
        IPage<CalligraphyWork> works = workMapper.selectPageWithUser(
                new Page<>(1, 8), null, null, null);
        result.put("works", works.getRecords());
        IPage<Activity> activities = activityMapper.selectPageWithAdmin(
                new Page<>(1, 6), null, null);
        result.put("activities", activities.getRecords());
        IPage<Video> videos = videoMapper.selectPageWithUser(
                new Page<>(1, 6), null, null);
        result.put("videos", videos.getRecords());
        return Result.success(result);
    }

    @GetMapping("/work/page")
    public Result<?> workPage(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "12") Integer pageSize,
                               @RequestParam(required = false) String title,
                               @RequestParam(required = false) String category) {
        IPage<CalligraphyWork> page = workMapper.selectPageWithUser(
                new Page<>(pageNum, pageSize), title, category, null);
        return Result.success(page);
    }

    @GetMapping("/work/{id}")
    public Result<?> workDetail(@PathVariable Long id) {
        CalligraphyWork work = workMapper.selectById(id);
        IPage<CalligraphyWork> page = workMapper.selectPageWithUser(new Page<>(1, 1000), null, null, work.getUserId());
        for (CalligraphyWork w : page.getRecords()) {
            if (w.getId().equals(id)) {
                return Result.success(w);
            }
        }
        return Result.success(work);
    }

    @GetMapping("/activity/page")
    public Result<?> activityPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "12") Integer pageSize,
                                   @RequestParam(required = false) String title,
                                   @RequestParam(required = false) Integer status) {
        IPage<Activity> page = activityMapper.selectPageWithAdmin(
                new Page<>(pageNum, pageSize), title, status);
        return Result.success(page);
    }

    @GetMapping("/activity/{id}")
    public Result<?> activityDetail(@PathVariable Long id) {
        Activity activity = activityMapper.selectById(id);
        return Result.success(activity);
    }

    @GetMapping("/video/page")
    public Result<?> videoPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "12") Integer pageSize,
                                @RequestParam(required = false) String title) {
        IPage<Video> page = videoMapper.selectPageWithUser(
                new Page<>(pageNum, pageSize), title, null);
        return Result.success(page);
    }

    @GetMapping("/video/{id}")
    public Result<?> videoDetail(@PathVariable Long id) {
        Video video = videoMapper.selectById(id);
        IPage<Video> page = videoMapper.selectPageWithUser(new Page<>(1, 1000), null, video.getUserId());
        for (Video v : page.getRecords()) {
            if (v.getId().equals(id)) {
                return Result.success(v);
            }
        }
        return Result.success(video);
    }

    @GetMapping("/vote/page")
    public Result<?> votePage(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "12") Integer pageSize,
                               @RequestParam(required = false) String title) {
        QueryWrapper<Vote> wrapper = new QueryWrapper<>();
        if (title != null && !title.isEmpty()) {
            wrapper.like("title", title);
        }
        wrapper.orderByDesc("create_time");
        Page<Vote> page = voteMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(page);
    }

    @GetMapping("/vote/{id}")
    public Result<?> voteDetail(@PathVariable Long id) {
        Vote vote = voteMapper.selectById(id);
        List<VoteItem> items = voteItemMapper.selectByVoteIdWithWork(id);
        Map<String, Object> result = new HashMap<>();
        result.put("vote", vote);
        result.put("items", items);
        return Result.success(result);
    }

    @GetMapping("/comment/page")
    public Result<?> commentPage(@RequestParam String targetType,
                                  @RequestParam Long targetId,
                                  @RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "20") Integer pageSize) {
        IPage<Comment> page = commentMapper.selectPageWithUser(
                new Page<>(pageNum, pageSize), targetType, targetId);
        return Result.success(page);
    }
}
