<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item label="用户姓名" prop="appletUserName">
        <el-input
          v-model="queryParams.appletUserName"
          placeholder="请输入用户姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="小区" prop="districtName">
        <el-input
          v-model="queryParams.districtName"
          placeholder="请输入小区"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="楼栋" prop="buildingName">
        <el-input
          v-model="queryParams.buildingName"
          placeholder="请输入楼栋"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单元" prop="unitName">
        <el-input
          v-model="queryParams.unitName"
          placeholder="请输入单元"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="房号" prop="roomNumber">
        <el-input
          v-model="queryParams.roomNumber"
          placeholder="请输入房号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审核时间" prop="checkTime">
        <el-date-picker
          v-model="queryParams.checkTime"
          clearable
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择审核时间"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:check:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:check:edit']"
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:check:remove']"
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:check:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col> -->
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="checkList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="检查表ID" align="center" prop="checkId" />
      <el-table-column label="用户姓名" align="center" prop="appletUserName" />
      <el-table-column label="身份证号" align="center" prop="appletUserIdNumber" />
      <el-table-column label="小区" align="center" prop="districtName" />
      <el-table-column label="楼栋" align="center" prop="buildingName" />
      <el-table-column label="单元" align="center" prop="unitName" />
      <el-table-column label="房号" align="center" prop="roomNumber" />
      <el-table-column label="房屋证明" align="center" prop="certificate">
        <template slot-scope="scope">
          <el-image
            style="width: 100px; height: 100px"
            :src="baseApi + '/system/check/certificate?imagePath=' + scope.row.certificate"
            :preview-src-list="[baseApi + '/system/check/certificate?imagePath=' + scope.row.certificate]"
          />
        </template>
      </el-table-column>
      <el-table-column label="审核状态" align="center" prop="checkStatus">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.checkStatus === 'success'" type="success">已通过</el-tag>
          <el-tag v-else-if="scope.row.checkStatus === 'fail'" type="danger">未通过</el-tag>
          <el-tag v-else type="info">待审核</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="审核失败信息" align="center" prop="checkErrorMsg" />
      <el-table-column label="审核时间" align="center" prop="checkTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.checkTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <!-- <el-button
            v-hasPermi="['system:check:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-hasPermi="['system:check:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button> -->
          <el-button
            v-if="scope.row.checkStatus === 'uncheck'"
            v-hasPermi="['system:check:edit']"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAudit(scope.row)"
          >审核</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改房屋绑定审核对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户姓名" prop="appletUserName">
          <el-input v-model="form.appletUserName" placeholder="请输入用户姓名" />
        </el-form-item>
        <el-form-item label="小区" prop="districtName">
          <el-input v-model="form.districtName" placeholder="请输入小区" />
        </el-form-item>
        <el-form-item label="楼栋" prop="buildingName">
          <el-input v-model="form.buildingName" placeholder="请输入楼栋" />
        </el-form-item>
        <el-form-item label="单元" prop="unitName">
          <el-input v-model="form.unitName" placeholder="请输入单元" />
        </el-form-item>
        <el-form-item label="房号" prop="roomNumber">
          <el-input v-model="form.roomNumber" placeholder="请输入房号" />
        </el-form-item>
        <el-form-item label="房屋证明" prop="certificate">
          <el-upload
            class="upload-demo"
            :action="uploadUrl"
            :headers="headers"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
            :file-list="fileList"
          >
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog title="审核" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" :rules="auditRules" label-width="80px">
        <el-form-item label="审核结果" prop="checkStatus">
          <el-radio-group v-model="auditForm.checkStatus">
            <el-radio label="success">通过</el-radio>
            <el-radio label="fail">不通过</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="auditForm.checkStatus === 'fail'" label="失败原因" prop="checkErrorMsg">
          <el-input v-model="auditForm.checkErrorMsg" type="textarea" placeholder="请输入审核失败原因" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAudit">确 定</el-button>
        <el-button @click="auditOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCheck, getCheck, delCheck, addCheck, updateCheck } from '@/api/buildings/check'
import { getToken } from '@/utils/auth'

export default {
  name: 'Check',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 房屋绑定审核表格数据
      checkList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 是否显示审核弹出层
      auditOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        appletUserName: null,
        districtName: null,
        buildingName: null,
        unitName: null,
        roomNumber: null,
        checkStatus: null,
        checkTime: null
      },
      // 表单参数
      form: {},
      // 审核表单参数
      auditForm: {
        checkId: null,
        checkStatus: 'success',
        checkErrorMsg: ''
      },
      // 表单校验
      rules: {
        appletUserName: [
          { required: true, message: '用户姓名不能为空', trigger: 'blur' }
        ],
        districtName: [
          { required: true, message: '小区不能为空', trigger: 'blur' }
        ],
        buildingName: [
          { required: true, message: '楼栋不能为空', trigger: 'blur' }
        ],
        unitName: [
          { required: true, message: '单元不能为空', trigger: 'blur' }
        ],
        roomNumber: [
          { required: true, message: '房号不能为空', trigger: 'blur' }
        ]
      },
      // 审核表单校验
      auditRules: {
        checkStatus: [
          { required: true, message: '请选择审核结果', trigger: 'change' }
        ],
        checkErrorMsg: [
          { required: true, message: '请输入审核失败原因', trigger: 'blur' }
        ]
      },
      // 上传地址
      uploadUrl: process.env.VUE_APP_BASE_API + '/system/check',
      // 上传文件列表
      fileList: [],
      // 上传请求头
      headers: {
        Authorization: 'Bearer ' + getToken()
      }
    }
  },
  computed: {
    baseApi() {
      return process.env.VUE_APP_BASE_API
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询房屋绑定审核列表 */
    getList() {
      this.loading = true
      listCheck(this.queryParams).then(response => {
        this.checkList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        checkId: null,
        appletUserName: null,
        districtName: null,
        buildingName: null,
        unitName: null,
        roomNumber: null,
        certificate: null,
        checkStatus: null,
        checkErrorMsg: null,
        createTime: null,
        checkTime: null,
        userId: null
      }
      this.resetForm('form')
      this.fileList = []
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.checkId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加房屋绑定审核'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const checkId = row.checkId || this.ids
      getCheck(checkId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改房屋绑定审核'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          const formData = new FormData()
          if (this.fileList.length > 0) {
            formData.append('file', this.fileList[0].raw)
          }
          formData.append('districtName', this.form.districtName)
          formData.append('buildingName', this.form.buildingName)
          formData.append('unitName', this.form.unitName)
          formData.append('roomNumber', this.form.roomNumber)

          if (this.form.checkId != null) {
            formData.append('checkId', this.form.checkId)
            updateCheck(formData).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addCheck(formData).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const checkIds = row.checkId || this.ids
      this.$modal.confirm('是否确认删除房屋绑定审核编号为"' + checkIds + '"的数据项？').then(function() {
        return delCheck(checkIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/check/export', {
        ...this.queryParams
      }, `check_${new Date().getTime()}.xlsx`)
    },
    /** 审核按钮操作 */
    handleAudit(row) {
      this.auditForm = {
        checkId: row.checkId,
        checkStatus: 'success',
        checkErrorMsg: ''
      }
      this.auditOpen = true
    },
    /** 提交审核 */
    submitAudit() {
      this.$refs['auditForm'].validate(valid => {
        if (valid) {
          const data = {
            checkId: this.auditForm.checkId,
            checkStatus: this.auditForm.checkStatus,
            checkErrorMsg: this.auditForm.checkStatus === 'fail' ? this.auditForm.checkErrorMsg : '',
            checkTime: new Date(),
            userId: this.$store.getters.userId
          }
          updateCheck(data).then(response => {
            this.$modal.msgSuccess('审核成功')
            this.auditOpen = false
            this.getList()
          })
        }
      })
    },
    // 文件上传成功处理
    handleUploadSuccess(response, file, fileList) {
      this.form.certificate = response.fileName
      this.$modal.msgSuccess('上传成功')
    },
    // 文件上传前的处理
    beforeUpload(file) {
      const isImage = file.type.indexOf('image') !== -1
      if (!isImage) {
        this.$modal.msgError('只能上传图片文件！')
        return false
      }
      return true
    }
  }
}
</script>
