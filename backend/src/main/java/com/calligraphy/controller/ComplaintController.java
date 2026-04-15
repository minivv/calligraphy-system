package com.calligraphy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.calligraphy.common.Result;
import com.calligraphy.entity.Complaint;
import com.calligraphy.mapper.ComplaintMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/complaint")
public class ComplaintController {

    @Autowired
    private ComplaintMapper complaintMapper;

    @GetMapping("/page")
    public Result<?> page(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam(required = false) Integer status,
                          @RequestParam(required = false) Long userId) {
        IPage<Complaint> page = complaintMapper.selectPageWithUser(
                new Page<>(pageNum, pageSize), status, userId);
        return Result.success(page);
    }

    @PostMapping
    public Result<?> add(@RequestBody Complaint complaint, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        complaint.setUserId(userId);
        complaint.setStatus(0);
        complaintMapper.insert(complaint);
        return Result.success("投诉提交成功");
    }

    @PutMapping("/handle/{id}")
    public Result<?> handle(@PathVariable Long id, @RequestBody Complaint complaint) {
        Complaint update = new Complaint();
        update.setId(id);
        update.setStatus(complaint.getStatus());
        update.setReply(complaint.getReply());
        complaintMapper.updateById(update);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        complaintMapper.deleteById(id);
        return Result.success();
    }
}
