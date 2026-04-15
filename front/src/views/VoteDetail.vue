<template>
  <div>
    <div class="detail-card" v-if="vote">
      <h2 class="detail-title">{{ vote.title }}</h2>
      <div class="detail-meta">
        <el-tag size="small" :type="['info','success','danger'][vote.status]">{{ ['未开始','进行中','已结束'][vote.status] }}</el-tag>
        <span class="meta-item">{{ vote.startTime }} ~ {{ vote.endTime }}</span>
      </div>
      <p class="vote-desc">{{ vote.description }}</p>
      <div class="detail-divider"></div>
      <h3 class="section-title">投票选项</h3>
      <el-row :gutter="24">
        <el-col :span="8" v-for="item in items" :key="item.id">
          <div class="vote-item-card">
            <div class="vote-item-cover">
              <el-image v-if="item.workImage" :src="item.workImage" style="width:100%;height:100%" fit="cover"></el-image>
              <div v-else class="vote-item-placeholder"><i class="el-icon-picture"></i></div>
            </div>
            <div class="vote-item-body">
              <h4 class="vote-item-title">{{ item.workTitle || '作品#'+item.workId }}</h4>
              <p class="vote-item-author">作者：{{ item.authorName || '-' }}</p>
              <el-progress :percentage="totalVotes?Math.round(item.voteCount/totalVotes*100):0" :format="()=>item.voteCount+'票'" style="margin:12px 0"></el-progress>
              <el-button type="primary" size="small" @click="castVote(item.id)" :disabled="vote.status!==1" style="width:100%">投票</el-button>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  data() { return { vote: null, items: [] } },
  computed: { totalVotes() { return this.items.reduce((s, i) => s + i.voteCount, 0) } },
  created() { this.loadData() },
  methods: {
    async loadData() { const res = await this.$request.get('/api/front/vote/' + this.$route.params.id); this.vote = res.data.vote; this.items = res.data.items },
    async castVote(itemId) { if (!localStorage.getItem('user')) return this.$message.warning('请先登录'); try { await this.$request.post('/api/vote/cast/' + itemId); this.$message.success('投票成功'); this.loadData() } catch (e) {} }
  }
}
</script>

<style scoped>
.detail-card { background: var(--color-surface); border-radius: var(--radius-card); border: 1px solid var(--color-border); box-shadow: var(--shadow-card); padding: 32px; }
.detail-title { font-size: 28px; font-weight: 700; color: var(--color-text); line-height: 1.0; }
.detail-meta { display: flex; align-items: center; gap: 16px; margin-top: 12px; }
.meta-item { font-size: 14px; color: var(--color-text-slate); font-weight: 500; }
.vote-desc { margin-top: 16px; font-size: 16px; color: var(--color-text-secondary); line-height: 1.5; font-weight: 500; }
.detail-divider { height: 1px; background: var(--color-border); margin: 24px 0; }
.section-title { font-size: 20px; font-weight: 600; color: var(--color-text); margin-bottom: 20px; }
.vote-item-card { background: var(--color-surface); border-radius: var(--radius-card); border: 1px solid var(--color-border); overflow: hidden; margin-bottom: 24px; box-shadow: var(--shadow-card); transition: box-shadow 200ms; }
.vote-item-card:hover { box-shadow: var(--shadow-card-hover); }
.vote-item-cover { width: 100%; height: 160px; overflow: hidden; background: var(--color-surface-snow); }
.vote-item-placeholder { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; color: var(--color-border); font-size: 40px; }
.vote-item-body { padding: 16px; text-align: center; }
.vote-item-title { font-size: 16px; font-weight: 600; color: var(--color-text); margin-bottom: 4px; }
.vote-item-author { font-size: 13px; color: var(--color-text-slate); font-weight: 500; }
</style>
