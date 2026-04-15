<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="auth-header">
        <h2 class="auth-title">创建账号</h2>
        <p class="auth-subtitle">加入书法交流平台</p>
      </div>
      <el-form :model="form" :rules="rules" ref="regForm">
        <el-form-item prop="username">
          <el-input v-model="form.username" prefix-icon="el-icon-user" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" prefix-icon="el-icon-lock" placeholder="请输入密码" type="password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="form.confirmPassword" prefix-icon="el-icon-lock" placeholder="请确认密码" type="password"></el-input>
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input v-model="form.nickname" prefix-icon="el-icon-s-custom" placeholder="请输入昵称"></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="form.email" prefix-icon="el-icon-message" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item prop="phone">
          <el-input v-model="form.phone" prefix-icon="el-icon-phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width:100%" @click="register" :loading="loading">注 册</el-button>
        </el-form-item>
        <div class="auth-footer">
          <router-link to="/login">已有账号？去登录</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    const checkPass = (rule, value, callback) => {
      if (value !== this.form.password) {
        callback(new Error('两次密码不一致'))
      } else {
        callback()
      }
    }
    return {
      form: { username: '', password: '', confirmPassword: '', nickname: '', email: '', phone: '' },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '密码至少6位', trigger: 'blur' }],
        confirmPassword: [{ required: true, message: '请确认密码', trigger: 'blur' }, { validator: checkPass, trigger: 'blur' }]
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
  width: 460px;
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
