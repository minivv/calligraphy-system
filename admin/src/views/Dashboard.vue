<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <i class="el-icon-user" style="color:#409EFF"></i>
            <div><p class="num">{{ stats.userCount }}</p><p class="label">用户数</p></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <i class="el-icon-picture" style="color:#67C23A"></i>
            <div><p class="num">{{ stats.workCount }}</p><p class="label">作品数</p></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <i class="el-icon-date" style="color:#E6A23C"></i>
            <div><p class="num">{{ stats.activityCount }}</p><p class="label">活动数</p></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <i class="el-icon-video-camera" style="color:#F56C6C"></i>
            <div><p class="num">{{ stats.videoCount }}</p><p class="label">视频数</p></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-card style="margin-top:20px">
      <h3>欢迎使用书法交流活动管理系统</h3>
      <p style="margin-top:10px;color:#999">系统功能包括：用户管理、书法作品管理、活动管理、报名管理、视频管理、投票管理、投诉管理等。</p>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return { stats: { userCount: 0, workCount: 0, activityCount: 0, videoCount: 0 } }
  },
  async created() {
    try {
      const [u, w, a, v] = await Promise.all([
        this.$request.get('/api/user/page', { params: { pageNum: 1, pageSize: 1 } }),
        this.$request.get('/api/work/page', { params: { pageNum: 1, pageSize: 1 } }),
        this.$request.get('/api/activity/page', { params: { pageNum: 1, pageSize: 1 } }),
        this.$request.get('/api/video/page', { params: { pageNum: 1, pageSize: 1 } })
      ])
      this.stats.userCount = u.data.total
      this.stats.workCount = w.data.total
      this.stats.activityCount = a.data.total
      this.stats.videoCount = v.data.total
    } catch (e) {}
  }
}
</script>

<style scoped>
.stat-card { display:flex; align-items:center; }
.stat-card i { font-size:48px; margin-right:20px; }
.stat-card .num { font-size:28px; font-weight:bold; }
.stat-card .label { color:#999; margin-top:5px; }
</style>
