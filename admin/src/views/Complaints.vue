<template>
  <el-card>
    <div slot="header"><span>投诉管理</span></div>
    <el-form :inline="true" :model="query">
      <el-form-item>
        <el-select v-model="query.status" placeholder="状态" clearable>
          <el-option label="待处理" :value="0"></el-option>
          <el-option label="已处理" :value="1"></el-option>
          <el-option label="已驳回" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="title" label="标题" min-width="120"></el-table-column>
      <el-table-column prop="content" label="内容" min-width="200" show-overflow-tooltip></el-table-column>
      <el-table-column prop="username" label="投诉人" width="100"></el-table-column>
      <el-table-column prop="targetType" label="对象类型" width="100"></el-table-column>
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="['warning','success','danger'][scope.row.status]">{{ ['待处理','已处理','已驳回'][scope.row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reply" label="回复" min-width="150" show-overflow-tooltip></el-table-column>
      <el-table-column prop="createTime" label="投诉时间" width="160"></el-table-column>
      <el-table-column label="操作" width="120">
        <template slot-scope="scope">
          <el-button type="text" @click="openHandle(scope.row)" v-if="scope.row.status===0">处理</el-button>
          <el-popconfirm title="确定删除？" @confirm="deleteRow(scope.row.id)">
            <el-button slot="reference" type="text" style="color:#F56C6C">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:15px" @current-change="handlePage" :current-page="pageNum" :page-size="pageSize" :total="total" layout="total, prev, pager, next"></el-pagination>

    <el-dialog title="处理投诉" :visible.sync="handleVisible" width="500px">
      <el-form :model="handleForm" label-width="80px">
        <el-form-item label="处理结果">
          <el-select v-model="handleForm.status" style="width:100%">
            <el-option label="已处理" :value="1"></el-option>
            <el-option label="驳回" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="回复"><el-input v-model="handleForm.reply" type="textarea" :rows="3"></el-input></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="handleVisible=false">取消</el-button>
        <el-button type="primary" @click="submitHandle">确定</el-button>
      </span>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  data() { return { tableData: [], query: { status: null }, pageNum: 1, pageSize: 10, total: 0, handleVisible: false, handleForm: {}, currentId: null } },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/complaint/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize, ...this.query } })
      this.tableData = res.data.records; this.total = res.data.total
    },
    handlePage(val) { this.pageNum = val; this.loadData() },
    openHandle(row) {
      this.currentId = row.id
      this.handleForm = { status: 1, reply: '' }
      this.handleVisible = true
    },
    async submitHandle() {
      await this.$request.put(`/api/complaint/handle/${this.currentId}`, this.handleForm)
      this.$message.success('处理成功'); this.handleVisible = false; this.loadData()
    },
    async deleteRow(id) {
      await this.$request.delete(`/api/complaint/${id}`)
      this.$message.success('删除成功'); this.loadData()
    }
  }
}
</script>
