<template>
  <el-container style="height:100vh">
    <el-aside :width="isCollapse ? '64px' : '220px'" style="background:#304156;transition:width 0.3s">
      <div class="logo" v-if="!isCollapse">
        <h3 style="color:#fff;text-align:center;line-height:60px;font-size:16px">书法管理系统</h3>
      </div>
      <div class="logo" v-else>
        <h3 style="color:#fff;text-align:center;line-height:60px">书</h3>
      </div>
      <el-menu :default-active="$route.path" router background-color="#304156" text-color="#bfcbd9" active-text-color="#409EFF" :collapse="isCollapse">
        <el-menu-item index="/dashboard"><i class="el-icon-s-home"></i><span slot="title">首页</span></el-menu-item>
        <el-menu-item index="/users" v-if="admin.role==='ADMIN'"><i class="el-icon-user"></i><span slot="title">用户管理</span></el-menu-item>
        <el-menu-item index="/admins" v-if="admin.role==='ADMIN'"><i class="el-icon-s-custom"></i><span slot="title">管理员管理</span></el-menu-item>
        <el-menu-item index="/works"><i class="el-icon-picture"></i><span slot="title">书法作品管理</span></el-menu-item>
        <el-menu-item index="/activities"><i class="el-icon-date"></i><span slot="title">活动管理</span></el-menu-item>
        <el-menu-item index="/registrations"><i class="el-icon-s-claim"></i><span slot="title">报名管理</span></el-menu-item>
        <el-menu-item index="/videos"><i class="el-icon-video-camera"></i><span slot="title">视频管理</span></el-menu-item>
        <el-menu-item index="/votes"><i class="el-icon-s-marketing"></i><span slot="title">投票管理</span></el-menu-item>
        <el-menu-item index="/complaints"><i class="el-icon-warning"></i><span slot="title">投诉管理</span></el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background:#fff;display:flex;align-items:center;justify-content:space-between;box-shadow:0 1px 4px rgba(0,0,0,0.1)">
        <el-button type="text" @click="isCollapse=!isCollapse">
          <i :class="isCollapse?'el-icon-s-unfold':'el-icon-s-fold'" style="font-size:24px"></i>
        </el-button>
        <div>
          <span style="margin-right:15px">{{ admin.nickname || admin.username }}</span>
          <el-button type="text" @click="logout"><i class="el-icon-switch-button"></i> 退出</el-button>
        </div>
      </el-header>
      <el-main style="background:#f0f2f5">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  data() {
    return {
      isCollapse: false,
      admin: JSON.parse(localStorage.getItem('admin') || '{}')
    }
  },
  methods: {
    logout() {
      localStorage.removeItem('admin')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.logo { height: 60px; border-bottom: 1px solid rgba(255,255,255,0.1); }
</style>
