<template>
  <el-card>
    <div slot="header"><span>视频管理</span></div>
    <el-form :inline="true" :model="query">
      <el-form-item><el-input v-model="query.title" placeholder="视频标题" clearable></el-input></el-form-item>
      <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="title" label="标题" min-width="150"></el-table-column>
      <el-table-column prop="username" label="上传者" width="120"></el-table-column>
      <el-table-column prop="likeCount" label="点赞" width="80"></el-table-column>
      <el-table-column prop="commentCount" label="评论" width="80"></el-table-column>
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status===1?'success':'info'">{{ scope.row.status===1?'正常':'下架' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button type="text" @click="toggleStatus(scope.row)">{{ scope.row.status===1?'下架':'上架' }}</el-button>
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
  data() { return { tableData: [], query: { title: '' }, pageNum: 1, pageSize: 10, total: 0 } },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/video/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize, ...this.query } })
      this.tableData = res.data.records; this.total = res.data.total
    },
    handlePage(val) { this.pageNum = val; this.loadData() },
    async toggleStatus(row) {
      await this.$request.put(`/api/video/status/${row.id}`, null, { params: { status: row.status === 1 ? 0 : 1 } })
      this.$message.success('操作成功'); this.loadData()
    },
    async deleteRow(id) {
      await this.$request.delete(`/api/video/${id}`)
      this.$message.success('删除成功'); this.loadData()
    }
  }
}
</script>
