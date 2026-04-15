<template>
  <div>
    <!-- Hero -->
    <div class="hero">
      <div class="hero-content">
        <h1 class="hero-title">探索书法之美</h1>
        <p class="hero-subtitle">传承经典，交流艺术，共享书法文化</p>
        <div class="hero-actions">
          <router-link to="/works" class="btn-hero-primary">浏览作品</router-link>
          <router-link to="/activities" class="btn-hero-secondary">查看活动</router-link>
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
              <div v-else class="work-card-placeholder"><i class="el-icon-picture"></i></div>
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
                <el-tag size="mini" :type="['info','success','danger'][item.status]">{{ ['未开始','进行中','已结束'][item.status] }}</el-tag>
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
              <h4 class="video-card-title">{{ item.title }}</h4>
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
  data() { return { homeData: { works: [], activities: [], videos: [] } } },
  async created() {
    try {
      const res = await this.$request.get('/api/front/home')
      this.homeData = res.data
    } catch (e) {}
  }
}
</script>

<style scoped>
.hero {
  background: var(--color-dark);
  border-radius: var(--radius-lg);
  padding: 80px 48px;
  text-align: center;
  margin-bottom: 48px;
}
.hero-title {
  font-size: 60px;
  font-weight: 700;
  color: #FFFFFF;
  line-height: 1.08;
  margin-bottom: 16px;
}
.hero-subtitle {
  font-size: 20px;
  font-weight: 500;
  color: var(--color-text-slate);
  line-height: 1.5;
  margin-bottom: 32px;
}
.hero-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
}
.btn-hero-primary {
  display: inline-block;
  background: var(--color-primary);
  color: var(--color-ink);
  font-size: 16px;
  font-weight: 600;
  padding: 12px 32px;
  border-radius: var(--radius-pill);
  text-decoration: none;
  letter-spacing: var(--letter-spacing-button);
  transition: background 200ms ease;
}
.btn-hero-primary:hover {
  background: var(--color-focus);
  color: #FFFFFF;
}
.btn-hero-secondary {
  display: inline-block;
  background: transparent;
  color: #FFFFFF;
  font-size: 16px;
  font-weight: 600;
  padding: 12px 32px;
  border-radius: var(--radius-pill);
  border: 1px solid rgba(255,255,255,0.3);
  text-decoration: none;
  transition: border-color 200ms ease, background 200ms ease;
}
.btn-hero-secondary:hover {
  border-color: var(--color-primary);
  background: rgba(240,185,11,0.1);
  color: var(--color-primary);
}
.section { margin-bottom: 48px; }
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.section-title {
  font-size: 24px;
  font-weight: 700;
  color: var(--color-text);
}
.section-more {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-primary);
  cursor: pointer;
  transition: color 200ms;
}
.section-more:hover { color: var(--color-primary-hover); }
.work-card {
  background: var(--color-surface);
  border-radius: var(--radius-card);
  border: 1px solid var(--color-border);
  overflow: hidden;
  cursor: pointer;
  box-shadow: var(--shadow-card);
  transition: box-shadow 200ms ease;
  margin-bottom: 24px;
}
.work-card:hover { box-shadow: var(--shadow-card-hover); }
.work-card-cover {
  width: 100%;
  height: 200px;
  overflow: hidden;
  background: var(--color-surface-snow);
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
.work-card-body { padding: 16px; }
.work-card-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.work-card-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}
.meta-author {
  font-size: 13px;
  color: var(--color-text-slate);
  font-weight: 500;
}
.meta-stat {
  font-size: 13px;
  color: var(--color-text-muted);
  font-weight: 500;
}
.activity-card {
  background: var(--color-surface);
  border-radius: var(--radius-card);
  border: 1px solid var(--color-border);
  cursor: pointer;
  box-shadow: var(--shadow-card);
  transition: box-shadow 200ms ease;
  margin-bottom: 24px;
}
.activity-card:hover { box-shadow: var(--shadow-card-hover); }
.activity-card-body { padding: 20px; }
.activity-card-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-text);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  margin-bottom: 10px;
}
.activity-card-info {
  font-size: 14px;
  color: var(--color-text-slate);
  margin-bottom: 6px;
  font-weight: 500;
}
.activity-card-info i { margin-right: 4px; }
.activity-card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
}
.video-card {
  background: var(--color-surface);
  border-radius: var(--radius-card);
  border: 1px solid var(--color-border);
  overflow: hidden;
  cursor: pointer;
  box-shadow: var(--shadow-card);
  transition: box-shadow 200ms ease;
  margin-bottom: 24px;
}
.video-card:hover { box-shadow: var(--shadow-card-hover); }
.video-card-cover {
  width: 100%;
  height: 200px;
  background: var(--color-dark);
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}
.video-play-icon {
  position: absolute;
  font-size: 48px;
  color: rgba(255,255,255,0.85);
  transition: color 200ms;
}
.video-card:hover .video-play-icon { color: var(--color-primary); }
.video-card-body { padding: 16px; }
.video-card-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
