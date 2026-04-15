<template>
  <el-card>
    <div slot="header">
      <span>活动管理</span>
      <el-button type="primary" size="small" style="float:right" @click="openDialog(null)">新增活动</el-button>
    </div>
    <el-form :inline="true" :model="query">
      <el-form-item><el-input v-model="query.title" placeholder="活动标题" clearable></el-input></el-form-item>
      <el-form-item><el-button type="primary" @click="loadData">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="title" label="标题" min-width="150"></el-table-column>
      <el-table-column prop="location" label="地点" width="150"></el-table-column>
      <el-table-column label="人数" width="100">
        <template slot-scope="scope">{{ scope.row.currentParticipants }}/{{ scope.row.maxParticipants }}</template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="['info','success','danger'][scope.row.status]">{{ ['未开始','进行中','已结束'][scope.row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="startTime" label="开始时间" width="160"></el-table-column>
      <el-table-column prop="endTime" label="结束时间" width="160"></el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button type="text" @click="openDialog(scope.row)">编辑</el-button>
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

    <el-dialog :title="form.id?'编辑活动':'新增活动'" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="活动标题"><el-input v-model="form.title"></el-input></el-form-item>
        <el-form-item label="活动描述"><el-input v-model="form.description" type="textarea" :rows="3"></el-input></el-form-item>
        <el-form-item label="活动地点"><el-input v-model="form.location"></el-input></el-form-item>
        <el-form-item label="最大人数"><el-input-number v-model="form.maxParticipants" :min="0"></el-input-number></el-form-item>
        <el-form-item label="封面图片">
          <el-upload action="/api/file/upload" :show-file-list="false" :on-success="handleUpload">
            <el-image v-if="form.coverImage" :src="form.coverImage" style="width:150px;height:100px" fit="cover"></el-image>
            <el-button v-else size="small" type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="开始时间"><el-date-picker v-model="form.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker></el-form-item>
        <el-form-item label="结束时间"><el-date-picker v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </span>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  data() { return { tableData: [], query: { title: '' }, pageNum: 1, pageSize: 10, total: 0, dialogVisible: false, form: {} } },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await this.$request.get('/api/activity/page', { params: { pageNum: this.pageNum, pageSize: this.pageSize, ...this.query } })
      this.tableData = res.data.records; this.total = res.data.total
    },
    handlePage(val) { this.pageNum = val; this.loadData() },
    openDialog(row) {
      this.form = row ? { ...row } : { title: '', description: '', location: '', maxParticipants: 50, coverImage: '', startTime: '', endTime: '' }
      this.dialogVisible = true
    },
    handleUpload(res) { if (res.code === 200) this.form.coverImage = res.data },
    async save() {
      if (this.form.id) { await this.$request.put('/api/activity', this.form) }
      else { await this.$request.post('/api/activity', this.form) }
      this.$message.success('操作成功'); this.dialogVisible = false; this.loadData()
    },
    async updateStatus(id, status) {
      await this.$request.put(`/api/activity/status/${id}`, null, { params: { status } })
      this.$message.success('操作成功'); this.loadData()
    },
    async deleteRow(id) {
      await this.$request.delete(`/api/activity/${id}`)
      this.$message.success('删除成功'); this.loadData()
    }
  }
}
</script>
