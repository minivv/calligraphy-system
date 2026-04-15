package com.calligraphy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.calligraphy.common.Result;
import com.calligraphy.entity.Activity;
import com.calligraphy.entity.ActivityRegistration;
import com.calligraphy.mapper.ActivityMapper;
import com.calligraphy.mapper.ActivityRegistrationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    @Autowired
    private ActivityRegistrationMapper registrationMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @GetMapping("/page")
    public Result<?> page(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam(required = false) Long activityId,
                          @RequestParam(required = false) Long userId,
                          @RequestParam(required = false) Integer status) {
        IPage<ActivityRegistration> page = registrationMapper.selectPageWithDetail(
                new Page<>(pageNum, pageSize), activityId, userId, status);
        return Result.success(page);
    }

    @PostMapping
    public Result<?> register(@RequestBody ActivityRegistration registration, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        registration.setUserId(userId);
        // Check if already registered
        ActivityRegistration exist = registrationMapper.selectOne(new QueryWrapper<ActivityRegistration>()
                .eq("activity_id", registration.getActivityId())
                .eq("user_id", userId)
                .ne("status", 3));
        if (exist != null) {
            return Result.error("您已报名该活动");
        }
        // Check activity capacity
        Activity activity = activityMapper.selectById(registration.getActivityId());
        if (activity == null) {
            return Result.error("活动不存在");
        }
        if (activity.getMaxParticipants() != null && activity.getMaxParticipants() > 0
                && activity.getCurrentParticipants() >= activity.getMaxParticipants()) {
            return Result.error("活动名额已满");
        }
        registration.setStatus(0);
        registrationMapper.insert(registration);
        return Result.success("报名成功");
    }

    @PutMapping("/audit/{id}")
    public Result<?> audit(@PathVariable Long id, @RequestParam Integer status) {
        ActivityRegistration reg = registrationMapper.selectById(id);
        if (reg == null) {
            return Result.error("报名记录不存在");
        }
        int oldStatus = reg.getStatus();
        reg.setStatus(status);
        registrationMapper.updateById(reg);
        // Update activity participant count based on status transitions
        if (status == 1 && oldStatus != 1) {
            Activity activity = activityMapper.selectById(reg.getActivityId());
            if (activity != null) {
                activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);
                activityMapper.updateById(activity);
            }
        } else if (oldStatus == 1 && status != 1) {
            Activity activity = activityMapper.selectById(reg.getActivityId());
            if (activity != null) {
                activity.setCurrentParticipants(Math.max(0, activity.getCurrentParticipants() - 1));
                activityMapper.updateById(activity);
            }
        }
        return Result.success();
    }

    @PutMapping("/cancel/{id}")
    public Result<?> cancel(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        ActivityRegistration reg = registrationMapper.selectById(id);
        if (reg == null || !reg.getUserId().equals(userId)) {
            return Result.error("操作无效");
        }
        int oldStatus = reg.getStatus();
        reg.setStatus(3);
        registrationMapper.updateById(reg);
        // Decrement participant count if previously approved
        if (oldStatus == 1) {
            Activity activity = activityMapper.selectById(reg.getActivityId());
            if (activity != null) {
                activity.setCurrentParticipants(Math.max(0, activity.getCurrentParticipants() - 1));
                activityMapper.updateById(activity);
            }
        }
        return Result.success("取消报名成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        registrationMapper.deleteById(id);
        return Result.success();
    }
}
