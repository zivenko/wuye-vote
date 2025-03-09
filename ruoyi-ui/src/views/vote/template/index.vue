<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="投票状态" clearable>
          <el-option label="全部" :value="-1" />
          <el-option label="未开始" :value="0" />
          <el-option label="进行中" :value="1" />
          <el-option label="已结束" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="时间范围" prop="timeRange">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
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
          v-hasPermi="['system:template:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:template:edit']"
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
          v-hasPermi="['system:template:remove']"
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
          v-hasPermi="['system:template:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="templateList" @selection-change="handleSelectionChange" @row-click="handleView">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="模板ID" align="center" prop="templateId" />
      <el-table-column label="标题" align="center" prop="title" :show-overflow-tooltip="true" />
      <el-table-column label="描述" align="center" prop="description" :show-overflow-tooltip="true" />
      <el-table-column label="投票状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ getStatusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="开始时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:template:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click.stop="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-hasPermi="['system:template:remove']"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click.stop="handleDelete(scope.row)"
          >删除</el-button>
          <el-button
            v-hasPermi="['system:template:add']"
            size="mini"
            type="text"
            icon="el-icon-document-copy"
            @click.stop="handleCopy(scope.row)"
          >复制</el-button>
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

    <!-- 添加或修改投票模板对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="780px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="投票标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入投票标题" />
        </el-form-item>
        <el-form-item label="投票描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入投票描述" />
        </el-form-item>
        <el-form-item label="投票选项" prop="choices">
          <div v-for="(choice, index) in choicesList" :key="index" style="display: flex; align-items: center; margin-bottom: 10px;">
            <el-input v-model="choice.content" placeholder="请输入选项内容" style="margin-right: 10px;">
              <template slot="prepend">{{ String.fromCharCode(65 + index) }}</template>
            </el-input>
            <el-button type="danger" icon="el-icon-delete" circle :disabled="choicesList.length <= 2" @click="removeChoice(index)" />
          </div>
          <el-button type="primary" icon="el-icon-plus" style="margin-top: 10px;" @click="addChoice">添加选项</el-button>
        </el-form-item>
        <el-form-item label="投票次数" prop="voteTimes">
          <el-input-number v-model="form.voteTimes" :min="1" :max="10" label="每人可投票次数" />
        </el-form-item>
        <el-form-item label="投票规则" prop="rule">
          <el-input v-model="form.rule" type="textarea" placeholder="请输入投票规则" />
        </el-form-item>
        <el-form-item label="所属小区" prop="communityIds">
          <el-select v-model="form.communityIds" multiple placeholder="请选择小区" style="width: 100%;">
            <el-option
              v-for="item in communityOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="投票时间" prop="dateRange">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd HH:mm:ss"
            :default-time="['00:00:00', '23:59:59']"
            style="width: 100%;"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <template v-if="title !== '查看投票模板'">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </template>
        <template v-else>
          <el-button @click="cancel">关 闭</el-button>
        </template>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTemplate, getTemplate, delTemplate, addTemplate, updateTemplate } from '@/api/vote/template'
import { listBuilding } from '@/api/buildings/building'

