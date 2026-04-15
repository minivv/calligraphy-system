<template>
  <div class="login-container">
    <div class="login-box">
      <h2>用户注册</h2>
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
        <div style="text-align:center">
          <router-link to="/login" style="color:#409EFF">已有账号？去登录</router-link>
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
.login-container {
  height: 100vh; display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #2c3e50 0%, #4ca1af 100%);
}
.login-box {
  width: 450px; padding: 40px; background: #fff; border-radius: 10px; box-shadow: 0 10px 40px rgba(0,0,0,0.2);
}
.login-box h2 { text-align: center; margin-bottom: 30px; color: #333; }
</style>
