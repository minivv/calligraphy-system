package com.calligraphy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.calligraphy.common.Result;
import com.calligraphy.entity.Activity;
import com.calligraphy.mapper.ActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    @Autowired
    private ActivityMapper activityMapper;

    @GetMapping("/page")
    public Result<?> page(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam(required = false) String title,
                          @RequestParam(required = false) Integer status) {
        IPage<Activity> page = activityMapper.selectPageWithAdmin(
                new Page<>(pageNum, pageSize), title, status);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        Activity activity = activityMapper.selectById(id);
        return Result.success(activity);
    }

    @PostMapping
    public Result<?> add(@RequestBody Activity activity, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        activity.setAdminId(userId);
        activity.setCurrentParticipants(0);
        activity.setLikeCount(0);
        activity.setDislikeCount(0);
        activity.setCommentCount(0);
        if (activity.getStatus() == null) {
            activity.setStatus(0);
        }
        activityMapper.insert(activity);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Activity activity) {
        activityMapper.updateById(activity);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        activityMapper.deleteById(id);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        Activity activity = new Activity();
        activity.setId(id);
        activity.setStatus(status);
        activityMapper.updateById(activity);
        return Result.success();
    }
}
