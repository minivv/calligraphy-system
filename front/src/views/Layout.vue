<template>
  <div>
    <el-header style="background:#2c3e50;display:flex;align-items:center;justify-content:space-between;padding:0 30px;height:60px;line-height:60px">
      <div style="display:flex;align-items:center">
        <h2 style="color:#fff;font-size:20px;margin-right:40px;cursor:pointer" @click="$router.push('/')">书法交流活动平台</h2>
        <el-menu :default-active="$route.path" mode="horizontal" background-color="#2c3e50" text-color="#bbb" active-text-color="#fff" router style="border:none">
          <el-menu-item index="/home">首页</el-menu-item>
          <el-menu-item index="/works">书法作品</el-menu-item>
          <el-menu-item index="/activities">交流活动</el-menu-item>
          <el-menu-item index="/videos">书法视频</el-menu-item>
          <el-menu-item index="/votes">作品投票</el-menu-item>
        </el-menu>
      </div>
      <div v-if="user.id">
        <el-dropdown @command="handleCmd">
          <span style="color:#fff;cursor:pointer">
            <i class="el-icon-user-solid"></i> {{ user.nickname || user.username }}
            <i class="el-icon-arrow-down"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="profile">个人中心</el-dropdown-item>
            <el-dropdown-item command="my-works">我的作品</el-dropdown-item>
            <el-dropdown-item command="my-registrations">我的报名</el-dropdown-item>
            <el-dropdown-item command="my-complaints">我的投诉</el-dropdown-item>
            <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <div v-else>
        <el-button type="text" style="color:#fff" @click="$router.push('/login')">登录</el-button>
        <el-button type="text" style="color:#fff" @click="$router.push('/register')">注册</el-button>
      </div>
    </el-header>
    <div style="max-width:1200px;margin:20px auto;padding:0 20px;min-height:calc(100vh - 140px)">
      <router-view />
    </div>
    <div style="background:#2c3e50;color:#999;text-align:center;padding:20px;font-size:13px">
      书法交流活动管理系统 &copy; 2024
    </div>
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
