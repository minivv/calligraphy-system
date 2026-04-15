<template>
  <el-container style="height:100vh">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="admin-aside">
      <div class="aside-logo">
        <span class="logo-icon">书</span>
        <span v-if="!isCollapse" class="logo-text">书法管理系统</span>
      </div>
      <el-menu :default-active="$route.path" router background-color="#222126" text-color="#848E9C" active-text-color="#F0B90B" :collapse="isCollapse" class="admin-menu">
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
      <el-header class="admin-header">
        <el-button type="text" @click="isCollapse=!isCollapse" class="collapse-btn">
          <i :class="isCollapse?'el-icon-s-unfold':'el-icon-s-fold'" style="font-size:22px;color:var(--color-text)"></i>
        </el-button>
        <div class="header-right">
          <span class="admin-name">{{ admin.nickname || admin.username }}</span>
          <el-button type="text" @click="logout" class="logout-btn"><i class="el-icon-switch-button"></i> 退出</el-button>
        </div>
      </el-header>
      <el-main class="admin-main">
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
.admin-aside {
  background: var(--color-dark);
  transition: width 0.3s;
  overflow: hidden;
}
.aside-logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  border-bottom: 1px solid rgba(255,255,255,0.08);
}
.logo-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background: var(--color-primary);
  color: var(--color-ink);
  font-size: 16px;
  font-weight: 700;
  border-radius: var(--radius-sm);
}
.logo-text {
  color: #FFFFFF;
  font-size: 15px;
  font-weight: 600;
  white-space: nowrap;
}
.admin-menu {
  border-right: none !important;
}
.admin-header {
  background: var(--color-surface);
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid var(--color-border);
  height: 64px !important;
  padding: 0 24px;
}
.collapse-btn {
  padding: 4px !important;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}
.admin-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text);
}
.logout-btn {
  color: var(--color-text-slate) !important;
  font-size: 14px;
}
.logout-btn:hover {
  color: var(--color-danger) !important;
}
.admin-main {
  background: var(--color-surface-snow);
  padding: 24px;
}
</style>
