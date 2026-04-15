<template>
  <div>
    <div class="page-card">
      <div class="card-header">
        <h2 class="card-title">我的报名</h2>
      </div>
      <el-table :data="tableData" stripe style="width:100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="activityTitle" label="活动名称" min-width="200"></el-table-column>
        <el-table-column prop="remark" label="备注" width="150"></el-table-column>
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="['warning','success','danger','info'][scope.row.status]" effect="plain">{{ ['待审核','已通过','已拒绝','已取消'][scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="报名时间" width="160"></el-table-column>
        <el-table-column label="操作" width="120">
          <template slot-scope="scope">
            <el-button type="text" style="color:var(--color-danger)" @click="cancel(scope.row.id)" v-if="scope.row.status===0">取消报名</el-button>
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
  data() {
    const user = JSON.parse(localStorage.getItem('user') || '{}')
    return { tableData: [], pageNum: 1, pageSize: 10, total: 0, userId: user.id }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/registration/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize, userId: this.userId } })
      this.tableData = res.data.records; this.total = res.data.total
    },
    handlePage(val) { this.pageNum = val; this.loadData() },
    async cancel(id) {
      await this.$request.put(`/api/registration/cancel/${id}`)
      this.$message.success('已取消'); this.loadData()
    }
  }
}
</script>

<style scoped>
.page-card {
  background: var(--color-surface);
  border-radius: var(--radius-card);
  border: 1px solid var(--color-border);
  box-shadow: var(--shadow-soft);
  padding: 32px;
}
.card-header {
  margin-bottom: 24px;
}
.card-title {
  font-size: 24px;
  font-weight: 400;
  color: var(--color-text);
  letter-spacing: 0.12px;
}
.pagination-wrap {
  text-align: center;
  margin-top: 20px;
}
</style>
