package com.calligraphy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.calligraphy.common.Result;
import com.calligraphy.entity.Vote;
import com.calligraphy.entity.VoteItem;
import com.calligraphy.entity.VoteRecord;
import com.calligraphy.mapper.VoteItemMapper;
import com.calligraphy.mapper.VoteMapper;
import com.calligraphy.mapper.VoteRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vote")
public class VoteController {

    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private VoteItemMapper voteItemMapper;

    @Autowired
    private VoteRecordMapper voteRecordMapper;

    @GetMapping("/page")
    public Result<?> page(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam(required = false) String title) {
        QueryWrapper<Vote> wrapper = new QueryWrapper<>();
        if (title != null && !title.isEmpty()) {
            wrapper.like("title", title);
        }
        wrapper.orderByDesc("create_time");
        Page<Vote> page = voteMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        Vote vote = voteMapper.selectById(id);
        List<VoteItem> items = voteItemMapper.selectByVoteIdWithWork(id);
        Map<String, Object> result = new HashMap<>();
        result.put("vote", vote);
        result.put("items", items);
        return Result.success(result);
    }

    @PostMapping
    public Result<?> add(@RequestBody Vote vote, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        vote.setAdminId(userId);
        if (vote.getStatus() == null) {
            vote.setStatus(0);
        }
        voteMapper.insert(vote);
        return Result.success(vote);
    }

    @PutMapping
    public Result<?> update(@RequestBody Vote vote) {
        voteMapper.updateById(vote);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        voteMapper.deleteById(id);
        voteItemMapper.delete(new QueryWrapper<VoteItem>().eq("vote_id", id));
        voteRecordMapper.delete(new QueryWrapper<VoteRecord>().eq("vote_id", id));
        return Result.success();
    }

    @PostMapping("/item")
    public Result<?> addItem(@RequestBody VoteItem item) {
        item.setVoteCount(0);
        voteItemMapper.insert(item);
        return Result.success();
    }

    @DeleteMapping("/item/{id}")
    public Result<?> deleteItem(@PathVariable Long id) {
        voteItemMapper.deleteById(id);
        return Result.success();
    }

    @PostMapping("/cast/{voteItemId}")
    public Result<?> castVote(@PathVariable Long voteItemId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        VoteItem item = voteItemMapper.selectById(voteItemId);
        if (item == null) {
            return Result.error("投票项不存在");
        }
        // Check if already voted in this vote
        VoteRecord exist = voteRecordMapper.selectOne(new QueryWrapper<VoteRecord>()
                .eq("vote_id", item.getVoteId())
                .eq("user_id", userId));
        if (exist != null) {
            return Result.error("您已投过票");
        }
        // Create vote record
        VoteRecord record = new VoteRecord();
        record.setVoteId(item.getVoteId());
        record.setVoteItemId(voteItemId);
        record.setUserId(userId);
        voteRecordMapper.insert(record);
        // Update vote count
        item.setVoteCount(item.getVoteCount() + 1);
        voteItemMapper.updateById(item);
        return Result.success("投票成功");
    }

    @PutMapping("/status/{id}")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        Vote vote = new Vote();
        vote.setId(id);
        vote.setStatus(status);
        voteMapper.updateById(vote);
        return Result.success();
    }
}
