package com.calligraphy.controller;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.calligraphy.common.Constants;
import com.calligraphy.common.Result;
import com.calligraphy.entity.User;
import com.calligraphy.mapper.UserMapper;
import com.calligraphy.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        User dbUser = userMapper.selectOne(new QueryWrapper<User>()
                .eq("username", user.getUsername()));
        if (dbUser == null) {
            return Result.error("用户不存在");
        }
        if (!SecureUtil.md5(user.getPassword()).equals(dbUser.getPassword())) {
            return Result.error("密码错误");
        }
        if (dbUser.getStatus() == 0) {
            return Result.error("账号已被禁用");
        }
        String token = JwtUtils.generateToken(dbUser.getId(), dbUser.getUsername(), Constants.ROLE_USER);
        dbUser.setToken(token);
        dbUser.setPassword(null);
        return Result.success("登录成功", dbUser);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        User exist = userMapper.selectOne(new QueryWrapper<User>()
                .eq("username", user.getUsername()));
        if (exist != null) {
            return Result.error("用户名已存在");
        }
        user.setPassword(SecureUtil.md5(user.getPassword()));
        user.setStatus(1);
        if (user.getNickname() == null || user.getNickname().isEmpty()) {
            user.setNickname(user.getUsername());
        }
        userMapper.insert(user);
        return Result.success("注册成功", null);
    }

    @GetMapping("/info")
    public Result<?> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userMapper.selectById(userId);
        user.setPassword(null);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result<?> updateUser(@RequestBody User user, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        user.setId(userId);
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(SecureUtil.md5(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        userMapper.updateById(user);
        return Result.success("更新成功", null);
    }

    @GetMapping("/page")
    public Result<?> page(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam(required = false) String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            wrapper.like("username", username);
        }
        wrapper.orderByDesc("create_time");
        Page<User> page = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        page.getRecords().forEach(u -> u.setPassword(null));
        return Result.success(page);
    }

    @PutMapping("/admin-update")
    public Result<?> adminUpdate(@RequestBody User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(SecureUtil.md5(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        userMapper.updateById(user);
        return Result.success("更新成功", null);
    }

    @PostMapping("/admin-add")
    public Result<?> adminAdd(@RequestBody User user) {
        User exist = userMapper.selectOne(new QueryWrapper<User>()
                .eq("username", user.getUsername()));
        if (exist != null) {
            return Result.error("用户名已存在");
        }
        user.setPassword(SecureUtil.md5(user.getPassword()));
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        if (user.getNickname() == null || user.getNickname().isEmpty()) {
            user.setNickname(user.getUsername());
        }
        userMapper.insert(user);
        return Result.success("添加成功", null);
    }

    @PutMapping("/status/{id}")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        userMapper.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        userMapper.deleteById(id);
        return Result.success();
    }

    @PutMapping("/password")
    public Result<?> updatePassword(@RequestBody User user, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User dbUser = userMapper.selectById(userId);
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setPassword(SecureUtil.md5(user.getPassword()));
        userMapper.updateById(updateUser);
        return Result.success("密码修改成功", null);
    }
}
