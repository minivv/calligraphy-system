<template>
  <div>
    <el-card>
      <div slot="header"><span style="font-size:18px;font-weight:bold">个人中心</span></div>
      <el-form :model="form" label-width="80px" style="max-width:500px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname"></el-input>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload action="/api/file/upload" :show-file-list="false" :on-success="handleAvatar" :headers="uploadHeaders">
            <el-avatar v-if="form.avatar" :src="form.avatar" :size="80"></el-avatar>
            <el-button v-else size="small" type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveProfile">保存修改</el-button>
        </el-form-item>
      </el-form>

      <el-divider></el-divider>
      <h3 style="margin-bottom:15px">修改密码</h3>
      <el-form :model="pwdForm" label-width="80px" style="max-width:500px">
        <el-form-item label="新密码">
          <el-input v-model="pwdForm.password" type="password"></el-input>
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="pwdForm.confirmPassword" type="password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="warning" @click="changePassword">修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {},
      pwdForm: { password: '', confirmPassword: '' },
      uploadHeaders: {}
    }
  },
  async created() {
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    this.uploadHeaders = { token: user.token }
    const res = await this.$request.get('/api/user/info')
    this.form = res.data
  },
  methods: {
    handleAvatar(res) {
      if (res.code === 200) {
        this.form.avatar = res.data
      }
    },
    async saveProfile() {
      await this.$request.put('/api/user/update', this.form)
      // Update local storage
      const user = JSON.parse(localStorage.getItem('user') || '{}')
      Object.assign(user, { nickname: this.form.nickname, avatar: this.form.avatar })
      localStorage.setItem('user', JSON.stringify(user))
      this.$message.success('保存成功')
    },
    async changePassword() {
      if (!this.pwdForm.password) return this.$message.warning('请输入新密码')
      if (this.pwdForm.password !== this.pwdForm.confirmPassword) return this.$message.warning('两次密码不一致')
      await this.$request.put('/api/user/password', { password: this.pwdForm.password })
      this.pwdForm = { password: '', confirmPassword: '' }
    }
  }
}
</script>
