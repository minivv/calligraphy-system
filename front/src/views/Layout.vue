<template>
  <div class="app-layout">
    <header class="app-header">
      <div class="header-inner">
        <div class="header-left">
          <h1 class="logo" @click="$router.push('/')">书法交流平台</h1>
          <nav class="nav-links">
            <router-link to="/home" class="nav-link" :class="{active: $route.path==='/home' || $route.path==='/'}">首页</router-link>
            <router-link to="/works" class="nav-link" :class="{active: $route.path.startsWith('/work')}">书法作品</router-link>
            <router-link to="/activities" class="nav-link" :class="{active: $route.path.startsWith('/activit')}">交流活动</router-link>
            <router-link to="/videos" class="nav-link" :class="{active: $route.path.startsWith('/video')}">书法视频</router-link>
            <router-link to="/votes" class="nav-link" :class="{active: $route.path.startsWith('/vote')}">作品投票</router-link>
          </nav>
        </div>
        <div class="header-right" v-if="user.id">
          <el-dropdown @command="handleCmd" trigger="click">
            <span class="user-trigger">
              <el-avatar v-if="user.avatar" :src="user.avatar" :size="32"></el-avatar>
              <el-avatar v-else :size="32" style="background:var(--color-primary)">{{ (user.nickname || user.username || '').charAt(0) }}</el-avatar>
              <span class="user-name">{{ user.nickname || user.username }}</span>
              <i class="el-icon-arrow-down" style="font-size:12px;margin-left:4px"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="profile"><i class="el-icon-user"></i> 个人中心</el-dropdown-item>
              <el-dropdown-item command="my-works"><i class="el-icon-picture"></i> 我的作品</el-dropdown-item>
              <el-dropdown-item command="my-registrations"><i class="el-icon-tickets"></i> 我的报名</el-dropdown-item>
              <el-dropdown-item command="my-complaints"><i class="el-icon-warning"></i> 我的投诉</el-dropdown-item>
              <el-dropdown-item command="logout" divided><i class="el-icon-switch-button"></i> 退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
        <div class="header-right" v-else>
          <el-button size="small" @click="$router.push('/login')">登录</el-button>
          <el-button size="small" type="primary" @click="$router.push('/register')">注册</el-button>
        </div>
      </div>
    </header>
    <main class="app-main">
      <router-view />
    </main>
    <footer class="app-footer">
      <p>书法交流活动管理系统 &copy; 2024</p>
    </footer>
  </div>
</template>

<script>
export default {
  data() {
    return {
      user: JSON.parse(localStorage.getItem('user') || '{}')
    }
  },
  watch: {
    '$route'() {
      this.user = JSON.parse(localStorage.getItem('user') || '{}')
    }
  },
  methods: {
    handleCmd(cmd) {
      if (cmd === 'logout') {
        localStorage.removeItem('user')
        this.user = {}
        this.$router.push('/')
      } else {
        this.$router.push('/' + cmd)
      }
    }
  }
}
</script>

<style scoped>
.app-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
.app-header {
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: var(--shadow-soft);
}
.header-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.header-left {
  display: flex;
  align-items: center;
  gap: 32px;
}
.logo {
  font-size: 20px;
  font-weight: 600;
  color: var(--color-text);
  cursor: pointer;
  letter-spacing: 0.12px;
  white-space: nowrap;
}
.nav-links {
  display: flex;
  align-items: center;
  gap: 4px;
}
.nav-link {
  padding: 6px 14px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  color: var(--color-text-weak);
  text-decoration: none;
  transition: all 0.2s;
  letter-spacing: var(--letter-spacing-button);
}
.nav-link:hover {
  background: var(--color-surface-hover);
  color: var(--color-text);
}
.nav-link.active {
  background: var(--color-primary-light);
  color: var(--color-primary);
}
.header-right {
  display: flex;
  align-items: center;
  gap: 8px;
}
.user-trigger {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 8px;
  transition: background 0.2s;
}
.user-trigger:hover {
  background: var(--color-surface-hover);
}
.user-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--color-text);
}
.app-main {
  flex: 1;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 32px 24px;
}
.app-footer {
  background: var(--color-surface);
  border-top: 1px solid var(--color-border);
  text-align: center;
  padding: 20px;
}
.app-footer p {
  font-size: 13px;
  color: var(--color-text-muted);
  letter-spacing: var(--letter-spacing-caption);
}
</style>
