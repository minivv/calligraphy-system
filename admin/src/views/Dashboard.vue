<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="stat-card">
            <i class="el-icon-user" style="color:#409EFF"></i>
            <div><p class="num">{{ stats.userCount }}</p><p class="label">用户数</p></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="stat-card">
            <i class="el-icon-picture" style="color:#67C23A"></i>
            <div><p class="num">{{ stats.workCount }}</p><p class="label">作品数</p></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="stat-card">
            <i class="el-icon-date" style="color:#E6A23C"></i>
            <div><p class="num">{{ stats.activityCount }}</p><p class="label">活动数</p></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="stat-card">
            <i class="el-icon-video-camera" style="color:#F56C6C"></i>
            <div><p class="num">{{ stats.videoCount }}</p><p class="label">视频数</p></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="stat-card">
            <i class="el-icon-tickets" style="color:#909399"></i>
            <div><p class="num">{{ stats.registrationCount }}</p><p class="label">报名数</p></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover">
          <div class="stat-card">
            <i class="el-icon-warning" style="color:#E6A23C"></i>
            <div><p class="num">{{ stats.complaintCount }}</p><p class="label">投诉数</p></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <el-card>
          <div slot="header"><span>作品分类统计</span></div>
          <div ref="chartWorkCategory" style="height:300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header"><span>活动状态分布</span></div>
          <div ref="chartActivityStatus" style="height:300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <el-card>
          <div slot="header"><span>报名审核状态</span></div>
          <div ref="chartRegStatus" style="height:300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header"><span>投诉处理状态</span></div>
          <div ref="chartComplaintStatus" style="height:300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <el-card>
          <div slot="header"><span>作品上下架状态</span></div>
          <div ref="chartWorkStatus" style="height:300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header"><span>作品点赞排行</span></div>
          <div ref="chartTopLiked" style="height:300px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  data() {
    return {
      stats: { userCount: 0, workCount: 0, activityCount: 0, videoCount: 0, registrationCount: 0, complaintCount: 0 },
      charts: []
    }
  },
  async created() {
    try {
      const res = await this.$request.get('/api/stats/dashboard')
      const data = res.data
      this.stats.userCount = data.userCount || 0
      this.stats.workCount = data.workCount || 0
      this.stats.activityCount = data.activityCount || 0
      this.stats.videoCount = data.videoCount || 0
      this.stats.registrationCount = data.registrationCount || 0
      this.stats.complaintCount = data.complaintCount || 0
      this.$nextTick(() => { this.renderCharts(data) })
    } catch (e) { console.error(e) }
  },
  beforeDestroy() {
    this.charts.forEach(c => c.dispose())
  },
  methods: {
    renderCharts(data) {
      // 1. Works by category - pie
      this.renderPie(this.$refs.chartWorkCategory, '作品分类',
        (data.worksByCategory || []).map(item => ({ name: item.category || '未分类', value: item.count })))

      // 2. Activity status - pie
      const activityStatusMap = { 0: '未开始', 1: '进行中', 2: '已结束' }
      this.renderPie(this.$refs.chartActivityStatus, '活动状态',
        (data.activitiesByStatus || []).map(item => ({ name: activityStatusMap[item.status] || '未知', value: item.count })))

      // 3. Registration status - pie
      const regStatusMap = { 0: '待审核', 1: '已通过', 2: '已拒绝', 3: '已取消' }
      this.renderPie(this.$refs.chartRegStatus, '报名状态',
        (data.registrationsByStatus || []).map(item => ({ name: regStatusMap[item.status] || '未知', value: item.count })))

      // 4. Complaint status - pie
      const compStatusMap = { 0: '待处理', 1: '已处理', 2: '已驳回' }
      this.renderPie(this.$refs.chartComplaintStatus, '投诉状态',
        (data.complaintsByStatus || []).map(item => ({ name: compStatusMap[item.status] || '未知', value: item.count })))

      // 5. Work status - pie
      const workStatusMap = { 0: '已下架', 1: '正常' }
      this.renderPie(this.$refs.chartWorkStatus, '作品状态',
        (data.worksByStatus || []).map(item => ({ name: workStatusMap[item.status] || '未知', value: item.count })))

      // 6. Top liked works - bar
      const topWorks = data.topLikedWorks || []
      this.renderBar(this.$refs.chartTopLiked, '点赞排行',
        topWorks.map(item => item.title),
        topWorks.map(item => item.like_count || item.likeCount || 0))
    },
    renderPie(el, title, seriesData) {
      if (!el) return
      const chart = echarts.init(el)
      this.charts.push(chart)
      chart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { bottom: 0 },
        series: [{
          name: title, type: 'pie', radius: ['35%', '60%'],
          label: { formatter: '{b}\n{c}' },
          data: seriesData.length > 0 ? seriesData : [{ name: '暂无数据', value: 0 }]
        }]
      })
    },
    renderBar(el, title, categories, values) {
      if (!el) return
      const chart = echarts.init(el)
      this.charts.push(chart)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: 60, right: 20, bottom: 60, top: 20 },
        xAxis: { type: 'category', data: categories, axisLabel: { rotate: 30, interval: 0 } },
        yAxis: { type: 'value', minInterval: 1 },
        series: [{ name: title, type: 'bar', data: values, itemStyle: { color: '#409EFF' }, barMaxWidth: 40 }]
      })
    }
  }
}
</script>

<style scoped>
.stat-card { display:flex; align-items:center; }
.stat-card i { font-size:36px; margin-right:15px; }
.stat-card .num { font-size:24px; font-weight:bold; }
.stat-card .label { color:#999; margin-top:3px; font-size:13px; }
</style>
