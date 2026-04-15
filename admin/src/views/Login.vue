<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <span class="login-logo">书</span>
        <h2>书法管理系统</h2>
        <p class="login-subtitle">管理员登录</p>
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
          const res = await this.$request.post('/api/admin/login', this.form)
          localStorage.setItem('admin', JSON.stringify(res.data))
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
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-dark);
}
.login-box {
  width: 420px;
  padding: 48px 40px;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-card);
}
.login-header {
  text-align: center;
  margin-bottom: 32px;
}
.login-logo {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  background: var(--color-primary);
  color: var(--color-ink);
  font-size: 22px;
  font-weight: 700;
  border-radius: var(--radius-input);
  margin-bottom: 16px;
}
.login-header h2 {
  font-size: 24px;
  font-weight: 700;
  color: var(--color-text);
  margin-bottom: 6px;
}
.login-subtitle {
  font-size: 14px;
  color: var(--color-text-slate);
  font-weight: 500;
}
</style>