export default {
  name: 'Template',
  dicts: ['vote_status'],
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
      // 投票模板表格数据
      templateList: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        status: -1
      },
      // 表单
      form: {
        templateId: null,
        title: null,
        description: null,
        choices: null,
        voteTimes: 1,
        rule: null,
        communityIds: [],
        startTime: null,
        endTime: null
      },
      // 表单打开状态
      open: false,
      // 表单标题
      title: '',
      // 表单校验
      rules: {
        title: [
          { required: true, message: '投票标题不能为空', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '投票描述不能为空', trigger: 'blur' }
        ],
        voteTimes: [
          { required: true, message: '投票次数不能为空', trigger: 'change' }
        ],
        communityIds: [
          { required: true, message: '请选择所属小区', trigger: 'change' }
        ]
      },
      // 选项列表
      choicesList: [
        { content: '' },
        { content: '' }
      ],
      // 小区选项
      communityOptions: []
    }
  },
  created() {
    this.getList()
    this.loadCommunityOptions()
  },
  methods: {
    /** 查询投票模板列表 */
    getList() {
      this.loading = true
      listTemplate(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.templateList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 加载小区数据 */
    loadCommunityOptions() {
      listBuilding({ level: 1 }).then(response => {
        this.communityOptions = response.rows || []
      })
    },
    /** 取消按钮 */
    cancel() {
      this.open = false
      this.reset()
    },
    /** 表单重置 */
    reset() {
      this.form = {
        templateId: null,
        title: null,
        description: null,
        choices: null,
        voteTimes: 1,
        rule: null,
        communityIds: [],
        startTime: null,
        endTime: null
      }
      this.dateRange = []
      this.choicesList = [
        { content: '' },
        { content: '' }
      ]
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.queryParams.status = -1
      this.queryParams.pageNum = 1
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.templateId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加投票模板'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const templateId = row.templateId || this.ids
      getTemplate(templateId).then(response => {
        this.form = response.data
        // 处理选项数据
        try {
          const choices = JSON.parse(this.form.choices || '{}')
          this.choicesList = Object.entries(choices).map(([key, value]) => ({
            content: value.content
          }))
          if (this.choicesList.length < 2) {
            this.choicesList = [
              { content: '' },
              { content: '' }
            ]
          }
        } catch (e) {
          this.choicesList = [
            { content: '' },
            { content: '' }
          ]
        }
        // 处理小区ID
        if (this.form.communityIds) {
          this.form.communityIds = this.form.communityIds.split(',').map(Number)
        } else {
          this.form.communityIds = []
        }
        // 处理时间
        if (this.form.startTime && this.form.endTime) {
          this.dateRange = [this.parseTime(this.form.startTime), this.parseTime(this.form.endTime)]
        }
        this.open = true
        this.title = '修改投票模板'
      })
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.reset()
      const templateId = row.templateId || this.ids
      getTemplate(templateId).then(response => {
        this.form = response.data
        // 处理选项数据
        try {
          const choices = JSON.parse(this.form.choices || '{}')
          this.choicesList = Object.entries(choices).map(([key, value]) => ({
            content: value.content
          }))
        } catch (e) {
          this.choicesList = []
        }
        // 处理小区ID
        if (this.form.communityIds) {
          this.form.communityIds = this.form.communityIds.split(',').map(Number)
        }
        // 处理时间
        if (this.form.startTime && this.form.endTime) {
          this.dateRange = [this.parseTime(this.form.startTime), this.parseTime(this.form.endTime)]
        }
        this.open = true
        this.title = '查看投票模板'

        // 设置表单只读
        this.$nextTick(() => {
          const form = this.$refs.form
          if (form) {
            const inputs = form.$el.querySelectorAll('input, textarea')
            inputs.forEach(el => {
              el.setAttribute('readonly', 'readonly')
            })

            const selects = form.$el.querySelectorAll('.el-select input')
            selects.forEach(el => {
              el.setAttribute('disabled', 'disabled')
            })

            const datePicker = form.$el.querySelector('.el-date-editor')
            if (datePicker) {
              datePicker.setAttribute('disabled', 'disabled')
            }

            const buttons = form.$el.querySelectorAll('.el-button:not(.dialog-footer .el-button)')
            buttons.forEach(el => {
              el.setAttribute('disabled', 'disabled')
            })
          }
        })
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const templateIds = row.templateId || this.ids
      this.$modal.confirm('是否确认删除投票模板编号为"' + templateIds + '"的数据项？').then(() => {
        return delTemplate(templateIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/template/export', {
        ...this.queryParams
      }, `template_${new Date().getTime()}.xlsx`)
    },
    // 获取状态标签样式
    getStatusType(status) {
      if (status === 0) return 'info' // 未开始
      if (status === 1) return 'success' // 进行中
      if (status === 2) return 'danger' // 已结束
      return 'info'
    },
    // 获取状态文字
    getStatusLabel(status) {
      if (status === 0) return '未开始'
      if (status === 1) return '进行中'
      if (status === 2) return '已结束'
      return '未知'
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          // 验证时间范围
          if (!this.dateRange || this.dateRange.length !== 2) {
            this.$message.error('请选择投票时间范围')
            return
          }

          // 验证选项内容
          if (!this.choicesList.every(choice => choice.content.trim())) {
            this.$message.error('请填写所有选项内容')
            return
          }

          // 处理选项数据
          const choices = {}
          this.choicesList.forEach((choice, index) => {
            choices[String.fromCharCode(65 + index)] = { content: choice.content }
          })

          const data = {
            ...this.form,
            choices: JSON.stringify(choices),
            startTime: this.dateRange[0],
            endTime: this.dateRange[1]
          }

          if (this.form.communityIds && this.form.communityIds.length > 0) {
            data.communityIds = this.form.communityIds.join(',')
          }

          if (this.form.templateId != null) {
            updateTemplate(data).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            }).catch(() => {
              this.$modal.msgError('修改失败')
            })
          } else {
            addTemplate(data).then(() => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            }).catch(() => {
              this.$modal.msgError('新增失败')
            })
          }
        }
      })
    },
    // 添加选项
    addChoice() {
      if (this.choicesList.length >= 10) {
        this.$message.warning('最多只能添加10个选项')
        return
      }
      this.choicesList.push({ content: '' })
    },
    // 删除选项
    removeChoice(index) {
      if (this.choicesList.length <= 2) {
        return
      }
      this.choicesList.splice(index, 1)
    },
    /** 复制按钮操作 */
    handleCopy(row) {
      this.reset()
      const templateId = row.templateId
      getTemplate(templateId).then(response => {
        this.form = response.data
        this.form.templateId = null // 清空ID，作为新模板
        this.form.title = this.form.title + '的副本' // 标题加上副本标识

        // 处理选项数据
        try {
          const choices = JSON.parse(this.form.choices || '{}')
          this.choicesList = Object.entries(choices).map(([key, value]) => ({
            content: value.content
          }))
          if (this.choicesList.length < 2) {
            this.choicesList = [
              { content: '' },
              { content: '' }
            ]
          }
        } catch (e) {
          this.choicesList = [
            { content: '' },
            { content: '' }
          ]
        }

        // 处理小区ID
        if (this.form.communityIds) {
          this.form.communityIds = this.form.communityIds.split(',').map(Number)
        } else {
          this.form.communityIds = []
        }

        // 处理时间
        if (this.form.startTime && this.form.endTime) {
          this.dateRange = [this.parseTime(this.form.startTime), this.parseTime(this.form.endTime)]
        }

        this.open = true
        this.title = '添加投票模板'
      })
    }
  }
}
</script>

<style scoped>
.el-select {
  width: 100%;
}
.el-date-picker {
  width: 100%;
}
</style>
