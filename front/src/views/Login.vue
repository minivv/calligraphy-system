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
          <el-button type="primary" style="width:100%" @click="login" :loading="loading">登 录</el-button>
        </el-form-item>
        <div class="auth-footer">
          <router-link to="/register">没有账号？去注册</router-link>
        </div>
      </el-form>
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
          this.$router.push('/')
        } catch (e) {} finally {
          this.loading = false
        }
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
  background: linear-gradient(135deg, #0d1b3e 0%, #1b61c9 50%, #254fad 100%);
}
.auth-card {
  width: 420px;
  padding: 48px 40px;
  background: var(--color-surface);
  border-radius: var(--radius-section);
  box-shadow: var(--shadow-card);
}
.auth-header {
  text-align: center;
  margin-bottom: 32px;
}
.auth-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--color-text);
  letter-spacing: 0.12px;
  margin-bottom: 8px;
}
.auth-subtitle {
  font-size: 15px;
  color: var(--color-text-weak);
  letter-spacing: var(--letter-spacing-body);
}
.auth-footer {
  text-align: center;
  margin-top: 4px;
}
.auth-footer a {
  font-size: 14px;
  color: var(--color-primary);
  letter-spacing: var(--letter-spacing-caption);
}
.auth-footer a:hover {
  color: var(--color-primary-hover);
}
</style>
