<template>
  <el-card>
    <div slot="header"><span>报名管理</span></div>
    <el-form :inline="true" :model="query">
      <el-form-item>
        <el-select v-model="query.status" placeholder="状态" clearable>
          <el-option label="待审核" :value="0"></el-option>
          <el-option label="已通过" :value="1"></el-option>
          <el-option label="已拒绝" :value="2"></el-option>
          <el-option label="已取消" :value="3"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="activityTitle" label="活动名称" min-width="150"></el-table-column>
      <el-table-column prop="username" label="报名用户" width="120"></el-table-column>
      <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
      <el-table-column prop="remark" label="备注" width="150"></el-table-column>
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="['warning','success','danger','info'][scope.row.status]">{{ ['待审核','已通过','已拒绝','已取消'][scope.row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="报名时间" width="160"></el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <template v-if="scope.row.status===0">
            <el-button type="text" style="color:#67C23A" @click="audit(scope.row.id, 1)">通过</el-button>
            <el-button type="text" style="color:#F56C6C" @click="audit(scope.row.id, 2)">拒绝</el-button>
          </template>
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
  data() { return { tableData: [], query: { status: null }, pageNum: 1, pageSize: 10, total: 0 } },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/registration/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize, ...this.query } })
      this.tableData = res.data.records; this.total = res.data.total
    },
    handlePage(val) { this.pageNum = val; this.loadData() },
    async audit(id, status) {
      await this.$request.put(`/api/registration/audit/${id}`, null, { params: { status } })
      this.$message.success('操作成功'); this.loadData()
    },
    async deleteRow(id) {
      await this.$request.delete(`/api/registration/${id}`)
      this.$message.success('删除成功'); this.loadData()
    }
  }
}
</script>
