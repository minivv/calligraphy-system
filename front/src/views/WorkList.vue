<template>
  <div>
    <div class="page-header">
      <h2 class="page-title">书法作品</h2>
      <div class="search-bar">
        <el-input v-model="query.title" placeholder="搜索作品" clearable prefix-icon="el-icon-search" style="width:220px" @keyup.enter.native="loadData"></el-input>
        <el-select v-model="query.category" placeholder="类别" clearable style="width:140px">
          <el-option v-for="c in categories" :key="c" :label="c" :value="c"></el-option>
        </el-select>
        <el-button type="primary" @click="loadData">搜索</el-button>
      </div>
    </div>
    <el-row :gutter="24">
      <el-col :span="6" v-for="item in list" :key="item.id">
        <div class="work-card" @click="$router.push('/work/'+item.id)">
          <div class="work-card-cover">
            <el-image v-if="item.imageUrl" :src="item.imageUrl" style="width:100%;height:100%" fit="cover"></el-image>
            <div v-else class="work-card-placeholder">
              <i class="el-icon-picture"></i>
            </div>
          </div>
          <div class="work-card-body">
            <h4 class="work-card-title">{{ item.title }}</h4>
            <el-tag size="mini" effect="plain" style="margin-top:6px">{{ item.category }}</el-tag>
            <div class="work-card-meta">
              <span class="meta-author">{{ item.nickname || item.username }}</span>
              <span class="meta-stat">
                <i class="el-icon-thumb"></i> {{ item.likeCount }}
                <i class="el-icon-chat-dot-round" style="margin-left:8px"></i> {{ item.commentCount }}
              </span>
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
  data() { return { list: [], query: { title: '', category: '' }, categories: ['楷书','行书','草书','隶书','篆书'], pageNum: 1, pageSize: 12, total: 0 } },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/front/work/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize, ...this.query } })
      this.list = res.data.records; this.total = res.data.total
    },
    handlePage(val) { this.pageNum = val; this.loadData() }
  }
}
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}
.page-title {
  font-size: 24px;
  font-weight: 400;
  color: var(--color-text);
  letter-spacing: 0.12px;
}
.search-bar {
  display: flex;
  align-items: center;
  gap: 8px;
}
.work-card {
  background: var(--color-surface);
  border-radius: var(--radius-card);
  border: 1px solid var(--color-border);
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
  box-shadow: var(--shadow-soft);
  margin-bottom: 24px;
}
.work-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-hover);
}
.work-card-cover {
  width: 100%;
  height: 200px;
  overflow: hidden;
  background: var(--color-surface-subtle);
}
.work-card-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-border);
  font-size: 40px;
}
.work-card-body { padding: 16px; }
.work-card-title {
  font-size: 16px;
  font-weight: 500;
  color: var(--color-text);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  letter-spacing: 0.08px;
}
.work-card-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}
.meta-author {
  font-size: 13px;
  color: var(--color-text-weak);
  letter-spacing: var(--letter-spacing-caption);
}
.meta-stat {
  font-size: 13px;
  color: var(--color-text-muted);
  letter-spacing: var(--letter-spacing-caption);
}
.pagination-wrap {
  text-align: center;
  margin-top: 16px;
}
</style>
