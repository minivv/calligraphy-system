<template>
  <el-card>
    <div slot="header">
      <span>投票管理</span>
      <el-button type="primary" size="small" style="float:right" @click="openDialog(null)">新增投票</el-button>
    </div>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="title" label="标题" min-width="150"></el-table-column>
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="['info','success','danger'][scope.row.status]">{{ ['未开始','进行中','已结束'][scope.row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="startTime" label="开始时间" width="160"></el-table-column>
      <el-table-column prop="endTime" label="结束时间" width="160"></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
      <el-table-column label="操作" width="250">
        <template slot-scope="scope">
          <el-button type="text" @click="openDialog(scope.row)">编辑</el-button>
          <el-button type="text" @click="manageItems(scope.row)">管理选项</el-button>
          <el-button type="text" @click="updateStatus(scope.row.id, scope.row.status===0?1:2)">
            {{ scope.row.status===0?'开始':scope.row.status===1?'结束':'' }}
          </el-button>
          <el-popconfirm title="确定删除？" @confirm="deleteRow(scope.row.id)">
            <el-button slot="reference" type="text" style="color:#F56C6C">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination style="margin-top:15px" @current-change="handlePage" :current-page="pageNum" :page-size="pageSize" :total="total" layout="total, prev, pager, next"></el-pagination>

    <el-dialog :title="form.id?'编辑投票':'新增投票'" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="标题"><el-input v-model="form.title"></el-input></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.description" type="textarea" :rows="3"></el-input></el-form-item>
        <el-form-item label="开始时间"><el-date-picker v-model="form.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker></el-form-item>
        <el-form-item label="结束时间"><el-date-picker v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="管理投票选项" :visible.sync="itemDialogVisible" width="750px">
      <div style="margin-bottom:15px;display:flex;align-items:center">
        <el-select v-model="selectedWorkId" placeholder="选择要添加的作品" filterable style="width:350px;margin-right:10px">
          <el-option v-for="w in availableWorks" :key="w.id" :label="w.title + ' - ' + (w.username||w.nickname||'未知')" :value="w.id"></el-option>
        </el-select>
        <el-button type="primary" @click="addItem" :disabled="!selectedWorkId">添加作品</el-button>
      </div>
      <el-table :data="voteItems" border>
        <el-table-column prop="workTitle" label="作品标题"></el-table-column>
        <el-table-column prop="authorName" label="作者" width="120"></el-table-column>
        <el-table-column prop="voteCount" label="得票数" width="100"></el-table-column>
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <el-popconfirm title="确定移除？" @confirm="deleteItem(scope.row.id)">
              <el-button slot="reference" type="text" style="color:#F56C6C">移除</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  data() { return { tableData: [], pageNum: 1, pageSize: 10, total: 0, dialogVisible: false, form: {}, itemDialogVisible: false, currentVoteId: null, voteItems: [], selectedWorkId: null, availableWorks: [] } },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/vote/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize } })
      this.tableData = res.data.records; this.total = res.data.total
    },
    handlePage(val) { this.pageNum = val; this.loadData() },
    openDialog(row) {
      this.form = row ? { ...row } : { title: '', description: '', startTime: '', endTime: '' }
      this.dialogVisible = true
    },
    async save() {
      if (this.form.id) { await this.$request.put('/api/vote', this.form) }
      else { await this.$request.post('/api/vote', this.form) }
      this.$message.success('操作成功'); this.dialogVisible = false; this.loadData()
    },
    async updateStatus(id, status) {
      await this.$request.put(`/api/vote/status/${id}`, null, { params: { status } })
      this.$message.success('操作成功'); this.loadData()
    },
    async deleteRow(id) {
      await this.$request.delete(`/api/vote/${id}`)
      this.$message.success('删除成功'); this.loadData()
    },
    async manageItems(row) {
      this.currentVoteId = row.id
      this.selectedWorkId = null
      const [itemRes, workRes] = await Promise.all([
        this.$request.get(`/api/vote/${row.id}`),
        this.$request.get('/api/work/page', { params: { pageNum: 1, pageSize: 1000 } })
      ])
      this.voteItems = itemRes.data.items
      const existingWorkIds = this.voteItems.map(item => item.workId)
      this.availableWorks = (workRes.data.records || []).filter(w => !existingWorkIds.includes(w.id))
      this.itemDialogVisible = true
    },
    async addItem() {
      if (!this.selectedWorkId) { this.$message.warning('请选择作品'); return }
      await this.$request.post('/api/vote/item', { voteId: this.currentVoteId, workId: this.selectedWorkId })
      this.$message.success('添加成功'); this.selectedWorkId = null
      const [itemRes, workRes] = await Promise.all([
        this.$request.get(`/api/vote/${this.currentVoteId}`),
        this.$request.get('/api/work/page', { params: { pageNum: 1, pageSize: 1000 } })
      ])
      this.voteItems = itemRes.data.items
      const existingWorkIds = this.voteItems.map(item => item.workId)
      this.availableWorks = (workRes.data.records || []).filter(w => !existingWorkIds.includes(w.id))
    },
    async deleteItem(id) {
      await this.$request.delete(`/api/vote/item/${id}`)
      this.$message.success('移除成功')
      const [itemRes, workRes] = await Promise.all([
        this.$request.get(`/api/vote/${this.currentVoteId}`),
        this.$request.get('/api/work/page', { params: { pageNum: 1, pageSize: 1000 } })
      ])
      this.voteItems = itemRes.data.items
      const existingWorkIds = this.voteItems.map(item => item.workId)
      this.availableWorks = (workRes.data.records || []).filter(w => !existingWorkIds.includes(w.id))
    }
  }
}
</script>
