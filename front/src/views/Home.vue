<template>
  <div>
    <!-- Banner -->
    <div class="banner">
      <h1>欢迎来到书法交流活动平台</h1>
      <p>传承经典，交流书法艺术</p>
    </div>

    <!-- Latest Works -->
    <div class="section">
      <div class="section-header">
        <h2>最新书法作品</h2>
        <el-button type="text" @click="$router.push('/works')">查看更多 ></el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in homeData.works" :key="item.id">
          <el-card shadow="hover" class="item-card" @click.native="$router.push('/work/'+item.id)">
            <el-image v-if="item.imageUrl" :src="item.imageUrl" style="width:100%;height:180px" fit="cover"></el-image>
            <div v-else style="width:100%;height:180px;background:#f0f0f0;display:flex;align-items:center;justify-content:center;color:#999">
              <i class="el-icon-picture" style="font-size:40px"></i>
            </div>
            <div style="padding:10px">
              <h4 class="ellipsis">{{ item.title }}</h4>
              <div style="display:flex;justify-content:space-between;color:#999;font-size:12px;margin-top:5px">
                <span>{{ item.nickname || item.username }}</span>
                <span><i class="el-icon-thumb"></i> {{ item.likeCount }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- Latest Activities -->
    <div class="section">
      <div class="section-header">
        <h2>最新交流活动</h2>
        <el-button type="text" @click="$router.push('/activities')">查看更多 ></el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="8" v-for="item in homeData.activities" :key="item.id">
          <el-card shadow="hover" class="item-card" @click.native="$router.push('/activity/'+item.id)">
            <div style="padding:15px">
              <h4 class="ellipsis">{{ item.title }}</h4>
              <p style="color:#999;font-size:13px;margin:8px 0"><i class="el-icon-location"></i> {{ item.location }}</p>
              <p style="color:#999;font-size:13px"><i class="el-icon-time"></i> {{ item.startTime }}</p>
              <div style="margin-top:8px">
                <el-tag size="mini" :type="['info','success','danger'][item.status]">{{ ['未开始','进行中','已结束'][item.status] }}</el-tag>
                <span style="float:right;color:#999;font-size:12px">{{ item.currentParticipants }}/{{ item.maxParticipants }}人</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- Latest Videos -->
    <div class="section">
      <div class="section-header">
        <h2>最新书法视频</h2>
        <el-button type="text" @click="$router.push('/videos')">查看更多 ></el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="8" v-for="item in homeData.videos" :key="item.id">
          <el-card shadow="hover" class="item-card" @click.native="$router.push('/video/'+item.id)">
            <div style="width:100%;height:160px;background:#000;display:flex;align-items:center;justify-content:center">
              <i class="el-icon-video-play" style="font-size:50px;color:#fff"></i>
            </div>
            <div style="padding:10px">
              <h4 class="ellipsis">{{ item.title }}</h4>
              <div style="display:flex;justify-content:space-between;color:#999;font-size:12px;margin-top:5px">
                <span>{{ item.nickname || item.username }}</span>
                <span><i class="el-icon-thumb"></i> {{ item.likeCount }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
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
.banner {
  background: linear-gradient(135deg, #2c3e50, #4ca1af);
  color: #fff; text-align: center; padding: 60px 20px; border-radius: 8px; margin-bottom: 30px;
}
.banner h1 { font-size: 32px; margin-bottom: 10px; }
.banner p { font-size: 16px; opacity: 0.8; }
.section { margin-bottom: 30px; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.section-header h2 { font-size: 20px; color: #333; }
.item-card { cursor: pointer; margin-bottom: 15px; }
.item-card:hover { transform: translateY(-3px); transition: 0.3s; }
.ellipsis { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
</style>
