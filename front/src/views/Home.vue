<template>
  <div>
    <!-- Hero Banner -->
    <div class="hero">
      <div class="hero-content">
        <h1 class="hero-title">探索书法之美</h1>
        <p class="hero-subtitle">传承经典，交流艺术，共享书法文化</p>
        <div class="hero-actions">
          <el-button type="primary" size="medium" @click="$router.push('/works')">浏览作品</el-button>
          <el-button size="medium" @click="$router.push('/activities')">查看活动</el-button>
        </div>
      </div>
    </div>

    <!-- Latest Works -->
    <section class="section">
      <div class="section-header">
        <h2 class="section-title">最新书法作品</h2>
        <span class="section-more" @click="$router.push('/works')">查看更多 <i class="el-icon-arrow-right"></i></span>
      </div>
      <el-row :gutter="24">
        <el-col :span="6" v-for="item in homeData.works" :key="item.id">
          <div class="work-card" @click="$router.push('/work/'+item.id)">
            <div class="work-card-cover">
              <el-image v-if="item.imageUrl" :src="item.imageUrl" style="width:100%;height:100%" fit="cover"></el-image>
              <div v-else class="work-card-placeholder">
                <i class="el-icon-picture"></i>
              </div>
            </div>
            <div class="work-card-body">
              <h4 class="work-card-title">{{ item.title }}</h4>
              <div class="work-card-meta">
                <span class="meta-author">{{ item.nickname || item.username }}</span>
                <span class="meta-stat"><i class="el-icon-thumb"></i> {{ item.likeCount }}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </section>

    <!-- Latest Activities -->
    <section class="section">
      <div class="section-header">
        <h2 class="section-title">最新交流活动</h2>
        <span class="section-more" @click="$router.push('/activities')">查看更多 <i class="el-icon-arrow-right"></i></span>
      </div>
      <el-row :gutter="24">
        <el-col :span="8" v-for="item in homeData.activities" :key="item.id">
          <div class="activity-card" @click="$router.push('/activity/'+item.id)">
            <div class="activity-card-body">
              <h4 class="activity-card-title">{{ item.title }}</h4>
              <p class="activity-card-info"><i class="el-icon-location-outline"></i> {{ item.location }}</p>
              <p class="activity-card-info"><i class="el-icon-time"></i> {{ item.startTime }}</p>
              <div class="activity-card-footer">
                <el-tag size="mini" :type="['info','success','danger'][item.status]" effect="plain">{{ ['未开始','进行中','已结束'][item.status] }}</el-tag>
                <span class="meta-stat">{{ item.currentParticipants }}/{{ item.maxParticipants }}人</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </section>

    <!-- Latest Videos -->
    <section class="section">
      <div class="section-header">
        <h2 class="section-title">最新书法视频</h2>
        <span class="section-more" @click="$router.push('/videos')">查看更多 <i class="el-icon-arrow-right"></i></span>
      </div>
      <el-row :gutter="24">
        <el-col :span="8" v-for="item in homeData.videos" :key="item.id">
          <div class="video-card" @click="$router.push('/video/'+item.id)">
            <div class="video-card-cover">
              <el-image v-if="item.coverImage" :src="item.coverImage" style="width:100%;height:100%" fit="cover"></el-image>
              <div class="video-play-icon"><i class="el-icon-video-play"></i></div>
            </div>
            <div class="video-card-body">
              <h4 class="work-card-title">{{ item.title }}</h4>
              <div class="work-card-meta">
                <span class="meta-author">{{ item.nickname || item.username }}</span>
                <span class="meta-stat"><i class="el-icon-thumb"></i> {{ item.likeCount }}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </section>
  </div>
</template>

<script>
export default {
  data() {
    return { homeData: { works: [], activities: [], videos: [] } }
  },
  async created() {
    try {
      const res = await this.$request.get('/api/front/home')
      this.homeData = res.data
    } catch (e) {}
  }
}
</script>

