<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类型" prop="level">
        <el-select v-model="queryParams.level" placeholder="请选择类型" clearable>
          <el-option label="小区" :value="1" />
          <el-option label="楼栋" :value="2" />
          <el-option label="单元" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:building:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAddCommunity"
        >添加</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="selectedKeys.length === 0"
          @click="handleBatchDelete"
        >批量删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-folder-opened"
          size="mini"
          @click="collapseAll"
        >折叠全部</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-tree
      ref="tree"
      :data="treeData"
      :props="defaultProps"
      node-key="id"
      show-checkbox
      :default-expand-all="false"
      :highlight-current="true"
      :expand-on-click-node="true"
      :filter-node-method="filterNode"
      @check="handleCheckChange"
    >
      <span slot-scope="{ node, data }" class="custom-tree-node">
        <span :class="['tree-label', 'level-' + data.level]">
          <i :class="getIconByLevel(data.level)" />
          {{ node.label }}
          <el-tag size="mini" :type="getTagTypeByLevel(data.level)" effect="plain">
            {{ getLevelLabel(data.level) }}
          </el-tag>
        </span>
        <span class="operation-buttons">
          <el-button-group>
            <el-button
              v-if="data.level < 3"
              type="primary"
              size="mini"
              icon="el-icon-plus"
              @click.stop="handleAdd(data)"
            >添加</el-button>
            <el-button
              type="info"
              size="mini"
              icon="el-icon-edit"
              @click.stop="handleUpdate(data)"
            >修改</el-button>
            <el-button
              type="danger"
              size="mini"
              icon="el-icon-delete"
              @click.stop="handleDelete(data)"
            >删除</el-button>
          </el-button-group>
        </span>
      </span>
    </el-tree>

    <!-- 添加或修改对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="500px"
      append-to-body
      center
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :modal-append-to-body="false"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="类型" prop="level">
          <el-select v-model="form.level" placeholder="请选择类型" @change="handleLevelChange">
            <el-option label="小区" :value="1" />
            <el-option label="楼栋" :value="2" />
            <el-option label="单元" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="form.level === 2" label="所属小区" prop="communityId">
          <el-select v-model="form.pid" placeholder="请选择所属小区">
            <el-option
              v-for="item in communityOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="form.level === 3" label="所属楼栋" prop="pid">
          <el-cascader
            v-model="cascaderValue"
            :options="cascaderOptions"
            :props="cascaderProps"
            placeholder="请选择所属小区和楼栋"
            clearable
            @change="handleCascaderChange"
          />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item v-if="form.level === 1" label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        :on-exceed="handleExceed"
        :on-error="handleError"
        drag
      >
        <i class="el-icon-upload" />
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div slot="tip" class="el-upload__tip">
          <el-link :underline="false" type="primary" @click="downloadTemplate">下载模板</el-link>
          <span>仅允许导入xlsx、xls格式文件。</span>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBuilding, getBuilding, delBuilding, addBuilding, updateBuilding } from '@/api/buildings/building.js'
import { getToken } from '@/utils/auth'

