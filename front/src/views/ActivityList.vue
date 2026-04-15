<template>
  <div>
    <el-card>
      <div slot="header"><span style="font-size:18px;font-weight:bold">交流活动</span></div>
      <el-form :inline="true">
        <el-form-item><el-input v-model="query.title" placeholder="搜索活动" clearable></el-input></el-form-item>
        <el-form-item>
          <el-select v-model="query.status" placeholder="状态" clearable>
            <el-option label="未开始" :value="0"></el-option>
            <el-option label="进行中" :value="1"></el-option>
            <el-option label="已结束" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">搜索</el-button></el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="8" v-for="item in list" :key="item.id">
          <el-card shadow="hover" class="item-card" @click.native="$router.push('/activity/'+item.id)">
            <el-image v-if="item.coverImage" :src="item.coverImage" style="width:100%;height:180px" fit="cover"></el-image>
            <div v-else style="width:100%;height:180px;background:linear-gradient(135deg,#667eea,#764ba2);display:flex;align-items:center;justify-content:center;color:#fff">
              <i class="el-icon-date" style="font-size:50px"></i>
            </div>
            <div style="padding:15px">
              <h4 class="ellipsis">{{ item.title }}</h4>
              <p style="color:#999;font-size:13px;margin:5px 0"><i class="el-icon-location"></i> {{ item.location }}</p>
              <p style="color:#999;font-size:13px"><i class="el-icon-time"></i> {{ item.startTime }}</p>
              <div style="margin-top:8px;display:flex;justify-content:space-between;align-items:center">
                <el-tag size="mini" :type="['info','success','danger'][item.status]">{{ ['未开始','进行中','已结束'][item.status] }}</el-tag>
                <span style="color:#999;font-size:12px">{{ item.currentParticipants }}/{{ item.maxParticipants }}人</span>
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
.item-card { cursor:pointer; margin-bottom:15px; }
.item-card:hover { transform:translateY(-3px); transition:0.3s; }
.ellipsis { overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }
</style>
