<template>
  <div>
    <el-card v-if="vote">
      <h2>{{ vote.title }}</h2>
      <div style="color:#999;margin:10px 0">
        <el-tag size="small" :type="['info','success','danger'][vote.status]">{{ ['未开始','进行中','已结束'][vote.status] }}</el-tag>
        <span style="margin-left:15px">{{ vote.startTime }} ~ {{ vote.endTime }}</span>
      </div>
      <p style="margin:10px 0;color:#666">{{ vote.description }}</p>
      <el-divider></el-divider>
      <h3 style="margin-bottom:15px">投票选项</h3>
      <el-row :gutter="20">
        <el-col :span="8" v-for="item in items" :key="item.id">
          <el-card shadow="hover" style="margin-bottom:15px;text-align:center">
            <el-image v-if="item.workImage" :src="item.workImage" style="width:100%;height:150px" fit="cover"></el-image>
            <div v-else style="height:150px;background:#f0f0f0;display:flex;align-items:center;justify-content:center">
              <i class="el-icon-picture" style="font-size:40px;color:#ccc"></i>
            </div>
            <h4 style="margin:10px 0">{{ item.workTitle || '作品#'+item.workId }}</h4>
            <p style="color:#999;font-size:13px">作者：{{ item.authorName || '-' }}</p>
            <div style="margin:10px 0">
              <el-progress :percentage="totalVotes?Math.round(item.voteCount/totalVotes*100):0" :format="()=>item.voteCount+'票'"></el-progress>
            </div>
            <el-button type="primary" size="small" @click="castVote(item.id)" :disabled="vote.status!==1">投票</el-button>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
export default {
  data() { return { vote: null, items: [] } },
  computed: {
    totalVotes() { return this.items.reduce((s, i) => s + i.voteCount, 0) }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/front/vote/' + this.$route.params.id)
      this.vote = res.data.vote; this.items = res.data.items
    },
    async castVote(itemId) {
      if (!localStorage.getItem('user')) return this.$message.warning('请先登录')
      try {
        await this.$request.post('/api/vote/cast/' + itemId)
        this.$message.success('投票成功'); this.loadData()
      } catch (e) {}
    }
  }
}
</script>