<style scoped>
/* Hero */
.hero {
  background: linear-gradient(135deg, #0d1b3e 0%, #1b61c9 100%);
  border-radius: var(--radius-section);
  padding: 80px 48px;
  margin-bottom: 40px;
  text-align: center;
}
.hero-title {
  font-size: 48px;
  font-weight: 400;
  color: #fff;
  line-height: 1.15;
  margin-bottom: 12px;
}
.hero-subtitle {
  font-size: 18px;
  color: rgba(255,255,255,0.75);
  line-height: 1.5;
  letter-spacing: 0.18px;
  margin-bottom: 32px;
}
.hero-actions {
  display: flex;
  justify-content: center;
  gap: 12px;
}
.hero-actions .el-button--default {
  background: rgba(255,255,255,0.15) !important;
  border-color: rgba(255,255,255,0.3) !important;
  color: #fff !important;
}
.hero-actions .el-button--default:hover {
  background: rgba(255,255,255,0.25) !important;
  border-color: rgba(255,255,255,0.5) !important;
}

/* Section */
.section {
  margin-bottom: 40px;
}
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.section-title {
  font-size: 24px;
  font-weight: 400;
  color: var(--color-text);
  letter-spacing: 0.12px;
  line-height: 1.3;
}
.section-more {
  font-size: 14px;
  color: var(--color-primary);
  cursor: pointer;
  font-weight: 500;
  letter-spacing: var(--letter-spacing-button);
  transition: color 0.2s;
}
.section-more:hover { color: var(--color-primary-hover); }

/* Work Card */
.work-card {
  background: var(--color-surface);
  border-radius: var(--radius-card);
  border: 1px solid var(--color-border);
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
  box-shadow: var(--shadow-soft);
  margin-bottom: 24px;
}
.work-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-hover);
}
.work-card-cover {
  width: 100%;
  height: 200px;
  overflow: hidden;
  background: var(--color-surface-subtle);
}
.work-card-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-border);
  font-size: 40px;
}
.work-card-body {
  padding: 16px;
}
.work-card-title {
  font-size: 16px;
  font-weight: 500;
  color: var(--color-text);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  letter-spacing: 0.08px;
  line-height: 1.3;
}
.work-card-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}
.meta-author {
  font-size: 13px;
  color: var(--color-text-weak);
  letter-spacing: var(--letter-spacing-caption);
}
.meta-stat {
  font-size: 13px;
  color: var(--color-text-muted);
  letter-spacing: var(--letter-spacing-caption);
}

/* Activity Card */
.activity-card {
  background: var(--color-surface);
  border-radius: var(--radius-card);
  border: 1px solid var(--color-border);
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
  box-shadow: var(--shadow-soft);
  margin-bottom: 24px;
}
.activity-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-hover);
}
.activity-card-body {
  padding: 24px;
}
.activity-card-title {
  font-size: 18px;
  font-weight: 500;
  color: var(--color-text);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 12px;
  letter-spacing: 0.1px;
}
.activity-card-info {
  font-size: 13px;
  color: var(--color-text-weak);
  margin-bottom: 6px;
  letter-spacing: var(--letter-spacing-caption);
}
.activity-card-info i {
  margin-right: 4px;
  color: var(--color-text-muted);
}
.activity-card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
}

/* Video Card */
.video-card {
  background: var(--color-surface);
  border-radius: var(--radius-card);
  border: 1px solid var(--color-border);
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.25s ease, box-shadow 0.25s ease;
  box-shadow: var(--shadow-soft);
  margin-bottom: 24px;
}
.video-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-hover);
}
.video-card-cover {
  width: 100%;
  height: 180px;
  background: #0d1b3e;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}
.video-play-icon {
  position: absolute;
  font-size: 48px;
  color: rgba(255,255,255,0.85);
  transition: transform 0.2s;
}
.video-card:hover .video-play-icon {
  transform: scale(1.1);
}
.video-card-body {
  padding: 16px;
}
</style>
