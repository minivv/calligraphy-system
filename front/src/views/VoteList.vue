<template>
  <div>
    <el-card>
      <div slot="header"><span style="font-size:18px;font-weight:bold">作品投票</span></div>
      <el-form :inline="true">
        <el-form-item><el-input v-model="query.title" placeholder="搜索投票" clearable></el-input></el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">搜索</el-button></el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="8" v-for="item in list" :key="item.id">
          <el-card shadow="hover" class="item-card" @click.native="$router.push('/vote/'+item.id)">
            <div style="padding:20px">
              <h3>{{ item.title }}</h3>
              <p style="color:#999;margin:10px 0;font-size:13px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">{{ item.description }}</p>
              <div style="display:flex;justify-content:space-between;align-items:center">
                <el-tag size="small" :type="['info','success','danger'][item.status]">{{ ['未开始','进行中','已结束'][item.status] }}</el-tag>
                <span style="color:#999;font-size:12px">{{ item.startTime }}</span>
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
      const res = await this.$request.get('/api/front/vote/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize, ...this.query } })
      this.list = res.data.records; this.total = res.data.total
    },
    handlePage(val) { this.pageNum = val; this.loadData() }
  }
}
</script>

<style scoped>
.item-card { cursor:pointer; margin-bottom:15px; }
.item-card:hover { transform:translateY(-3px); transition:0.3s; }
</style>
