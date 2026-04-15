package com.calligraphy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.calligraphy.common.Result;
import com.calligraphy.entity.CalligraphyWork;
import com.calligraphy.mapper.CalligraphyWorkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/work")
public class CalligraphyWorkController {

    @Autowired
    private CalligraphyWorkMapper workMapper;

    @GetMapping("/page")
    public Result<?> page(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam(required = false) String title,
                          @RequestParam(required = false) String category,
                          @RequestParam(required = false) Long userId) {
        IPage<CalligraphyWork> page = workMapper.selectPageWithUser(
                new Page<>(pageNum, pageSize), title, category, userId, null);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        CalligraphyWork work = workMapper.selectById(id);
        return Result.success(work);
    }

    @PostMapping
    public Result<?> add(@RequestBody CalligraphyWork work, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        work.setUserId(userId);
        work.setStatus(1);
        work.setLikeCount(0);
        work.setDislikeCount(0);
        work.setCommentCount(0);
        workMapper.insert(work);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody CalligraphyWork work) {
        workMapper.updateById(work);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        workMapper.deleteById(id);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        CalligraphyWork work = new CalligraphyWork();
        work.setId(id);
        work.setStatus(status);
        workMapper.updateById(work);
        return Result.success();
    }
}
