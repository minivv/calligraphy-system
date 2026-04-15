<template>
  <div>
    <div class="page-card">
      <div class="card-header"><h3 class="card-title">我的报名</h3></div>
      <el-table :data="tableData" stripe>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="activityTitle" label="活动名称" min-width="200"></el-table-column>
        <el-table-column prop="remark" label="备注" width="150"></el-table-column>
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <el-tag size="mini" :type="['warning','success','danger','info'][scope.row.status]">{{ ['待审核','已通过','已拒绝','已取消'][scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="报名时间" width="160"></el-table-column>
        <el-table-column label="操作" width="120">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status===0" type="text" style="color:var(--color-danger)" @click="cancelReg(scope.row.id)">取消报名</el-button>
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
  data() { return { tableData: [], pageNum: 1, pageSize: 10, total: 0 } },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/registration/my', { params: { pageNum: this.pageNum, pageSize: this.pageSize } })
      this.tableData = res.data.records; this.total = res.data.total
    },
    handlePage(val) { this.pageNum = val; this.loadData() },
    async cancelReg(id) { await this.$request.put('/api/registration/cancel/' + id); this.$message.success('已取消'); this.loadData() }
  }
}
</script>

<style scoped>
.page-card { background: var(--color-surface); border-radius: var(--radius-card); border: 1px solid var(--color-border); box-shadow: var(--shadow-card); padding: 32px; }
.card-header { margin-bottom: 20px; }
.card-title { font-size: 20px; font-weight: 600; color: var(--color-text); }
.pagination-wrap { text-align: center; margin-top: 16px; }
</style>
