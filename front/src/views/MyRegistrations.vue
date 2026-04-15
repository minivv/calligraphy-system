<template>
  <div>
    <el-card>
      <div slot="header"><span style="font-size:18px;font-weight:bold">我的报名</span></div>
      <el-table :data="tableData" border stripe>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="activityTitle" label="活动名称" min-width="200"></el-table-column>
        <el-table-column prop="remark" label="备注" width="150"></el-table-column>
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="['warning','success','danger','info'][scope.row.status]">{{ ['待审核','已通过','已拒绝','已取消'][scope.row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="报名时间" width="160"></el-table-column>
        <el-table-column label="操作" width="120">
          <template slot-scope="scope">
            <el-button type="text" style="color:#F56C6C" @click="cancel(scope.row.id)" v-if="scope.row.status===0">取消报名</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:15px" @current-change="handlePage" :current-page="pageNum" :page-size="pageSize" :total="total" layout="total, prev, pager, next"></el-pagination>
    </el-card>
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
