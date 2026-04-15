<template>
  <el-card>
    <div slot="header"><span>用户管理</span></div>
    <el-form :inline="true" :model="query">
      <el-form-item><el-input v-model="query.username" placeholder="用户名" clearable></el-input></el-form-item>
      <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="username" label="用户名"></el-table-column>
      <el-table-column prop="nickname" label="昵称"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="phone" label="手机号"></el-table-column>
      <el-table-column prop="gender" label="性别" width="80"></el-table-column>
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <el-switch :value="scope.row.status===1" @change="toggleStatus(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="160"></el-table-column>
      <el-table-column label="操作" width="120">
        <template slot-scope="scope">
          <el-popconfirm title="确定删除？" @confirm="deleteRow(scope.row.id)">
            <el-button slot="reference" type="text" style="color:#F56C6C">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:15px" @current-change="handlePage" :current-page="pageNum" :page-size="pageSize" :total="total" layout="total, prev, pager, next"></el-pagination>
  </el-card>
</template>

<script>
export default {
  data() { return { tableData: [], query: { username: '' }, pageNum: 1, pageSize: 10, total: 0 } },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/user/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize, ...this.query } })
      this.tableData = res.data.records; this.total = res.data.total
    },
    handlePage(val) { this.pageNum = val; this.loadData() },
    async toggleStatus(row) {
      await this.$request.put(`/api/user/status/${row.id}`, null, { params: { status: row.status === 1 ? 0 : 1 } })
      this.loadData()
    },
    async deleteRow(id) {
      await this.$request.delete(`/api/user/${id}`)
      this.$message.success('删除成功')
      this.loadData()
    }
  }
}
</script>
