<template>
  <div>
    <div class="page-header">
      <h2 class="page-title">交流活动</h2>
      <div class="search-bar">
        <el-input v-model="query.title" placeholder="搜索活动" clearable prefix-icon="el-icon-search" style="width:220px" @keyup.enter.native="loadData"></el-input>
        <el-select v-model="query.status" placeholder="状态" clearable style="width:140px">
          <el-option label="未开始" :value="0"></el-option>
          <el-option label="进行中" :value="1"></el-option>
          <el-option label="已结束" :value="2"></el-option>
        </el-select>
        <el-button type="primary" @click="loadData">搜索</el-button>
      </div>
    </div>
    <el-row :gutter="24">
      <el-col :span="8" v-for="item in list" :key="item.id">
        <div class="activity-card" @click="$router.push('/activity/'+item.id)">
          <div class="activity-card-cover">
            <el-image v-if="item.coverImage" :src="item.coverImage" style="width:100%;height:100%" fit="cover"></el-image>
            <div v-else class="activity-card-cover-fallback"><i class="el-icon-date"></i></div>
          </div>
          <div class="activity-card-body">
            <h4 class="activity-card-title">{{ item.title }}</h4>
            <p class="activity-card-info"><i class="el-icon-location-outline"></i> {{ item.location }}</p>
            <p class="activity-card-info"><i class="el-icon-time"></i> {{ item.startTime }}</p>
            <div class="activity-card-footer">
              <el-tag size="mini" :type="['info','success','danger'][item.status]">{{ ['未开始','进行中','已结束'][item.status] }}</el-tag>
              <span class="meta-stat">{{ item.currentParticipants }}/{{ item.maxParticipants }}人</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
    <div class="pagination-wrap">
      <el-pagination @current-change="handlePage" :current-page="pageNum" :page-size="pageSize" :total="total" layout="prev, pager, next"></el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  data() { return { list: [], query: { title: '', status: null }, pageNum: 1, pageSize: 12, total: 0 } },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/front/activity/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize, ...this.query } })
      this.list = res.data.records; this.total = res.data.total
    },
    handlePage(val) { this.pageNum = val; this.loadData() }
  }
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.page-title { font-size: 28px; font-weight: 700; color: var(--color-text); }
.search-bar { display: flex; align-items: center; gap: 8px; }
.activity-card {
  background: var(--color-surface); border-radius: var(--radius-card); border: 1px solid var(--color-border);
  overflow: hidden; cursor: pointer; box-shadow: var(--shadow-card); transition: box-shadow 200ms ease; margin-bottom: 24px;
}
.activity-card:hover { box-shadow: var(--shadow-card-hover); }
.activity-card-cover { width: 100%; height: 200px; overflow: hidden; }
.activity-card-cover-fallback {
  width: 100%; height: 100%; background: var(--color-dark); display: flex; align-items: center; justify-content: center;
  color: var(--color-primary); font-size: 48px;
}
.activity-card-body { padding: 20px; }
.activity-card-title { font-size: 18px; font-weight: 600; color: var(--color-text); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; margin-bottom: 10px; }
.activity-card-info { font-size: 14px; color: var(--color-text-slate); margin-bottom: 6px; font-weight: 500; }
.activity-card-info i { margin-right: 4px; }
.activity-card-footer { display: flex; justify-content: space-between; align-items: center; margin-top: 12px; }
.meta-stat { font-size: 13px; color: var(--color-text-muted); font-weight: 500; }
.pagination-wrap { text-align: center; margin-top: 16px; }
</style>
