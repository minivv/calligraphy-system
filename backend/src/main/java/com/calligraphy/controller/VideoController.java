package com.calligraphy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.calligraphy.common.Result;
import com.calligraphy.entity.Video;
import com.calligraphy.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/video")
public class VideoController {

    @Autowired
    private VideoMapper videoMapper;

    @GetMapping("/page")
    public Result<?> page(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam(required = false) String title,
                          @RequestParam(required = false) Long userId) {
        IPage<Video> page = videoMapper.selectPageWithUser(
                new Page<>(pageNum, pageSize), title, userId);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        Video video = videoMapper.selectById(id);
        return Result.success(video);
    }

    @PostMapping
    public Result<?> add(@RequestBody Video video, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        video.setUserId(userId);
        video.setStatus(1);
        video.setLikeCount(0);
        video.setDislikeCount(0);
        video.setCommentCount(0);
        videoMapper.insert(video);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Video video) {
        videoMapper.updateById(video);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        videoMapper.deleteById(id);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        Video video = new Video();
        video.setId(id);
        video.setStatus(status);
        videoMapper.updateById(video);
        return Result.success();
    }
}
