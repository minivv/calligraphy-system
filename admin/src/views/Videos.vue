<template>
  <el-card>
    <div slot="header">
      <span>视频管理</span>
      <el-button type="primary" size="small" style="float:right" @click="openDialog(null)">新增视频</el-button>
    </div>
    <el-form :inline="true" :model="query">
      <el-form-item><el-input v-model="query.title" placeholder="视频标题" clearable></el-input></el-form-item>
      <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="title" label="标题" min-width="150"></el-table-column>
      <el-table-column prop="username" label="上传者" width="120"></el-table-column>
      <el-table-column label="封面" width="100">
        <template slot-scope="scope">
          <el-image v-if="scope.row.coverImage" :src="scope.row.coverImage" style="width:60px;height:60px" fit="cover"></el-image>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="likeCount" label="点赞" width="80"></el-table-column>
      <el-table-column prop="commentCount" label="评论" width="80"></el-table-column>
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status===1?'success':'info'">{{ scope.row.status===1?'正常':'下架' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button type="text" @click="openDialog(scope.row)">编辑</el-button>
          <el-button type="text" @click="toggleStatus(scope.row)">{{ scope.row.status===1?'下架':'上架' }}</el-button>
          <el-popconfirm title="确定删除？" @confirm="deleteRow(scope.row.id)">
            <el-button slot="reference" type="text" style="color:#F56C6C">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:15px" @current-change="handlePage" :current-page="pageNum" :page-size="pageSize" :total="total" layout="total, prev, pager, next"></el-pagination>

    <el-dialog :title="form.id?'编辑视频':'新增视频'" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title"></el-input></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="3"></el-input></el-form-item>
        <el-form-item label="视频链接"><el-input v-model="form.videoUrl" placeholder="输入视频URL或上传视频文件"></el-input></el-form-item>
        <el-form-item label="封面图">
          <el-upload action="/api/file/upload" :show-file-list="false" :on-success="handleCoverUpload" :headers="uploadHeaders">
            <el-image v-if="form.coverImage" :src="form.coverImage" style="width:200px;height:120px" fit="cover"></el-image>
            <el-button v-else size="small" type="primary">上传封面</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </span>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  data() {
    const admin = JSON.parse(localStorage.getItem('admin') || '{}')
    return {
      tableData: [], query: { title: '' }, pageNum: 1, pageSize: 10, total: 0,
      dialogVisible: false, form: {},
      uploadHeaders: { token: admin.token }
    }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/video/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize, ...this.query } })
      this.tableData = res.data.records; this.total = res.data.total
    },
    handlePage(val) { this.pageNum = val; this.loadData() },
    openDialog(row) {
      this.form = row ? { ...row } : { title: '', description: '', videoUrl: '', coverImage: '' }
      this.dialogVisible = true
    },
    handleCoverUpload(res) { if (res.code === 200) this.form.coverImage = res.data },
    async save() {
      if (!this.form.title) { this.$message.warning('请输入标题'); return }
      if (this.form.id) { await this.$request.put('/api/video', this.form) }
      else { await this.$request.post('/api/video', this.form) }
      this.$message.success('操作成功'); this.dialogVisible = false; this.loadData()
    },
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
