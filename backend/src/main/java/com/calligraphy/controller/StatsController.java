package com.calligraphy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.calligraphy.common.Result;
import com.calligraphy.entity.*;
import com.calligraphy.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CalligraphyWorkMapper workMapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private ActivityRegistrationMapper registrationMapper;
    @Autowired
    private ComplaintMapper complaintMapper;

    @GetMapping("/dashboard")
    public Result<?> dashboard() {
        Map<String, Object> stats = new HashMap<>();

        // 1. Works by category
        QueryWrapper<CalligraphyWork> workCatWrapper = new QueryWrapper<>();
        workCatWrapper.select("category", "COUNT(*) as count").groupBy("category");
        List<Map<String, Object>> worksByCategory = workMapper.selectMaps(workCatWrapper);
        stats.put("worksByCategory", worksByCategory);

        // 2. Activity by status (0-未开始, 1-进行中, 2-已结束)
        QueryWrapper<Activity> actStatusWrapper = new QueryWrapper<>();
        actStatusWrapper.select("status", "COUNT(*) as count").groupBy("status");
        List<Map<String, Object>> activitiesByStatus = activityMapper.selectMaps(actStatusWrapper);
        stats.put("activitiesByStatus", activitiesByStatus);

        // 3. Registration by status (0-待审核, 1-已通过, 2-已拒绝, 3-已取消)
        QueryWrapper<ActivityRegistration> regStatusWrapper = new QueryWrapper<>();
        regStatusWrapper.select("status", "COUNT(*) as count").groupBy("status");
        List<Map<String, Object>> registrationsByStatus = registrationMapper.selectMaps(regStatusWrapper);
        stats.put("registrationsByStatus", registrationsByStatus);

        // 4. Complaint by status (0-待处理, 1-已处理, 2-已驳回)
        QueryWrapper<Complaint> compStatusWrapper = new QueryWrapper<>();
        compStatusWrapper.select("status", "COUNT(*) as count").groupBy("status");
        List<Map<String, Object>> complaintsByStatus = complaintMapper.selectMaps(compStatusWrapper);
        stats.put("complaintsByStatus", complaintsByStatus);

        // 5. Works by status (1-正常, 0-下架)
        QueryWrapper<CalligraphyWork> workStatusWrapper = new QueryWrapper<>();
        workStatusWrapper.select("status", "COUNT(*) as count").groupBy("status");
        List<Map<String, Object>> worksByStatus = workMapper.selectMaps(workStatusWrapper);
        stats.put("worksByStatus", worksByStatus);

        // 6. Top liked works
        QueryWrapper<CalligraphyWork> topWorksWrapper = new QueryWrapper<>();
        topWorksWrapper.select("title", "like_count").orderByDesc("like_count").last("LIMIT 8");
        List<Map<String, Object>> topLikedWorks = workMapper.selectMaps(topWorksWrapper);
        stats.put("topLikedWorks", topLikedWorks);

        // Basic counts
        stats.put("userCount", userMapper.selectCount(null));
        stats.put("workCount", workMapper.selectCount(null));
        stats.put("activityCount", activityMapper.selectCount(null));
        stats.put("videoCount", videoMapper.selectCount(null));
        stats.put("registrationCount", registrationMapper.selectCount(null));
        stats.put("complaintCount", complaintMapper.selectCount(null));

        return Result.success(stats);
    }
}
