<template>
  <div>
    <div class="page-header">
      <h2 class="page-title">书法视频</h2>
      <div class="search-bar">
        <el-input v-model="query.title" placeholder="搜索视频" clearable prefix-icon="el-icon-search" style="width:220px" @keyup.enter.native="loadData"></el-input>
        <el-button type="primary" @click="loadData">搜索</el-button>
      </div>
    </div>
    <el-row :gutter="24">
      <el-col :span="8" v-for="item in list" :key="item.id">
        <div class="video-card" @click="$router.push('/video/'+item.id)">
          <div class="video-card-cover">
            <el-image v-if="item.coverImage" :src="item.coverImage" style="width:100%;height:100%" fit="cover"></el-image>
            <div class="video-play-icon"><i class="el-icon-video-play"></i></div>
          </div>
          <div class="video-card-body">
            <h4 class="video-card-title">{{ item.title }}</h4>
            <div class="video-card-meta">
              <span class="meta-author">{{ item.nickname || item.username }}</span>
              <span class="meta-stat"><i class="el-icon-thumb"></i> {{ item.likeCount }} <i class="el-icon-chat-dot-round" style="margin-left:8px"></i> {{ item.commentCount }}</span>
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
      const res = await this.$request.get('/api/front/video/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize, ...this.query } })
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
.video-card {
  background: var(--color-surface); border-radius: var(--radius-card); border: 1px solid var(--color-border);
  overflow: hidden; cursor: pointer; box-shadow: var(--shadow-card); transition: box-shadow 200ms ease; margin-bottom: 24px;
}
.video-card:hover { box-shadow: var(--shadow-card-hover); }
.video-card-cover {
  width: 100%; height: 200px; background: var(--color-dark); position: relative;
  display: flex; align-items: center; justify-content: center;
}
.video-play-icon { position: absolute; font-size: 48px; color: rgba(255,255,255,0.85); transition: color 200ms; }
.video-card:hover .video-play-icon { color: var(--color-primary); }
.video-card-body { padding: 16px; }
.video-card-title { font-size: 16px; font-weight: 600; color: var(--color-text); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.video-card-meta { display: flex; justify-content: space-between; align-items: center; margin-top: 10px; }
.meta-author { font-size: 13px; color: var(--color-text-slate); font-weight: 500; }
.meta-stat { font-size: 13px; color: var(--color-text-muted); font-weight: 500; }
.pagination-wrap { text-align: center; margin-top: 16px; }
</style>
