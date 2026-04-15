<template>
  <div>
    <el-card>
      <div slot="header">
        <span style="font-size:18px;font-weight:bold">我的投诉</span>
        <el-button type="primary" size="small" style="float:right" @click="openDialog">新建投诉</el-button>
      </div>
      <el-table :data="tableData" border stripe>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="标题" min-width="120"></el-table-column>
        <el-table-column prop="content" label="内容" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="['warning','success','danger'][scope.row.status]">{{ ['待处理','已处理','已驳回'][scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reply" label="处理回复" min-width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createTime" label="投诉时间" width="160"></el-table-column>
      </el-table>
      <el-pagination style="margin-top:15px" @current-change="handlePage" :current-page="pageNum" :page-size="pageSize" :total="total" layout="total, prev, pager, next"></el-pagination>
    </el-card>

    <el-dialog title="新建投诉" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title"></el-input></el-form-item>
        <el-form-item label="投诉内容"><el-input v-model="form.content" type="textarea" :rows="4"></el-input></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="submit">提交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    return { tableData: [], pageNum: 1, pageSize: 10, total: 0, userId: user.id, dialogVisible: false, form: {} }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/complaint/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize, userId: this.userId } })
      this.tableData = res.data.records; this.total = res.data.total
    },
    handlePage(val) { this.pageNum = val; this.loadData() },
    openDialog() { this.form = { title: '', content: '' }; this.dialogVisible = true },
    async submit() {
      if (!this.form.title || !this.form.content) return this.$message.warning('请填写完整')
      await this.$request.post('/api/complaint', this.form)
      this.$message.success('投诉已提交'); this.dialogVisible = false; this.loadData()
    }
  }
}
</script>
