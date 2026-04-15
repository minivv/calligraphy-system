package com.calligraphy.controller;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.calligraphy.common.Result;
import com.calligraphy.entity.Admin;
import com.calligraphy.mapper.AdminMapper;
import com.calligraphy.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminMapper adminMapper;

    @PostMapping("/login")
    public Result<?> login(@RequestBody Admin admin) {
        Admin dbAdmin = adminMapper.selectOne(new QueryWrapper<Admin>()
                .eq("username", admin.getUsername()));
        if (dbAdmin == null) {
            return Result.error("管理员不存在");
        }
        if (!SecureUtil.md5(admin.getPassword()).equals(dbAdmin.getPassword())) {
            return Result.error("密码错误");
        }
        if (dbAdmin.getStatus() == 0) {
            return Result.error("账号已被禁用");
        }
        String token = JwtUtils.generateToken(dbAdmin.getId(), dbAdmin.getUsername(), dbAdmin.getRole());
        dbAdmin.setToken(token);
        dbAdmin.setPassword(null);
        return Result.success("登录成功", dbAdmin);
    }

    @GetMapping("/info")
    public Result<?> getAdminInfo(@RequestParam Long id) {
        Admin admin = adminMapper.selectById(id);
        admin.setPassword(null);
        return Result.success(admin);
    }

    @GetMapping("/page")
    public Result<?> page(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam(required = false) String username,
                          @RequestParam(required = false) String role) {
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            wrapper.like("username", username);
        }
        if (role != null && !role.isEmpty()) {
            wrapper.eq("role", role);
        }
        wrapper.orderByDesc("create_time");
        Page<Admin> page = adminMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        page.getRecords().forEach(a -> a.setPassword(null));
        return Result.success(page);
    }

    @PostMapping
    public Result<?> add(@RequestBody Admin admin) {
        Admin exist = adminMapper.selectOne(new QueryWrapper<Admin>()
                .eq("username", admin.getUsername()));
        if (exist != null) {
            return Result.error("用户名已存在");
        }
        admin.setPassword(SecureUtil.md5(admin.getPassword()));
        admin.setStatus(1);
        adminMapper.insert(admin);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Admin admin) {
        if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
            admin.setPassword(SecureUtil.md5(admin.getPassword()));
        } else {
            admin.setPassword(null);
        }
        adminMapper.updateById(admin);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        adminMapper.deleteById(id);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        Admin admin = new Admin();
        admin.setId(id);
        admin.setStatus(status);
        adminMapper.updateById(admin);
        return Result.success();
    }
}
