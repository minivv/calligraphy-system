<template>
  <div>
    <el-card v-if="video">
      <h2>{{ video.title }}</h2>
      <div style="color:#999;margin:10px 0">
        <span>上传者：{{ video.nickname || video.username }}</span>
        <span style="margin-left:20px">{{ video.createTime }}</span>
      </div>
      <el-divider></el-divider>
      <div v-if="video.videoUrl" style="text-align:center">
        <video :src="video.videoUrl" controls style="max-width:100%;max-height:500px"></video>
      </div>
      <div v-else style="background:#000;height:300px;display:flex;align-items:center;justify-content:center;color:#fff;border-radius:4px">
        <div style="text-align:center">
          <i class="el-icon-video-camera" style="font-size:60px"></i>
          <p style="margin-top:10px">视频暂未上传</p>
        </div>
      </div>
      <p style="margin-top:20px;line-height:1.8;white-space:pre-wrap">{{ video.description }}</p>
      <el-divider></el-divider>
      <div style="display:flex;align-items:center;gap:20px">
        <el-button :type="likeStatus===1?'primary':'default'" @click="doLike(1)">
          <i class="el-icon-thumb"></i> 点赞 ({{ video.likeCount }})
        </el-button>
        <el-button :type="likeStatus===2?'danger':'default'" @click="doLike(2)">
          <i class="el-icon-thumb" style="transform:rotate(180deg);display:inline-block"></i> 踩 ({{ video.dislikeCount }})
        </el-button>
        <el-button type="warning" size="small" @click="openComplaint">投诉</el-button>
      </div>
    </el-card>

    <el-card style="margin-top:20px">
      <h3>评论 ({{ video ? video.commentCount : 0 }})</h3>
      <div style="margin:15px 0" v-if="isLogin">
        <el-input v-model="commentContent" type="textarea" :rows="3" placeholder="写下你的评论..."></el-input>
        <el-button type="primary" style="margin-top:10px" @click="submitComment">发表评论</el-button>
      </div>
      <div v-else style="margin:15px 0;color:#999"><router-link to="/login">登录</router-link> 后可以发表评论</div>
      <el-divider></el-divider>
      <div v-for="c in comments" :key="c.id" style="padding:10px 0;border-bottom:1px solid #f0f0f0">
        <div style="display:flex;justify-content:space-between">
          <span style="font-weight:bold">{{ c.nickname || c.username }}</span>
          <span style="color:#999;font-size:12px">{{ c.createTime }}</span>
        </div>
        <p style="margin-top:8px;color:#666">{{ c.content }}</p>
      </div>
      <el-pagination v-if="commentTotal>0" style="text-align:center;margin-top:15px" @current-change="loadComments" :page-size="10" :total="commentTotal" layout="prev, pager, next"></el-pagination>
    </el-card>

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
