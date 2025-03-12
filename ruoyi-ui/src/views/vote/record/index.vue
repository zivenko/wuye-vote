<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item label="用户ID" prop="appletId">
        <el-input
          v-model="queryParams.appletId"
          placeholder="请输入用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模板ID" prop="templateId">
        <el-input
          v-model="queryParams.templateId"
          placeholder="请输入模板ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户选择的选项" prop="choices">
        <el-input
          v-model="queryParams.choices"
          placeholder="请输入用户选择的选项"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投票时间" prop="voteTime">
        <el-date-picker
          v-model="queryParams.voteTime"
          clearable
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择投票时间"
        />
      </el-form-item>
      <el-form-item label="通过管理员投票" prop="byAdmin">
        <el-input
          v-model="queryParams.byAdmin"
          placeholder="请输入通过管理员投票"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:record:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:record:edit']"
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
          v-hasPermi="['system:record:remove']"
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
          v-hasPermi="['system:record:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="投票记录ID" align="center" prop="voteId" />
      <el-table-column label="用户ID" align="center" prop="appletId" />
      <el-table-column label="模板ID" align="center" prop="templateId" />
      <el-table-column label="用户选择的选项" align="center" prop="choices" />
      <el-table-column label="投票时间" align="center" prop="voteTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.voteTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="投票状态(有效: 1/无效: 0)" align="center" prop="status" />
      <el-table-column label="通过管理员投票" align="center" prop="byAdmin" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:record:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-hasPermi="['system:record:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
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

    <!-- 添加或修改投票记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-steps :active="activeStep" finish-status="success" simple style="margin-bottom: 20px">
        <el-step title="查找用户" />
        <el-step title="选择模板" />
        <el-step title="投票选项" />
      </el-steps>

      <!-- 步骤1：查找用户 -->
      <el-form v-if="activeStep === 0" ref="userForm" :model="userForm" :rules="userRules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="userForm.name" placeholder="请输入用户姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="mobile">
          <el-input v-model="userForm.mobile" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchUser">下一步</el-button>
          <el-button @click="cancel">取 消</el-button>
        </el-form-item>
      </el-form>

      <!-- 步骤2：选择模板 -->
      <el-form v-if="activeStep === 1" ref="templateForm" :model="templateForm" :rules="templateRules" label-width="80px">
        <el-form-item label="投票模板" prop="templateId">
          <el-select v-model="templateForm.templateId" placeholder="请选择投票模板" @change="handleTemplateChange">
            <el-option
              v-for="item in templateOptions"
              :key="item.templateId"
              :label="item.title"
              :value="item.templateId"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="nextStep">下一步</el-button>
          <el-button @click="prevStep">上一步</el-button>
        </el-form-item>
      </el-form>

      <!-- 步骤3：选择投票选项 -->
      <el-form v-if="activeStep === 2" ref="voteForm" :model="voteForm" :rules="voteRules" label-width="80px">
        <el-form-item label="投票选项" prop="choices">
          <div class="vote-info">可选 {{ currentTemplate ? currentTemplate.rule : 0 }} 项，剩余可选 {{ remainingChoices }} 项</div>
          <el-checkbox-group v-model="voteForm.choices" :max="currentTemplate ? parseInt(currentTemplate.rule) : 1" class="checkbox-list" @change="handleChoicesChange">
            <el-checkbox
              v-for="(choice, index) in choiceOptions"
              :key="index"
              :label="index"
              class="checkbox-item"
            >
              选项{{ choice.letter }}: {{ choice.content }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitVote">确 定</el-button>
          <el-button @click="prevStep">上一步</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { listRecord, getRecord, delRecord, addRecord, updateRecord, searchAppletUser, listTemplateByHouseIds } from '@/api/vote/record'
import store from '@/store'

export default {
  name: 'Record',
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
      // 投票记录表格数据
      recordList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        appletId: null,
        templateId: null,
        choices: null,
        voteTime: null,
        status: null,
        byAdmin: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        appletId: [
          { required: true, message: '用户ID不能为空', trigger: 'blur' }
        ],
        templateId: [
          { required: true, message: '模板ID不能为空', trigger: 'blur' }
        ],
        choices: [
          { required: true, message: '用户选择的选项不能为空', trigger: 'blur' }
        ]
      },
      // 步骤控制
      activeStep: 0,

      // 用户表单
      userForm: {
        name: '',
        mobile: ''
      },
      userRules: {
        name: [
          { required: true, message: '用户姓名不能为空', trigger: 'blur' }
        ],
        mobile: [
          { required: true, message: '手机号不能为空', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ]
      },

      // 模板表单
      templateForm: {
        templateId: undefined
      },
      templateRules: {
        templateId: [
          { required: true, message: '请选择投票模板', trigger: 'change' }
        ]
      },
      templateOptions: [],
      currentTemplate: null,

      // 投票表单
      voteForm: {
        choices: []
      },
      voteRules: {
        choices: [
          { required: true, message: '请选择投票选项', trigger: 'change' },
          { type: 'array', min: 1, message: '请至少选择一个选项', trigger: 'change' }
        ]
      },

      // 当前选中的用户ID
      currentAppletId: null,

      // 投票选项数据
      choiceOptions: [],
      remainingChoices: 0
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询投票记录列表 */
    getList() {
      this.loading = true
      listRecord(this.queryParams).then(response => {
        this.recordList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    /** 表单重置 */
    reset() {
      this.form = {
        voteId: null,
        appletId: null,
        templateId: null,
        choices: null,
        voteTime: null,
        status: null,
        byAdmin: null,
        createTime: null,
        updateTime: null
      }
      this.activeStep = 0
      this.userForm = {
        name: '',
        mobile: ''
      }
      this.templateForm = {
        templateId: undefined
      }
      this.voteForm = {
        choices: []
      }
      this.currentAppletId = null
      this.currentTemplate = null
      this.resetForm('userForm')
      this.resetForm('templateForm')
      this.resetForm('voteForm')
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
      this.ids = selection.map(item => item.voteId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加投票记录'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const voteId = row.voteId || this.ids
      getRecord(voteId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改投票记录'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.voteId != null) {
            updateRecord(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addRecord(this.form).then(response => {
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
      const voteIds = row.voteId || this.ids
      this.$modal.confirm('是否确认删除投票记录编号为"' + voteIds + '"的数据项？').then(function() {
        return delRecord(voteIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/record/export', {
        ...this.queryParams
      }, `record_${new Date().getTime()}.xlsx`)
    },
    /** 搜索用户 */
    searchUser() {
      this.$refs['userForm'].validate(valid => {
        if (valid) {
          searchAppletUser(this.userForm).then(response => {
            if (response.data) {
              this.currentAppletId = response.data.appletId
              // 获取用户可投票的模板列表
              this.getTemplateOptions(response.data.houseIds)
              this.activeStep = 1
            } else {
              this.$modal.msgError('未找到该用户信息')
            }
          })
        }
      })
    },

    /** 获取模板选项 */
    getTemplateOptions(houseIds) {
      listTemplateByHouseIds(houseIds).then(response => {
        this.templateOptions = response.data
      })
    },

    /** 处理模板变更 */
    handleTemplateChange(templateId) {
      this.currentTemplate = this.templateOptions.find(t => t.templateId === templateId)
      if (this.currentTemplate) {
        try {
          this.currentTemplate.rule = parseInt(this.currentTemplate.rule) || 1
          this.remainingChoices = this.currentTemplate.rule
          const choicesObj = JSON.parse(this.currentTemplate.choices)
          // 将对象转换为数组格式，保持字母顺序
          this.choiceOptions = Object.entries(choicesObj).map(([letter, data]) => ({
            letter,
            ...data
          })).sort((a, b) => a.letter.localeCompare(b.letter))
          // 重置选项
          this.voteForm.choices = []
        } catch (e) {
          this.currentTemplate.rule = 1
          this.remainingChoices = 1
          this.choiceOptions = []
          this.voteForm.choices = []
        }
      } else {
        this.voteForm.choices = []
      }
    },

    /** 下一步 */
    nextStep() {
      if (this.activeStep === 1) {
        this.$refs['templateForm'].validate(valid => {
          if (valid) {
            this.activeStep = 2
          }
        })
      }
    },

    /** 上一步 */
    prevStep() {
      this.activeStep--
    },

    /** 处理选项变化 */
    handleChoicesChange(value) {
      if (this.currentTemplate) {
        this.remainingChoices = this.currentTemplate.rule - value.length
      }
    },

    /** 提交投票 */
    submitVote() {
      this.$refs['voteForm'].validate(valid => {
        if (valid) {
          // 获取选中选项的字母
          const selectedLetters = this.voteForm.choices.map(index => this.choiceOptions[index].letter)

          const data = {
            appletId: this.currentAppletId,
            templateId: this.templateForm.templateId,
            choices: selectedLetters.join(','),
            status: 1,
            byAdmin: store.getters.userId
          }

          addRecord(data).then(response => {
            this.$modal.msgSuccess('投票成功')
            this.open = false
            this.getList()
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.vote-info {
  margin-bottom: 10px;
  color: #606266;
  font-size: 14px;
}

.checkbox-list {
  display: flex;
  flex-direction: column;
}

.checkbox-item {
  margin: 8px 0;
  display: block;
}

.checkbox-item:first-child {
  margin-top: 0;
}

.checkbox-item:last-child {
  margin-bottom: 0;
}
</style>
