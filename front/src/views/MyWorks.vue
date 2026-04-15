<template>
  <div>
    <el-card>
      <div slot="header">
        <span style="font-size:18px;font-weight:bold">我的作品</span>
        <el-button type="primary" size="small" style="float:right" @click="openDialog(null)">发布作品</el-button>
      </div>
      <el-table :data="tableData" border stripe>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="标题" min-width="150"></el-table-column>
        <el-table-column prop="category" label="类别" width="100"></el-table-column>
        <el-table-column label="图片" width="100">
          <template slot-scope="scope">
            <el-image v-if="scope.row.imageUrl" :src="scope.row.imageUrl" style="width:60px;height:60px" fit="cover"></el-image>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="likeCount" label="点赞" width="80"></el-table-column>
        <el-table-column prop="commentCount" label="评论" width="80"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button type="text" @click="openDialog(scope.row)">编辑</el-button>
            <el-popconfirm title="确定删除？" @confirm="deleteRow(scope.row.id)">
              <el-button slot="reference" type="text" style="color:#F56C6C">删除</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:15px" @current-change="handlePage" :current-page="pageNum" :page-size="pageSize" :total="total" layout="total, prev, pager, next"></el-pagination>
    </el-card>

    <el-dialog :title="form.id?'编辑作品':'发布作品'" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title"></el-input></el-form-item>
        <el-form-item label="类别">
          <el-select v-model="form.category" style="width:100%">
            <el-option v-for="c in categories" :key="c" :label="c" :value="c"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="4"></el-input></el-form-item>
        <el-form-item label="作品图片">
          <el-upload action="/api/file/upload" :show-file-list="false" :on-success="handleUpload" :headers="uploadHeaders">
            <el-image v-if="form.imageUrl" :src="form.imageUrl" style="width:200px;height:150px" fit="cover"></el-image>
            <el-button v-else size="small" type="primary">上传图片</el-button>
          </el-upload>
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
  data() {
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    return {
      tableData: [], pageNum: 1, pageSize: 10, total: 0, dialogVisible: false,
      form: {}, categories: ['楷书','行书','草书','隶书','篆书'],
      userId: user.id, uploadHeaders: { token: user.token }
    }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/work/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize, userId: this.userId } })
      this.tableData = res.data.records; this.total = res.data.total
    },
    handlePage(val) { this.pageNum = val; this.loadData() },
    openDialog(row) {
      this.form = row ? { ...row } : { title: '', category: '楷书', description: '', imageUrl: '' }
      this.dialogVisible = true
    },
    handleUpload(res) { if (res.code === 200) this.form.imageUrl = res.data },
    async save() {
      if (this.form.id) { await this.$request.put('/api/work', this.form) }
      else { await this.$request.post('/api/work', this.form) }
      this.$message.success('操作成功'); this.dialogVisible = false; this.loadData()
    },
    async deleteRow(id) {
      await this.$request.delete(`/api/work/${id}`)
      this.$message.success('删除成功'); this.loadData()
    }
  }
}
</script>
