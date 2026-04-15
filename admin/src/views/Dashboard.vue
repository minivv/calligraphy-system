<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="4" v-for="(stat, idx) in statCards" :key="idx">
        <div class="stat-card">
          <div class="stat-icon" :style="{background: stat.bg}">
            <i :class="stat.icon" :style="{color: stat.color}"></i>
          </div>
          <div class="stat-info">
            <p class="stat-num">{{ stat.value }}</p>
            <p class="stat-label">{{ stat.label }}</p>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:24px">
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-header">作品分类统计</div>
          <div ref="chartWorkCategory" style="height:300px"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-header">活动状态分布</div>
          <div ref="chartActivityStatus" style="height:300px"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:24px">
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-header">报名审核状态</div>
          <div ref="chartRegStatus" style="height:300px"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-header">投诉处理状态</div>
          <div ref="chartComplaintStatus" style="height:300px"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top:24px">
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-header">作品上下架状态</div>
          <div ref="chartWorkStatus" style="height:300px"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-header">作品点赞排行</div>
          <div ref="chartTopLiked" style="height:300px"></div>
        </div>
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
  computed: {
    statCards() {
      return [
        { label: '用户数', value: this.stats.userCount, icon: 'el-icon-user', color: '#F0B90B', bg: 'rgba(240,185,11,0.1)' },
        { label: '作品数', value: this.stats.workCount, icon: 'el-icon-picture', color: '#0ECB81', bg: 'rgba(14,203,129,0.1)' },
        { label: '活动数', value: this.stats.activityCount, icon: 'el-icon-date', color: '#1EAEDB', bg: 'rgba(30,174,219,0.1)' },
        { label: '视频数', value: this.stats.videoCount, icon: 'el-icon-video-camera', color: '#F6465D', bg: 'rgba(246,70,93,0.1)' },
        { label: '报名数', value: this.stats.registrationCount, icon: 'el-icon-tickets', color: '#848E9C', bg: 'rgba(132,142,156,0.1)' },
        { label: '投诉数', value: this.stats.complaintCount, icon: 'el-icon-warning', color: '#D0980B', bg: 'rgba(208,152,11,0.1)' }
      ]
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
      this.renderPie(this.$refs.chartWorkCategory, '作品分类',
        (data.worksByCategory || []).map(item => ({ name: item.category || '未分类', value: item.count })))

      const activityStatusMap = { 0: '未开始', 1: '进行中', 2: '已结束' }
      this.renderPie(this.$refs.chartActivityStatus, '活动状态',
        (data.activitiesByStatus || []).map(item => ({ name: activityStatusMap[item.status] || '未知', value: item.count })))

      const regStatusMap = { 0: '待审核', 1: '已通过', 2: '已拒绝', 3: '已取消' }
      this.renderPie(this.$refs.chartRegStatus, '报名状态',
        (data.registrationsByStatus || []).map(item => ({ name: regStatusMap[item.status] || '未知', value: item.count })))

      const compStatusMap = { 0: '待处理', 1: '已处理', 2: '已驳回' }
      this.renderPie(this.$refs.chartComplaintStatus, '投诉状态',
        (data.complaintsByStatus || []).map(item => ({ name: compStatusMap[item.status] || '未知', value: item.count })))

      const workStatusMap = { 0: '已下架', 1: '正常' }
      this.renderPie(this.$refs.chartWorkStatus, '作品状态',
        (data.worksByStatus || []).map(item => ({ name: workStatusMap[item.status] || '未知', value: item.count })))

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
        color: ['#F0B90B', '#0ECB81', '#F6465D', '#1EAEDB', '#848E9C', '#D0980B'],
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
        series: [{ name: title, type: 'bar', data: values, itemStyle: { color: '#F0B90B', borderRadius: [4, 4, 0, 0] }, barMaxWidth: 40 }]
      })
    }
  }
}
</script>

<style scoped>
.stat-card {
  background: var(--color-surface);
  border-radius: var(--radius-card);
  border: 1px solid var(--color-border);
  box-shadow: var(--shadow-card);
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
}
.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-input);
  display: flex;
  align-items: center;
  justify-content: center;
}
.stat-icon i {
  font-size: 24px;
}
.stat-num {
  font-size: 24px;
  font-weight: 700;
  color: var(--color-text);
  line-height: 1;
}
.stat-label {
  font-size: 13px;
  color: var(--color-text-slate);
  margin-top: 4px;
  font-weight: 500;
}
.chart-card {
  background: var(--color-surface);
  border-radius: var(--radius-card);
  border: 1px solid var(--color-border);
  box-shadow: var(--shadow-card);
  padding: 20px;
}
.chart-header {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text);
  margin-bottom: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--color-border);
}
</style>
