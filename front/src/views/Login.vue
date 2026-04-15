<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="auth-header">
        <h2 class="auth-title">书法交流平台</h2>
        <p class="auth-subtitle">登录你的账号</p>
      </div>
      <el-form :model="form" :rules="rules" ref="loginForm">
        <el-form-item prop="username">
          <el-input v-model="form.username" prefix-icon="el-icon-user" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" prefix-icon="el-icon-lock" placeholder="请输入密码" type="password" @keyup.enter.native="login"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width:100%;border-radius:50px;padding:12px 0;font-size:16px" @click="login" :loading="loading">登 录</el-button>
        </el-form-item>
      </el-form>
      <div class="auth-footer">
        <router-link to="/register">没有账号？去注册</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: { username: '', password: '' },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    login() {
      this.$refs.loginForm.validate(async valid => {
        if (!valid) return
        this.loading = true
        try {
          const res = await this.$request.post('/api/user/login', this.form)
          localStorage.setItem('user', JSON.stringify(res.data))
          this.$message.success('登录成功')
          this.$router.push('/home')
        } catch (e) {} finally { this.loading = false }
      })
    }
  }
}
</script>

<style scoped>
.auth-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-dark);
}
.auth-card {
  width: 420px;
  padding: 48px 40px;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-card);
}
.auth-header {
  text-align: center;
  margin-bottom: 32px;
}
.auth-title {
  font-size: 24px;
  font-weight: 700;
  color: var(--color-text);
  margin-bottom: 8px;
}
.auth-subtitle {
  font-size: 15px;
  color: var(--color-text-slate);
  font-weight: 500;
}
.auth-footer {
  text-align: center;
  margin-top: 8px;
}
.auth-footer a {
  color: var(--color-primary);
  font-size: 14px;
  font-weight: 600;
  text-decoration: none;
}
.auth-footer a:hover {
  color: var(--color-primary-hover);
}
</style>
