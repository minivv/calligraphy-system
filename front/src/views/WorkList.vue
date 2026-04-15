<template>
  <div>
    <el-card>
      <div slot="header">
        <span style="font-size:18px;font-weight:bold">书法作品</span>
      </div>
      <el-form :inline="true">
        <el-form-item><el-input v-model="query.title" placeholder="搜索作品" clearable></el-input></el-form-item>
        <el-form-item>
          <el-select v-model="query.category" placeholder="类别" clearable>
            <el-option v-for="c in categories" :key="c" :label="c" :value="c"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">搜索</el-button></el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in list" :key="item.id">
          <el-card shadow="hover" class="item-card" @click.native="$router.push('/work/'+item.id)">
            <el-image v-if="item.imageUrl" :src="item.imageUrl" style="width:100%;height:180px" fit="cover"></el-image>
            <div v-else style="width:100%;height:180px;background:#f0f0f0;display:flex;align-items:center;justify-content:center;color:#999">
              <i class="el-icon-picture" style="font-size:40px"></i>
            </div>
            <div style="padding:10px">
              <h4 class="ellipsis">{{ item.title }}</h4>
              <el-tag size="mini" style="margin-top:5px">{{ item.category }}</el-tag>
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
.item-card { cursor:pointer; margin-bottom:15px; }
.item-card:hover { transform:translateY(-3px); transition:0.3s; }
.ellipsis { overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }
</style>
