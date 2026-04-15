<template>
  <div class="login-container">
    <div class="login-box">
      <h2>书法交流活动平台</h2>
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
        <div style="text-align:center">
          <router-link to="/register" style="color:#409EFF">没有账号？去注册</router-link>
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
.login-container {
  height: 100vh; display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #2c3e50 0%, #4ca1af 100%);
}
.login-box {
  width: 400px; padding: 40px; background: #fff; border-radius: 10px; box-shadow: 0 10px 40px rgba(0,0,0,0.2);
}
.login-box h2 { text-align: center; margin-bottom: 30px; color: #333; }
</style>
