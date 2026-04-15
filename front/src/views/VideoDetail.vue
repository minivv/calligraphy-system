<template>
  <div>
    <div class="detail-card" v-if="video">
      <h2 class="detail-title">{{ video.title }}</h2>
      <div class="detail-meta">
        <span class="meta-item">上传者：{{ video.nickname || video.username }}</span>
        <span class="meta-item meta-time">{{ video.createTime }}</span>
      </div>
      <div class="detail-divider"></div>
      <div v-if="video.videoUrl" class="video-player">
        <video :src="video.videoUrl" controls style="max-width:100%;max-height:500px;border-radius:var(--radius-button)"></video>
      </div>
      <div v-else class="video-placeholder">
        <i class="el-icon-video-camera"></i>
        <p>视频暂未上传</p>
      </div>
      <p class="detail-content">{{ video.description }}</p>
      <div class="detail-divider"></div>
      <div class="action-bar">
        <el-button :type="likeStatus===1?'primary':'default'" @click="doLike(1)">
          <i class="el-icon-thumb"></i> 点赞 ({{ video.likeCount }})
        </el-button>
        <el-button :type="likeStatus===2?'danger':'default'" @click="doLike(2)">
          <i class="el-icon-thumb" style="transform:rotate(180deg);display:inline-block"></i> 踩 ({{ video.dislikeCount }})
        </el-button>
        <el-button type="warning" size="small" @click="openComplaint">投诉</el-button>
      </div>
    </div>

    <div class="detail-card" style="margin-top:24px">
      <h3 class="section-title">评论 ({{ video ? video.commentCount : 0 }})</h3>
      <div class="comment-input" v-if="isLogin">
        <el-input v-model="commentContent" type="textarea" :rows="3" placeholder="写下你的评论..."></el-input>
        <el-button type="primary" size="small" style="margin-top:12px" @click="submitComment">发表评论</el-button>
      </div>
      <div v-else class="login-prompt"><router-link to="/login">登录</router-link> 后可以发表评论</div>
      <div class="detail-divider"></div>
      <div v-for="c in comments" :key="c.id" class="comment-item">
        <div class="comment-header">
          <span class="comment-author">{{ c.nickname || c.username }}</span>
          <span class="comment-time">{{ c.createTime }}</span>
        </div>
        <p class="comment-content">{{ c.content }}</p>
      </div>
      <div class="pagination-wrap" v-if="commentTotal>0">
        <el-pagination @current-change="loadComments" :page-size="10" :total="commentTotal" layout="prev, pager, next"></el-pagination>
      </div>
    </div>

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
      video: null, likeStatus: 0, comments: [], commentTotal: 0, commentContent: '',
      complaintVisible: false, complaintForm: { title: '', content: '' }
    }
  },
  computed: { isLogin() { return !!localStorage.getItem('user') } },
  created() { this.loadVideo(); this.loadComments(1) },
  methods: {
    async loadVideo() {
      const res = await this.$request.get('/api/front/video/' + this.$route.params.id)
      this.video = res.data
      if (this.isLogin) {
        try {
          const lr = await this.$request.get('/api/like/status', { params: { targetType: 'video', targetId: this.video.id } })
          this.likeStatus = lr.data ? lr.data.type : 0
        } catch (e) {}
      }
    },
    async loadComments(page) {
      const res = await this.$request.get('/api/front/comment/page', { params: { targetType: 'video', targetId: this.$route.params.id, pageNum: page || 1 } })
      this.comments = res.data.records; this.commentTotal = res.data.total
    },
    async doLike(type) {
      if (!this.isLogin) return this.$message.warning('请先登录')
      await this.$request.post('/api/like', { targetType: 'video', targetId: this.video.id, type })
      this.loadVideo()
    },
    async submitComment() {
      if (!this.commentContent.trim()) return this.$message.warning('请输入评论内容')
      await this.$request.post('/api/comment', { targetType: 'video', targetId: this.video.id, content: this.commentContent })
      this.commentContent = ''; this.loadVideo(); this.loadComments(1)
    },
    openComplaint() {
      if (!this.isLogin) return this.$message.warning('请先登录')
      this.complaintForm = { title: '', content: '' }; this.complaintVisible = true
    },
    async submitComplaint() {
      await this.$request.post('/api/complaint', { ...this.complaintForm, targetType: 'video', targetId: this.video.id })
      this.$message.success('投诉已提交'); this.complaintVisible = false
    }
  }
}
</script>

<style scoped>
.detail-card {
  background: var(--color-surface);
  border-radius: var(--radius-card);
  border: 1px solid var(--color-border);
  box-shadow: var(--shadow-soft);
  padding: 32px;
}
.detail-title {
  font-size: 28px;
  font-weight: 400;
  color: var(--color-text);
  letter-spacing: 0.12px;
  line-height: 1.25;
}
.detail-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-top: 12px;
}
.meta-item {
  font-size: 14px;
  color: var(--color-text-weak);
  letter-spacing: var(--letter-spacing-caption);
}
.meta-time { color: var(--color-text-muted); }
.detail-divider {
  height: 1px;
  background: var(--color-border-light);
  margin: 24px 0;
}
.video-player {
  text-align: center;
}
.video-placeholder {
  background: #0d1b3e;
  height: 300px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: rgba(255,255,255,0.7);
  border-radius: var(--radius-button);
}
.video-placeholder i { font-size: 60px; }
.video-placeholder p { margin-top: 12px; font-size: 14px; }
.detail-content {
  margin-top: 20px;
  font-size: 16px;
  line-height: 1.75;
  color: var(--color-text-secondary);
  white-space: pre-wrap;
  letter-spacing: var(--letter-spacing-body);
}
.action-bar {
  display: flex;
  align-items: center;
  gap: 12px;
}
.section-title {
  font-size: 18px;
  font-weight: 500;
  color: var(--color-text);
  margin-bottom: 16px;
  letter-spacing: 0.1px;
}
.comment-input { margin-bottom: 8px; }
.login-prompt {
  font-size: 14px;
  color: var(--color-text-weak);
  margin-bottom: 8px;
}
.comment-item {
  padding: 16px 0;
  border-bottom: 1px solid var(--color-border-light);
}
.comment-item:last-child { border-bottom: none; }
.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.comment-author {
  font-size: 14px;
  font-weight: 500;
  color: var(--color-text);
}
.comment-time {
  font-size: 12px;
  color: var(--color-text-muted);
  letter-spacing: var(--letter-spacing-caption);
}
.comment-content {
  margin-top: 8px;
  font-size: 14px;
  color: var(--color-text-secondary);
  line-height: 1.5;
  letter-spacing: var(--letter-spacing-body);
}
.pagination-wrap {
  text-align: center;
  margin-top: 16px;
}
</style>
