<template>
  <div>
    <el-card>
      <div slot="header"><span style="font-size:18px;font-weight:bold">书法视频</span></div>
      <el-form :inline="true">
        <el-form-item><el-input v-model="query.title" placeholder="搜索视频" clearable></el-input></el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">搜索</el-button></el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="8" v-for="item in list" :key="item.id">
          <el-card shadow="hover" class="item-card" @click.native="$router.push('/video/'+item.id)">
            <div style="width:100%;height:180px;background:#000;display:flex;align-items:center;justify-content:center;position:relative">
              <el-image v-if="item.coverImage" :src="item.coverImage" style="width:100%;height:100%" fit="cover"></el-image>
              <i class="el-icon-video-play" style="font-size:50px;color:#fff;position:absolute"></i>
            </div>
            <div style="padding:10px">
              <h4 class="ellipsis">{{ item.title }}</h4>
              <div style="display:flex;justify-content:space-between;color:#999;font-size:12px;margin-top:5px">
                <span>{{ item.nickname || item.username }}</span>
                <span><i class="el-icon-thumb"></i> {{ item.likeCount }} <i class="el-icon-chat-dot-round"></i> {{ item.commentCount }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-pagination style="text-align:center;margin-top:20px" @current-change="handlePage" :current-page="pageNum" :page-size="pageSize" :total="total" layout="prev, pager, next"></el-pagination>
    </el-card>
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
.item-card { cursor:pointer; margin-bottom:15px; }
.item-card:hover { transform:translateY(-3px); transition:0.3s; }
.ellipsis { overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }
</style>
