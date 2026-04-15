<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="auth-header">
        <h2 class="auth-title">书法交流平台</h2>
        <p class="auth-subtitle">创建新账号</p>
      </div>
      <el-form :model="form" :rules="rules" ref="regForm">
        <el-form-item prop="username"><el-input v-model="form.username" prefix-icon="el-icon-user" placeholder="用户名"></el-input></el-form-item>
        <el-form-item prop="password"><el-input v-model="form.password" prefix-icon="el-icon-lock" placeholder="密码" type="password"></el-input></el-form-item>
        <el-form-item prop="nickname"><el-input v-model="form.nickname" prefix-icon="el-icon-s-custom" placeholder="昵称"></el-input></el-form-item>
        <el-form-item prop="email"><el-input v-model="form.email" prefix-icon="el-icon-message" placeholder="邮箱"></el-input></el-form-item>
        <el-form-item prop="phone"><el-input v-model="form.phone" prefix-icon="el-icon-phone" placeholder="手机号"></el-input></el-form-item>
        <el-form-item>
          <el-button type="primary" style="width:100%;border-radius:50px;padding:12px 0;font-size:16px" @click="register" :loading="loading">注 册</el-button>
        </el-form-item>
      </el-form>
      <div class="auth-footer">
        <router-link to="/login">已有账号？去登录</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: { username: '', password: '', nickname: '', email: '', phone: '' },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    register() {
      this.$refs.regForm.validate(async valid => {
        if (!valid) return
        this.loading = true
        try {
          await this.$request.post('/api/user/register', this.form)
          this.$message.success('注册成功，请登录')
          this.$router.push('/login')
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
  width: 460px;
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
