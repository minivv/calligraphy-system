<template>
  <div>
    <el-card v-if="work">
      <h2>{{ work.title }}</h2>
      <div style="color:#999;margin:10px 0">
        <span>作者：{{ work.nickname || work.username }}</span>
        <span style="margin-left:20px">类别：<el-tag size="mini">{{ work.category }}</el-tag></span>
        <span style="margin-left:20px">{{ work.createTime }}</span>
      </div>
      <el-divider></el-divider>
      <el-image v-if="work.imageUrl" :src="work.imageUrl" style="max-width:100%;max-height:500px" fit="contain" :preview-src-list="[work.imageUrl]"></el-image>
      <p style="margin-top:20px;line-height:1.8;white-space:pre-wrap">{{ work.description }}</p>
      <el-divider></el-divider>
      <!-- Like/Dislike -->
      <div style="display:flex;align-items:center;gap:20px">
        <el-button :type="likeStatus===1?'primary':'default'" @click="doLike(1)">
          <i class="el-icon-thumb"></i> 点赞 ({{ work.likeCount }})
        </el-button>
        <el-button :type="likeStatus===2?'danger':'default'" @click="doLike(2)">
          <i class="el-icon-thumb" style="transform:rotate(180deg);display:inline-block"></i> 踩 ({{ work.dislikeCount }})
        </el-button>
        <el-button type="warning" size="small" @click="openComplaint">投诉</el-button>
      </div>
    </el-card>

    <!-- Comments -->
    <el-card style="margin-top:20px">
      <h3>评论 ({{ work ? work.commentCount : 0 }})</h3>
      <div style="margin:15px 0" v-if="isLogin">
        <el-input v-model="commentContent" type="textarea" :rows="3" placeholder="写下你的评论..."></el-input>
        <el-button type="primary" style="margin-top:10px" @click="submitComment">发表评论</el-button>
      </div>
      <div v-else style="margin:15px 0;color:#999">
        <router-link to="/login">登录</router-link> 后可以发表评论
      </div>
      <el-divider></el-divider>
      <div v-for="c in comments" :key="c.id" style="padding:10px 0;border-bottom:1px solid #f0f0f0">
        <div style="display:flex;justify-content:space-between">
          <span style="font-weight:bold;color:#333">{{ c.nickname || c.username }}</span>
          <span style="color:#999;font-size:12px">{{ c.createTime }}</span>
        </div>
        <p style="margin-top:8px;color:#666">{{ c.content }}</p>
      </div>
      <el-pagination v-if="commentTotal>0" style="text-align:center;margin-top:15px" @current-change="loadComments" :page-size="10" :total="commentTotal" layout="prev, pager, next"></el-pagination>
    </el-card>

    <!-- Complaint Dialog -->
    <el-dialog title="投诉" :visible.sync="complaintVisible" width="500px">
      <el-form :model="complaintForm" label-width="80px">
        <el-form-item label="标题"><el-input v-model="complaintForm.title"></el-input></el-form-item>
        <el-form-item label="内容"><el-input v-model="complaintForm.content" type="textarea" :rows="4"></el-input></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="complaintVisible=false">取消</el-button>
        <el-button type="primary" @click="submitComplaint">提交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      work: null, likeStatus: 0, comments: [], commentTotal: 0, commentContent: '',
      complaintVisible: false, complaintForm: { title: '', content: '' }
    }
  },
  computed: {
    isLogin() { return !!localStorage.getItem('user') }
  },
  created() { this.loadWork(); this.loadComments(1) },
  methods: {
    async loadWork() {
      const res = await this.$request.get('/api/front/work/' + this.$route.params.id)
      this.work = res.data
      if (this.isLogin) {
        try {
          const lr = await this.$request.get('/api/like/status', { params: { targetType: 'work', targetId: this.work.id } })
          this.likeStatus = lr.data ? lr.data.type : 0
        } catch (e) {}
      }
    },
    async loadComments(page) {
      const res = await this.$request.get('/api/front/comment/page', { params: { targetType: 'work', targetId: this.$route.params.id, pageNum: page || 1 } })
      this.comments = res.data.records; this.commentTotal = res.data.total
    },
    async doLike(type) {
      if (!this.isLogin) return this.$message.warning('请先登录')
      await this.$request.post('/api/like', { targetType: 'work', targetId: this.work.id, type })
      this.loadWork()
    },
    async submitComment() {
      if (!this.commentContent.trim()) return this.$message.warning('请输入评论内容')
      await this.$request.post('/api/comment', { targetType: 'work', targetId: this.work.id, content: this.commentContent })
      this.commentContent = ''; this.loadWork(); this.loadComments(1)
    },
    openComplaint() {
      if (!this.isLogin) return this.$message.warning('请先登录')
      this.complaintForm = { title: '', content: '' }; this.complaintVisible = true
    },
    async submitComplaint() {
      await this.$request.post('/api/complaint', { ...this.complaintForm, targetType: 'work', targetId: this.work.id })
      this.$message.success('投诉已提交'); this.complaintVisible = false
    }
  }
}
</script>
