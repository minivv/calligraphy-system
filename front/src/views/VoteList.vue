<template>
  <div>
    <div class="page-header">
      <h2 class="page-title">作品投票</h2>
      <div class="search-bar">
        <el-input v-model="query.title" placeholder="搜索投票" clearable prefix-icon="el-icon-search" style="width:220px" @keyup.enter.native="loadData"></el-input>
        <el-button type="primary" @click="loadData">搜索</el-button>
      </div>
    </div>
    <el-row :gutter="24">
      <el-col :span="8" v-for="item in list" :key="item.id">
        <div class="vote-card" @click="$router.push('/vote/'+item.id)">
          <div class="vote-card-body">
            <h3 class="vote-card-title">{{ item.title }}</h3>
            <p class="vote-card-desc">{{ item.description }}</p>
            <div class="vote-card-footer">
              <el-tag size="mini" :type="['info','success','danger'][item.status]">{{ ['未开始','进行中','已结束'][item.status] }}</el-tag>
              <span class="meta-stat">{{ item.startTime }}</span>
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
  data() { return { list: [], query: { title: '' }, pageNum: 1, pageSize: 12, total: 0 } },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/front/vote/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize, ...this.query } })
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
.vote-card {
  background: var(--color-surface); border-radius: var(--radius-card); border: 1px solid var(--color-border);
  overflow: hidden; cursor: pointer; box-shadow: var(--shadow-card); transition: box-shadow 200ms ease; margin-bottom: 24px;
}
.vote-card:hover { box-shadow: var(--shadow-card-hover); }
.vote-card-body { padding: 24px; }
.vote-card-title { font-size: 20px; font-weight: 600; color: var(--color-text); margin-bottom: 10px; }
.vote-card-desc { font-size: 14px; color: var(--color-text-slate); margin-bottom: 16px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; font-weight: 500; line-height: 1.43; }
.vote-card-footer { display: flex; justify-content: space-between; align-items: center; }
.meta-stat { font-size: 13px; color: var(--color-text-muted); font-weight: 500; }
.pagination-wrap { text-align: center; margin-top: 16px; }
</style>
