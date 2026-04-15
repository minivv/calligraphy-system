<template>
  <div>
    <div class="page-card">
      <div class="card-header"><h3 class="card-title">个人信息</h3></div>
      <el-form :model="form" label-width="80px" style="max-width:500px">
        <el-form-item label="用户名"><el-input v-model="form.username" disabled></el-input></el-form-item>
        <el-form-item label="昵称"><el-input v-model="form.nickname"></el-input></el-form-item>
        <el-form-item label="头像">
          <el-upload class="avatar-uploader" :action="uploadUrl" :headers="uploadHeaders" :show-file-list="false" :on-success="onAvatarSuccess">
            <el-avatar :size="64" :src="form.avatar" icon="el-icon-user-solid"></el-avatar>
          </el-upload>
        </el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email"></el-input></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone"></el-input></el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="saveProfile">保存</el-button></el-form-item>
      </el-form>
      <div class="section-divider"></div>
      <h4 class="sub-title">修改密码</h4>
      <el-form :model="pwdForm" label-width="80px" style="max-width:500px">
        <el-form-item label="新密码"><el-input v-model="pwdForm.password" type="password"></el-input></el-form-item>
        <el-form-item label="确认密码"><el-input v-model="pwdForm.confirmPassword" type="password"></el-input></el-form-item>
        <el-form-item><el-button type="warning" @click="changePassword">更新密码</el-button></el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    return {
      form: { ...user },
      pwdForm: { password: '', confirmPassword: '' },
      uploadUrl: '/api/file/upload',
      uploadHeaders: { token: user.token }
    }
  },
  methods: {
    onAvatarSuccess(res) { if (res.code === 200) this.form.avatar = res.data },
    async saveProfile() {
      await this.$request.put('/api/user', this.form)
      const user = JSON.parse(localStorage.getItem('user') || '{}')
      user.nickname = this.form.nickname; user.avatar = this.form.avatar
      localStorage.setItem('user', JSON.stringify(user))
      this.$message.success('保存成功')
    },
    async changePassword() {
      if (this.pwdForm.password !== this.pwdForm.confirmPassword) return this.$message.error('两次密码不一致')
      if (!this.pwdForm.password) return this.$message.error('请输入密码')
      await this.$request.put('/api/user/password', { password: this.pwdForm.password })
      this.$message.success('密码修改成功')
      this.pwdForm = { password: '', confirmPassword: '' }
    }
  }
}
</script>

<style scoped>
.page-card { background: var(--color-surface); border-radius: var(--radius-card); border: 1px solid var(--color-border); box-shadow: var(--shadow-card); padding: 32px; }
.card-header { margin-bottom: 24px; }
.card-title { font-size: 20px; font-weight: 600; color: var(--color-text); }
.section-divider { height: 1px; background: var(--color-border); margin: 32px 0; }
.sub-title { font-size: 18px; font-weight: 600; color: var(--color-text); margin-bottom: 20px; }
.avatar-uploader { cursor: pointer; }
</style>
