<template>
  <div>
    <header class="app-header">
      <div class="header-inner">
        <div class="header-left">
          <h1 class="logo" @click="$router.push('/')">书法交流平台</h1>
          <nav class="nav-links">
            <router-link to="/home">首页</router-link>
            <router-link to="/works">书法作品</router-link>
            <router-link to="/activities">交流活动</router-link>
            <router-link to="/videos">书法视频</router-link>
            <router-link to="/votes">作品投票</router-link>
          </nav>
        </div>
        <div class="header-right">
          <template v-if="user.id">
            <el-dropdown @command="handleCmd" trigger="click">
              <span class="user-trigger">
                <el-avatar :size="32" :src="user.avatar" icon="el-icon-user-solid"></el-avatar>
                <span class="user-name">{{ user.nickname || user.username }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="profile" icon="el-icon-user">个人中心</el-dropdown-item>
                <el-dropdown-item command="my-works" icon="el-icon-picture">我的作品</el-dropdown-item>
                <el-dropdown-item command="my-registrations" icon="el-icon-s-claim">我的报名</el-dropdown-item>
                <el-dropdown-item command="my-complaints" icon="el-icon-warning">我的投诉</el-dropdown-item>
                <el-dropdown-item command="logout" icon="el-icon-switch-button" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
          <template v-else>
            <router-link to="/login" class="header-link">登录</router-link>
            <router-link to="/register" class="btn-get-started">注册</router-link>
          </template>
        </div>
      </div>
    </header>
    <main class="app-main">
      <router-view />
    </main>
    <footer class="app-footer">
      <div class="footer-inner">
        <span>书法交流活动管理系统 &copy; 2024</span>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  data() {
    return { user: JSON.parse(localStorage.getItem('user') || '{}') }
  },
  watch: {
    '$route'() { this.user = JSON.parse(localStorage.getItem('user') || '{}') }
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
.app-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
  height: 64px;
}
.header-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  height: 100%;
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
  font-size: 18px;
  font-weight: 700;
  color: var(--color-primary);
  cursor: pointer;
  letter-spacing: 0.5px;
}
.nav-links {
  display: flex;
  gap: 4px;
}
.nav-links a {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-secondary);
  text-decoration: none;
  padding: 8px 14px;
  border-radius: 10px;
  transition: color 200ms ease, background 200ms ease;
}
.nav-links a:hover {
  color: var(--color-text-hover);
}
.nav-links a.router-link-active {
  color: var(--color-primary);
  background: rgba(240, 185, 11, 0.08);
}
.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}
.header-link {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-secondary);
  text-decoration: none;
  padding: 8px 16px;
}
.header-link:hover { color: var(--color-text-hover); }
.btn-get-started {
  display: inline-block;
  background: var(--color-primary-gold);
  color: #FFFFFF;
  font-size: 14px;
  font-weight: 600;
  padding: 8px 24px;
  border-radius: var(--radius-pill);
  text-decoration: none;
  box-shadow: var(--shadow-pill);
  transition: background 200ms ease;
}
.btn-get-started:hover {
  background: var(--color-focus);
  color: #FFFFFF;
}
.user-trigger {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: var(--color-text);
}
.user-name {
  font-size: 14px;
  font-weight: 600;
}
.app-main {
  max-width: 1200px;
  margin: 0 auto;
  padding: 32px 24px;
  min-height: calc(100vh - 64px - 60px);
}
.app-footer {
  background: var(--color-dark);
  color: var(--color-text-slate);
  padding: 20px 0;
}
.footer-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  text-align: center;
  font-size: 13px;
  font-weight: 500;
}
</style>
