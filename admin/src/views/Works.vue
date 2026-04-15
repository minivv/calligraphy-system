<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">书法作品管理</h2>
    </div>
    <div class="page-card">
      <el-form :inline="true" :model="query" class="search-form">
        <el-form-item><el-input v-model="query.title" placeholder="作品标题" clearable></el-input></el-form-item>
        <el-form-item>
          <el-select v-model="query.category" placeholder="类别" clearable>
            <el-option v-for="c in categories" :key="c" :label="c" :value="c"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
      </el-form>
      <el-table :data="tableData" stripe style="width:100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="category" label="类别" width="100"></el-table-column>
        <el-table-column prop="username" label="作者" width="120"></el-table-column>
        <el-table-column label="图片" width="100">
          <template slot-scope="scope">
            <el-image v-if="scope.row.imageUrl" :src="scope.row.imageUrl" style="width:48px;height:48px;border-radius:6px" fit="cover" :preview-src-list="[scope.row.imageUrl]"></el-image>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="likeCount" label="点赞" width="80"></el-table-column>
        <el-table-column prop="commentCount" label="评论" width="80"></el-table-column>
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status===1?'success':'info'" size="small">{{ scope.row.status===1?'正常':'下架' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button type="text" @click="toggleStatus(scope.row)">{{ scope.row.status===1?'下架':'上架' }}</el-button>
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
  </div>
</template>

<script>
export default {
  data() { return { tableData: [], query: { title: '', category: '' }, categories: ['楷书','行书','草书','隶书','篆书'], pageNum: 1, pageSize: 10, total: 0 } },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/work/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize, ...this.query } })
      this.tableData = res.data.records; this.total = res.data.total
    },
    handlePage(val) { this.pageNum = val; this.loadData() },
    async toggleStatus(row) {
      await this.$request.put(`/api/work/status/${row.id}`, null, { params: { status: row.status === 1 ? 0 : 1 } })
      this.$message.success('操作成功'); this.loadData()
    },
    async deleteRow(id) {
      await this.$request.delete(`/api/work/${id}`)
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
.search-form { margin-bottom: 16px; }
.pagination-wrap { margin-top: 20px; display: flex; justify-content: flex-end; }
.text-muted { color: var(--color-text-slate); }
.btn-danger-text { color: var(--color-danger) !important; }
</style>
