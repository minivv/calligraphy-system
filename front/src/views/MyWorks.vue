<template>
  <div>
    <div class="page-card">
      <div class="card-header">
        <h3 class="card-title">我的作品</h3>
        <el-button type="primary" size="small" @click="openDialog(null)">发布作品</el-button>
      </div>
      <el-table :data="tableData" stripe>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="标题" min-width="150"></el-table-column>
        <el-table-column prop="category" label="类别" width="100"></el-table-column>
        <el-table-column label="图片" width="100">
          <template slot-scope="scope">
            <el-image v-if="scope.row.imageUrl" :src="scope.row.imageUrl" style="width:60px;height:60px;border-radius:8px" fit="cover"></el-image>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="likeCount" label="点赞" width="80"></el-table-column>
        <el-table-column prop="commentCount" label="评论" width="80"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button type="text" @click="openDialog(scope.row)">编辑</el-button>
            <el-popconfirm title="确定删除？" @confirm="deleteWork(scope.row.id)">
              <el-button slot="reference" type="text" style="color:var(--color-danger)">删除</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrap">
        <el-pagination @current-change="handlePage" :current-page="pageNum" :page-size="pageSize" :total="total" layout="total, prev, pager, next"></el-pagination>
      </div>
    </div>
    <el-dialog :title="form.id?'编辑作品':'发布作品'" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title"></el-input></el-form-item>
        <el-form-item label="类别">
          <el-select v-model="form.category" placeholder="选择类别">
            <el-option v-for="c in categories" :key="c" :label="c" :value="c"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="4"></el-input></el-form-item>
        <el-form-item label="图片">
          <el-upload :action="uploadUrl" :headers="uploadHeaders" :show-file-list="false" :on-success="onUploadSuccess">
            <el-image v-if="form.imageUrl" :src="form.imageUrl" style="width:200px;height:150px;border-radius:12px" fit="cover"></el-image>
            <el-button v-else size="small" type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="saveWork">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    return {
      tableData: [], pageNum: 1, pageSize: 10, total: 0,
      dialogVisible: false,
      form: { title: '', category: '', description: '', imageUrl: '' },
      categories: ['楷书','行书','草书','隶书','篆书'],
      uploadUrl: '/api/file/upload',
      uploadHeaders: { token: user.token }
    }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/work/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize } })
      this.tableData = res.data.records; this.total = res.data.total
    },
    handlePage(val) { this.pageNum = val; this.loadData() },
    openDialog(row) { this.form = row ? { ...row } : { title: '', category: '', description: '', imageUrl: '' }; this.dialogVisible = true },
    onUploadSuccess(res) { if (res.code === 200) this.form.imageUrl = res.data },
    async saveWork() {
      if (this.form.id) { await this.$request.put('/api/work', this.form) } else { await this.$request.post('/api/work', this.form) }
      this.$message.success('保存成功'); this.dialogVisible = false; this.loadData()
    },
    async deleteWork(id) { await this.$request.delete('/api/work/' + id); this.$message.success('删除成功'); this.loadData() }
  }
}
</script>

<style scoped>
.page-card { background: var(--color-surface); border-radius: var(--radius-card); border: 1px solid var(--color-border); box-shadow: var(--shadow-card); padding: 32px; }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.card-title { font-size: 20px; font-weight: 600; color: var(--color-text); }
.pagination-wrap { text-align: center; margin-top: 16px; }
</style>