export default {
  name: 'Building',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        name: null,
        level: null
      },
      // 树形数据
      treeData: [],
      // 树形配置选项
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      // 小区选项
      communityOptions: [],
      // 级联选择器的值
      cascaderValue: [],
      // 级联选择器配置
      cascaderProps: {
        value: 'id',
        label: 'name',
        children: 'children',
        checkStrictly: false,
        emitPath: true
      },
      // 级联选择器选项
      cascaderOptions: [],
      // 表单参数
      form: {
        id: null,
        pid: null,
        name: null,
        address: null,
        level: 1
      },
      // 表单校验
      rules: {
        name: [
          { required: true, message: '名称不能为空', trigger: 'blur' }
        ],
        level: [
          { required: true, message: '类型不能为空', trigger: 'change' }
        ],
        pid: [
          { required: true, message: '请选择所属楼栋', trigger: 'change' }
        ]
      },
      // 选中的节点key
      selectedKeys: [],
      // 弹窗拖动相关
      dialogDraggable: {
        isDragging: false,
        startX: 0,
        startY: 0,
        originalX: 0,
        originalY: 0
      },
      // 上传参数
      upload: {
        open: false,
        title: '导入数据',
        url: process.env.VUE_APP_BASE_API + '/system/building/import',
        headers: { Authorization: 'Bearer ' + getToken() },
        isUploading: false
      }
    }
  },
  watch: {
    // 监听搜索关键字变化
    'queryParams.name'(val) {
      this.$refs.tree && this.$refs.tree.filter(val)
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true
      listBuilding(this.queryParams).then(response => {
        if (this.queryParams.name || this.queryParams.level) {
          // 搜索时，构建完整的树形结构
          const allData = response.rows
          const searchResults = []

          allData.forEach(item => {
            if (this.matchSearchCriteria(item)) {
              // 找到匹配项的所有父节点
              const parents = this.findParents(item, allData)
              if (parents.length > 0) {
                // 将父节点添加到结果中
                let currentParent = parents[0]
                searchResults.push(currentParent)

                for (let i = 1; i < parents.length; i++) {
                  const parent = parents[i]
                  currentParent.children = currentParent.children || []
                  currentParent.children.push(parent)
                  currentParent = parent
                }

                // 添加当前节点
                currentParent.children = currentParent.children || []
                currentParent.children.push(item)
              } else {
                searchResults.push(item)
              }
            }
          })

          this.treeData = searchResults
        } else {
          this.treeData = this.handleTree(response.rows, 'id', 'pid')
        }
        this.total = response.total
        this.loading = false
      })
    },
    /** 匹配搜索条件 */
    matchSearchCriteria(item) {
      const nameMatch = !this.queryParams.name ||
        item.name.toLowerCase().includes(this.queryParams.name.toLowerCase())
      const levelMatch = !this.queryParams.level ||
        item.level === this.queryParams.level
      return nameMatch && levelMatch
    },
    /** 查找父节点 */
    findParents(item, allData) {
      const parents = []
      let currentPid = item.pid

      while (currentPid) {
        const parent = allData.find(d => d.id === currentPid)
        if (parent) {
          parents.unshift(parent)
          currentPid = parent.pid
        } else {
          break
        }
      }

      return parents
    },
    /** 树节点过滤方法 */
    filterNode(value, data) {
      if (!value) return true
      return data.name.toLowerCase().indexOf(value.toLowerCase()) !== -1
    },
    /** 添加按钮操作 */
    handleAddCommunity() {
      this.reset()
      this.form.level = 1 // 默认选择小区类型
      this.open = true
      this.title = '添加'
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        pid: null,
        name: null,
        address: null,
        level: 1
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.queryParams.level = null
      this.handleQuery()
    },
    /** 加载小区列表 */
    loadCommunityOptions() {
      listBuilding({ level: 1 }).then(response => {
        this.communityOptions = response.rows
      })
    },
    /** 加载级联选择器数据 */
    loadCascaderOptions(currentBuildingId = null) {
      listBuilding({ level: 1 }).then(response => {
        const communities = response.rows
        // 获取所有楼栋
        listBuilding({ level: 2 }).then(buildingRes => {
          const buildings = buildingRes.rows
          // 如果有当前楼栋ID，找到对应的楼栋和小区
          if (currentBuildingId) {
            const currentBuilding = buildings.find(b => b.id === currentBuildingId)
            if (currentBuilding) {
              this.form.pid = currentBuilding.id
              this.cascaderValue = [currentBuilding.pid, currentBuilding.id]
            }
          }
          // 构建级联数据
          this.cascaderOptions = communities.map(community => {
            return {
              id: community.id,
              name: community.name,
              children: buildings
                .filter(building => building.pid === community.id)
                .map(building => ({
                  id: building.id,
                  name: building.name
                }))
            }
          })
        })
      })
    },
    /** 处理级联选择器变化 */
    handleCascaderChange(value) {
      if (value && value.length > 0) {
        this.form.pid = value[value.length - 1]
        // 手动触发表单验证
        this.$nextTick(() => {
          this.$refs.form.validateField('pid')
        })
      }
    },
    /** 处理类型变化 */
    handleLevelChange(value) {
      this.form.pid = null
      this.cascaderValue = []
      if (value === 2) {
        this.loadCommunityOptions()
      } else if (value === 3) {
        this.loadCascaderOptions()
      }
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset()
      if (row) {
        this.form.pid = row.id
        this.form.level = row.level + 1
        if (this.form.level === 2) {
          this.loadCommunityOptions()
          // 设置所属小区
          this.form.pid = row.id
        } else if (this.form.level === 3) {
          this.loadCascaderOptions(row.id)
        }
      }
      this.open = true
      this.title = '添加'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      getBuilding(row.id).then(response => {
        this.form = response.data
        if (this.form.level === 2) {
          this.loadCommunityOptions()
          // 设置所属小区
          listBuilding({ id: this.form.pid }).then(res => {
            if (res.rows.length > 0) {
              const community = res.rows[0]
              this.form.pid = community.id
            }
          })
        } else if (this.form.level === 3) {
          // 先加载级联选择器数据
          listBuilding({ level: 2, id: this.form.pid }).then(buildingRes => {
            if (buildingRes.rows.length > 0) {
              const building = buildingRes.rows[0]
              // 获取楼栋所属的小区
              listBuilding({ level: 1, id: building.pid }).then(communityRes => {
                if (communityRes.rows.length > 0) {
                  const community = communityRes.rows[0]
                  this.loadCascaderOptions()
                  // 设置级联选择器的值为 [小区ID, 楼栋ID]
                  this.cascaderValue = [community.id, building.id]
                }
              })
            }
          })
        }
        this.open = true
        this.title = '修改'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBuilding(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addBuilding(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除该数据项？').then(function() {
        return delBuilding(row.id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    // 构建树形数据
    handleTree(data, id, pid) {
      const config = {
        id: id,
        pid: pid,
        children: 'children'
      }
      var childrenListMap = {}
      var nodeIds = {}
      var tree = []

      for (const d of data) {
        const parentId = d[config.pid]
        if (childrenListMap[parentId] == null) {
          childrenListMap[parentId] = []
        }
        nodeIds[d[config.id]] = d
        childrenListMap[parentId].push(d)
      }

      for (const d of data) {
        const parentId = d[config.pid]
        if (nodeIds[parentId] == null) {
          tree.push(d)
        }
      }

      for (const t of tree) {
        adaptToChildrenList(t)
      }

      function adaptToChildrenList(o) {
        if (childrenListMap[o[config.id]] !== null) {
          o[config.children] = childrenListMap[o[config.id]]
        }
        if (o[config.children]) {
          for (const c of o[config.children]) {
            adaptToChildrenList(c)
          }
        }
      }
      return tree
    },
    /** 处理复选框选择 */
    handleCheckChange(data, checked) {
      this.selectedKeys = checked.checkedKeys
    },
    /** 批量删除 */
    handleBatchDelete() {
      this.$modal.confirm('是否确认删除选中的' + this.selectedKeys.length + '条数据项？').then(() => {
        const deletePromises = this.selectedKeys.map(id => delBuilding(id))
        Promise.all(deletePromises).then(() => {
          this.getList()
          this.$modal.msgSuccess('批量删除成功')
          this.selectedKeys = []
        })
      }).catch(() => {})
    },
    /** 根据层级获取图标 */
    getIconByLevel(level) {
      const icons = {
        1: 'el-icon-office-building',
        2: 'el-icon-school',
        3: 'el-icon-house'
      }
      return icons[level] || ''
    },
    /** 根据层级获取标签类型 */
    getTagTypeByLevel(level) {
      const types = {
        1: 'success',
        2: 'warning',
        3: 'info'
      }
      return types[level] || ''
    },
    /** 获取层级标签文本 */
    getLevelLabel(level) {
      const labels = {
        1: '小区',
        2: '楼栋',
        3: '单元'
      }
      return labels[level] || ''
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.open = true
    },
    /** 下载模板操作 */
    downloadTemplate() {
      // 创建工作簿
      const wb = XLSX.utils.book_new()

      // 创建模板数据
      const templateData = [
        this.excel.headers,
        ['碧桂园', 'A栋', '101', '广东省广州'],
        ['碧桂园', 'A栋', '102', '广东省广州'],
        ['碧桂园', 'A栋', '103', '广东省广州']
      ]

      // 创建工作表
      const ws = XLSX.utils.aoa_to_sheet(templateData)

      // 设置列宽
      ws['!cols'] = [
        { wch: 20 }, // 小区
        { wch: 15 }, // 楼栋
        { wch: 15 }, // 单元
        { wch: 30 } // 地址
      ]

      // 将工作表添加到工作簿
      XLSX.utils.book_append_sheet(wb, ws, '物业小区导入模板')

      // 生成Excel文件并下载
      const excelBuffer = XLSX.write(wb, { bookType: 'xlsx', type: 'array' })
      const blob = new Blob([excelBuffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
      FileSaver.saveAs(blob, '物业小区导入模板.xlsx')
    },
    /** 导出按钮操作 */
    handleExport() {
      this.$confirm('是否确认导出所有数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.download('system/building/export', {
          ...this.queryParams
        }, `物业小区数据_${new Date().getTime()}.xlsx`)
      })
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false
      this.upload.isUploading = false
      this.$refs.upload.clearFiles()
      this.$alert(response.msg || '导入成功', '导入结果', { type: response.code === 200 ? 'success' : 'error' })
      this.getList()
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit()
    },
    // 文件超出个数限制处理
    handleExceed() {
      this.$message.warning('最多只能上传1个文件')
    },
    // 上传失败处理
    handleError(err) {
      this.upload.isUploading = false
      this.$message.error('导入失败，请重试')
    },
    /** 折叠全部 */
    collapseAll() {
      const allNodes = this.$refs.tree.store._getAllNodes()
      allNodes.forEach(node => {
        node.expanded = false
      })
    }
  }
}
</script>

<style scoped>
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding: 8px 16px;
  width: 100%;
  border-radius: 4px;
  transition: all 0.3s;
  margin: 4px 0;
  cursor: pointer;
}

.tree-label {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.tree-label i {
  font-size: 18px;
}

.tree-label .el-tag {
  margin-left: 12px;
  padding: 0 8px;
  height: 22px;
  line-height: 20px;
  border-radius: 11px;
  font-size: 12px;
}

.level-1 {
  font-size: 16px;
  font-weight: 600;
  color: #1890ff;
}

.level-2 {
  font-size: 15px;
  font-weight: 500;
  color: #2f4f4f;
}

.level-3 {
  font-size: 14px;
  color: #666;
}

.operation-buttons {
  opacity: 0;
  transition: opacity 0.3s;
  display: flex;
  align-items: center;
  margin-left: 16px;
}

.operation-buttons .el-button {
  padding: 4px 8px;
  font-size: 12px;
  border-radius: 4px;
  margin: 0;
}

.operation-buttons .el-button + .el-button {
  margin-left: -1px;
}

.custom-tree-node:hover {
  background-color: #fafafa;
}

.custom-tree-node:hover .operation-buttons {
  opacity: 1;
}

.el-tree-node__content {
  height: auto !important;
  min-height: 40px;
  margin: 2px 0;
}

.el-tree-node__content:hover {
  background-color: transparent !important;
}

/* 移除点击后的浅蓝色背景 */
.el-tree-node.is-current > .el-tree-node__content .custom-tree-node {
  background-color: transparent;
}

/* 移除选中后的背景色 */
.el-tree-node.is-checked > .el-tree-node__content .custom-tree-node {
  background-color: transparent;
}

.el-tree {
  padding: 12px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.el-tag.el-tag--success {
  background-color: #e6f7ff;
  border-color: #91d5ff;
  color: #1890ff;
}

.el-tag.el-tag--warning {
  background-color: #fff7e6;
  border-color: #ffd591;
  color: #fa8c16;
}

.el-tag.el-tag--info {
  background-color: #f5f5f5;
  border-color: #d9d9d9;
  color: #666;
}

.el-tree-node__expand-icon {
  padding: 6px;
  font-size: 14px;
  color: #666;
  transition: transform 0.3s;
}

.el-tree-node__expand-icon.expanded {
  transform: rotate(90deg);
}

/* 添加分页样式 */
.pagination-container {
  margin-top: 16px;
  padding: 16px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.mb8 {
  margin-bottom: 8px;
}
</style>
