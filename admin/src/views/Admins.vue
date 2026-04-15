<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">管理员管理</h2>
      <el-button type="primary" @click="openDialog(null)">新增管理员</el-button>
    </div>
    <div class="page-card">
      <el-table :data="tableData" stripe style="width:100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column prop="nickname" label="昵称"></el-table-column>
        <el-table-column label="角色" width="140">
          <template slot-scope="scope">
            <el-tag :type="scope.row.role==='ADMIN'?'danger':'warning'" size="small">{{ scope.row.role==='ADMIN'?'管理员':'活动管理员' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <el-switch :value="scope.row.status===1" @change="toggleStatus(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button type="text" @click="openDialog(scope.row)">编辑</el-button>
            <el-popconfirm title="确定删除？" @confirm="deleteRow(scope.row.id)">
              <el-button slot="reference" type="text" class="btn-danger-text">删除</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrap">
        <el-pagination @current-change="handlePage" :current-page="pageNum" :page-size="pageSize" :total="total" layout="total, prev, pager, next"></el-pagination>
      </div>
    </div>

    <el-dialog :title="form.id?'编辑管理员':'新增管理员'" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名"><el-input v-model="form.username" :disabled="!!form.id"></el-input></el-form-item>
        <el-form-item label="密码"><el-input v-model="form.password" :placeholder="form.id?'不修改请留空':'请输入密码'" type="password"></el-input></el-form-item>
        <el-form-item label="昵称"><el-input v-model="form.nickname"></el-input></el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role" style="width:100%">
            <el-option label="管理员" value="ADMIN"></el-option>
            <el-option label="活动管理员" value="ACTIVITY_ADMIN"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() { return { tableData: [], pageNum: 1, pageSize: 10, total: 0, dialogVisible: false, form: {} } },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/admin/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize } })
      this.tableData = res.data.records; this.total = res.data.total
    },
    handlePage(val) { this.pageNum = val; this.loadData() },
    openDialog(row) {
      this.form = row ? { ...row, password: '' } : { username: '', password: '', nickname: '', role: 'ACTIVITY_ADMIN' }
      this.dialogVisible = true
    },
    async save() {
      if (this.form.id) {
        await this.$request.put('/api/admin', this.form)
      } else {
        await this.$request.post('/api/admin', this.form)
      }
      this.$message.success('操作成功'); this.dialogVisible = false; this.loadData()
    },
    async toggleStatus(row) {
      await this.$request.put(`/api/admin/status/${row.id}`, null, { params: { status: row.status === 1 ? 0 : 1 } })
      this.loadData()
    },
    async deleteRow(id) {
      await this.$request.delete(`/api/admin/${id}`)
      this.$message.success('删除成功'); this.loadData()
    }
  }
}
</script>

<style scoped>
.page-container { }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 20px; }
.page-title { font-size: 20px; font-weight: 600; color: var(--color-text); }
.page-card { background: var(--color-surface); border-radius: var(--radius-card); border: 1px solid var(--color-border); box-shadow: var(--shadow-card); padding: 24px; }
.pagination-wrap { margin-top: 20px; display: flex; justify-content: flex-end; }
.btn-danger-text { color: var(--color-danger) !important; }
</style>
