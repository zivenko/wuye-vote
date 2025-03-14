<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item label="小区" prop="districtId">
        <el-select v-model="queryParams.districtId" placeholder="请选择小区" clearable @change="handleDistrictChange">
          <el-option
            v-for="item in districtOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="楼栋" prop="buildingId">
        <el-select v-model="queryParams.buildingId" placeholder="请选择楼栋" clearable @change="handleBuildingChange">
          <el-option
            v-for="item in buildingOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="单元" prop="unitId">
        <el-select v-model="queryParams.unitId" placeholder="请选择单元" clearable>
          <el-option
            v-for="item in unitOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="房号" prop="roomNumber">
        <el-input
          v-model="queryParams.roomNumber"
          placeholder="请输入房号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="业主姓名" prop="ownerNames">
        <el-input
          v-model="queryParams.ownerNames"
          placeholder="请输入业主姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号" prop="ownerMobiles">
        <el-input
          v-model="queryParams.ownerMobiles"
          placeholder="请输入手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="身份证号码" prop="ownerIdNumbers">
        <el-input
          v-model="queryParams.ownerIdNumbers"
          placeholder="请输入身份证号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="建筑面积" prop="area">
        <el-input
          v-model="queryParams.area"
          placeholder="请输入建筑面积"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="房屋编号" prop="houseNumber">
        <el-input
          v-model="queryParams.houseNumber"
          placeholder="请输入房屋编号"
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
          v-hasPermi="['system:houses:add']"
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['system:houses:edit']"
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
          v-hasPermi="['system:houses:remove']"
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
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="housesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="房屋ID" align="center" prop="houseId" />
      <el-table-column label="小区" align="center" prop="districtName" />
      <el-table-column label="楼栋" align="center" prop="buildingName" />
      <el-table-column label="单元" align="center" prop="unitName" />
      <el-table-column label="房号" align="center" prop="roomNumber" />
      <el-table-column label="是否绑定" align="center" width="100">
        <template slot-scope="scope">
          <el-tooltip :content="scope.row.isBind === 1 ? '已绑定' : '未绑定'" placement="top">
            <i
              :class="scope.row.isBind === 1 ? 'el-icon-link success' : 'el-icon-connection danger'"
              :style="{ fontSize: '20px', color: scope.row.isBind === 1 ? '#67C23A' : '#F56C6C', transform: scope.row.isBind === 1 ? 'none' : 'rotateZ(45deg)' }"
            />
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="业主姓名" align="center" prop="ownerNames">
        <template slot-scope="scope">
          <el-tooltip placement="top">
            <div slot="content">
              <div v-for="(name, index) in scope.row.ownerNames.split(',')" :key="index">
                {{ name.trim() }}
              </div>
            </div>
            <span>{{ formatMultipleValues(scope.row.ownerNames) }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="手机号" align="center" prop="ownerMobiles">
        <template slot-scope="scope">
          <el-tooltip placement="top">
            <div slot="content">
              <div v-for="(mobile, index) in scope.row.ownerMobiles.split(',')" :key="index">
                {{ mobile.trim() }}
              </div>
            </div>
            <span>{{ formatMultipleValues(scope.row.ownerMobiles) }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="身份证号码" align="center" prop="ownerIdNumbers">
        <template slot-scope="scope">
          <el-tooltip placement="top">
            <div slot="content">
              <div v-for="(idNumber, index) in scope.row.ownerIdNumbers.split(',')" :key="index">
                {{ idNumber.trim() }}
              </div>
            </div>
            <span>{{ formatMultipleValues(scope.row.ownerIdNumbers) }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="建筑面积" align="center" prop="area" />
      <el-table-column label="物业类型(高层、洋房、别墅、联排、商铺)" align="center" prop="type" />
      <el-table-column label="房屋编号" align="center" prop="houseNumber" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['system:houses:edit']"
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            v-hasPermi="['system:houses:remove']"
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

    <!-- 添加或修改物业房屋对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="小区" prop="districtId">
          <el-select v-model="form.districtId" placeholder="请选择小区" @change="handleFormDistrictChange">
            <el-option
              v-for="item in districtOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="楼栋" prop="buildingId">
          <el-select v-model="form.buildingId" placeholder="请选择楼栋" @change="handleFormBuildingChange">
            <el-option
              v-for="item in buildingOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="单元" prop="unitId">
          <el-select v-model="form.unitId" placeholder="请选择单元">
            <el-option
              v-for="item in unitOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="房号" prop="roomNumber">
          <el-input v-model="form.roomNumber" placeholder="请输入房号" />
        </el-form-item>
        <el-form-item label="业主姓名" prop="ownerNames">
          <el-input v-model="form.ownerNames" placeholder="请输入业主姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="ownerMobiles">
          <el-input v-model="form.ownerMobiles" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="身份证号码" prop="ownerIdNumbers">
          <el-input v-model="form.ownerIdNumbers" placeholder="请输入身份证号码" />
        </el-form-item>
        <el-form-item label="建筑面积" prop="area">
          <el-input v-model="form.area" placeholder="请输入建筑面积" />
        </el-form-item>
        <el-form-item label="物业类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择物业类型">
            <el-option label="高层" value="高层" />
            <el-option label="洋房" value="洋房" />
            <el-option label="别墅" value="别墅" />
            <el-option label="联排" value="联排" />
            <el-option label="商铺" value="商铺" />
          </el-select>
        </el-form-item>
        <el-form-item label="房屋编号" prop="houseNumber">
          <el-input v-model="form.houseNumber" placeholder="请输入房屋编号" />
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
        :before-upload="beforeUpload"
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
import { listHouses, getHouses, delHouses, addHouses, updateHouses } from '@/api/buildings/houses'
import { listBuilding, addBuilding } from '@/api/buildings/building'
import { getToken } from '@/utils/auth'
import * as XLSX from 'xlsx'
import FileSaver from 'file-saver'

export default {
  name: 'Houses',
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
      // 物业房屋表格数据
      housesList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        districtId: null,
        buildingId: null,
        unitId: null,
        roomNumber: null,
        ownerNames: null,
        ownerMobiles: null,
        ownerIdNumbers: null,
        area: null,
        type: null,
        houseNumber: null,
        isBind: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        districtId: [
          { required: true, message: '请选择小区', trigger: 'change' }
        ],
        buildingId: [
          { required: true, message: '请选择楼栋', trigger: 'change' }
        ],
        unitId: [
          { required: true, message: '请选择单元', trigger: 'change' }
        ],
        roomNumber: [
          { required: true, message: '请输入房号', trigger: 'blur' }
        ],
        ownerNames: [
          { required: true, message: '请输入业主姓名', trigger: 'blur' }
        ],
        ownerMobiles: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}(,1[3-9]\d{9})*$/, message: '请输入正确的手机号格式（多个手机号用英文逗号分隔）', trigger: 'blur' }
        ],
        ownerIdNumbers: [
          { required: true, message: '请输入身份证号码', trigger: 'blur' },
          { pattern: /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx](,[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx])*$/, message: '请输入正确的身份证号码格式（多个身份证号用英文逗号分隔）', trigger: 'blur' }
        ],
        area: [
          { required: true, message: '请输入建筑面积', trigger: 'blur' },
          { pattern: /^\d+(\.\d+)?$/, message: '建筑面积必须为数字', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择物业类型', trigger: 'change' }
        ]
      },
      // 下拉选项
      districtOptions: [],
      buildingOptions: [],
      unitOptions: [],

      // 上传参数
      upload: {
        open: false,
        title: '导入数据',
        url: process.env.VUE_APP_BASE_API + '/system/houses/import',
        headers: { Authorization: 'Bearer ' + getToken() },
        isUploading: false
      },

      // Excel相关配置
      excel: {
        headers: ['小区', '楼栋', '单元', '房号', '业主姓名', '手机号', '身份证号码', '建筑面积', '物业类型', '房屋编号'],
        headerKey: ['districtName', 'buildingName', 'unitName', 'roomNumber', 'ownerNames', 'ownerMobiles', 'ownerIdNumbers', 'area', 'type', 'houseNumber']
      },

      // 物业类型选项
      propertyTypes: [
        { label: '高层', value: '高层' },
        { label: '洋房', value: '洋房' },
        { label: '别墅', value: '别墅' },
        { label: '联排', value: '联排' },
        { label: '商铺', value: '商铺' }
      ]
    }
  },
  created() {
    this.getList()
    this.loadDistrictOptions()
  },
  methods: {
    /** 查询物业房屋列表 */
    getList() {
      this.loading = true
      listHouses(this.queryParams).then(response => {
        // 获取所有小区数据
        listBuilding({ level: 1 }).then(districtRes => {
          const districts = districtRes.rows
          // 获取所有楼栋数据
          listBuilding({ level: 2 }).then(buildingRes => {
            const buildings = buildingRes.rows
            // 获取所有单元数据
            listBuilding({ level: 3 }).then(unitRes => {
              const units = unitRes.rows

              // 转换数据
              this.housesList = response.rows.map(item => {
                // 查找对应的小区、楼栋、单元
                const district = districts.find(d => d.id === item.districtId)
                const building = buildings.find(b => b.id === item.buildingId)
                const unit = units.find(u => u.id === item.unitId)

                return {
                  ...item,
                  districtName: district ? district.name : '',
                  buildingName: building ? building.name : '',
                  unitName: unit ? unit.name : ''
                }
              })

              this.total = response.total
              this.loading = false
            })
          })
        })
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
        houseId: null,
        districtId: null,
        buildingId: null,
        unitId: null,
        roomNumber: null,
        ownerNames: null,
        ownerMobiles: null,
        ownerIdNumbers: null,
        area: null,
        type: null,
        houseNumber: null
      }
      this.resetForm('form')
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
      this.ids = selection.map(item => item.houseId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加物业房屋'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const houseId = row.houseId || this.ids
      getHouses(houseId).then(response => {
        this.form = response.data
        // 加载关联数据
        if (this.form.districtId) {
          this.loadBuildingOptions(this.form.districtId)
        }
        if (this.form.buildingId) {
          this.loadUnitOptions(this.form.buildingId)
        }
        this.open = true
        this.title = '修改物业房屋'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(async valid => {
        if (valid) {
          try {
            // 检查同一单元下的房号是否已存在
            const existingHouses = await listHouses({
              districtId: this.form.districtId,
              buildingId: this.form.buildingId,
              unitId: this.form.unitId,
              roomNumber: this.form.roomNumber
            })

            // 检查是否存在相同房号（排除当前记录）
            const hasDuplicate = existingHouses.rows.some(house => {
              // 如果是修改操作，排除当前记录
              if (this.form.houseId && house.houseId === this.form.houseId) {
                return false
              }
              // 检查是否在同一单元下有相同房号
              return house.roomNumber === this.form.roomNumber &&
                     house.districtId === this.form.districtId &&
                     house.buildingId === this.form.buildingId &&
                     house.unitId === this.form.unitId
            })

            if (hasDuplicate) {
              this.$message.error('该单元下已存在相同房号')
              return
            }

            // 创建一个新对象来存储要提交的数据
            const submitData = {
              ...this.form,
              districtId: this.form.districtId,
              buildingId: this.form.buildingId,
              unitId: this.form.unitId
            }

            if (this.form.houseId != null) {
              await updateHouses(submitData)
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            } else {
              await addHouses(submitData)
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            }
          } catch (error) {
            this.$modal.msgError('操作失败：' + (error.message || '未知错误'))
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const houseIds = row.houseId || this.ids
      this.$modal.confirm('是否确认删除物业房屋编号为"' + houseIds + '"的数据项？').then(function() {
        return delHouses(houseIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 加载小区选项 */
    loadDistrictOptions() {
      listBuilding({ level: 1 }).then(response => {
        this.districtOptions = response.rows
      })
    },

    /** 加载楼栋选项 */
    loadBuildingOptions(districtId) {
      listBuilding({ level: 2, pid: districtId }).then(response => {
        this.buildingOptions = response.rows
      })
    },

    /** 加载单元选项 */
    loadUnitOptions(buildingId) {
      listBuilding({ level: 3, pid: buildingId }).then(response => {
        this.unitOptions = response.rows
      })
    },

    /** 处理小区选择变化 */
    handleDistrictChange(value) {
      this.queryParams.buildingId = null
      this.queryParams.unitId = null
      this.buildingOptions = []
      this.unitOptions = []
      if (value) {
        this.loadBuildingOptions(value)
      }
    },

    /** 处理楼栋选择变化 */
    handleBuildingChange(value) {
      this.queryParams.unitId = null
      this.unitOptions = []
      if (value) {
        this.loadUnitOptions(value)
      }
    },

    /** 处理表单小区选择变化 */
    handleFormDistrictChange(value) {
      this.form.buildingId = null
      this.form.unitId = null
      this.buildingOptions = []
      this.unitOptions = []
      if (value) {
        this.loadBuildingOptions(value)
      }
    },

    /** 处理表单楼栋选择变化 */
    handleFormBuildingChange(value) {
      this.form.unitId = null
      this.unitOptions = []
      if (value) {
        this.loadUnitOptions(value)
      }
    },

    /** 导入按钮操作 */
    handleImport() {
      this.upload.open = true
    },

    /** 格式化多值显示 */
    formatMultipleValues(value) {
      if (!value) return ''
      const values = value.split(',').map(v => v.trim())
      if (values.length <= 1) return value
      return values[0] + ` (共${values.length}个)`
    },

    /** 下载模板操作 */
    downloadTemplate() {
      // 创建工作簿
      const wb = XLSX.utils.book_new()

      // 创建模板数据
      const templateData = [
        this.excel.headers,
        ['示例小区', '1号楼', 'A单元', '101', '张三,李四', '13800138000,13800138001', '110101199001011234,110101199001011235', '120', '高层', 'H-101']
      ]

      // 创建工作表
      const ws = XLSX.utils.aoa_to_sheet(templateData)

      // 设置列宽
      ws['!cols'] = [
        { wch: 20 }, // 小区
        { wch: 15 }, // 楼栋
        { wch: 15 }, // 单元
        { wch: 10 }, // 房号
        { wch: 15 }, // 业主姓名
        { wch: 15 }, // 手机号
        { wch: 25 }, // 身份证号码
        { wch: 12 }, // 建筑面积
        { wch: 15 }, // 物业类型
        { wch: 15 }, // 房屋编号
        { wch: 10 } // 绑定状态
      ]

      // 将工作表添加到工作簿
      XLSX.utils.book_append_sheet(wb, ws, '房屋信息导入模板')

      // 生成Excel文件并下载
      const excelBuffer = XLSX.write(wb, { bookType: 'xlsx', type: 'array' })
      const blob = new Blob([excelBuffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
      FileSaver.saveAs(blob, '房屋信息导入模板.xlsx')
    },

    /** 导出按钮操作 */
    handleExport() {
      this.$confirm('是否确认导出所有数据项?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 获取所有数据，不使用分页
        const params = { ...this.queryParams, pageNum: 1, pageSize: 999999 }
        Promise.all([
          listHouses(params),
          listBuilding({ level: 1 }),
          listBuilding({ level: 2 }),
          listBuilding({ level: 3 })
        ]).then(([housesRes, districtRes, buildingRes, unitRes]) => {
          const districts = districtRes.rows
          const buildings = buildingRes.rows
          const units = unitRes.rows

          // 创建ID到名称的映射
          const districtMap = new Map(districts.map(d => [d.id, d.name]))
          const buildingMap = new Map(buildings.map(b => [b.id, b.name]))
          const unitMap = new Map(units.map(u => [u.id, u.name]))

          const exportData = []
          // 添加表头
          exportData.push(this.excel.headers)

          // 处理数据
          housesRes.rows.forEach(item => {
            exportData.push([
              districtMap.get(item.districtId) || '', // 小区名称
              buildingMap.get(item.buildingId) || '', // 楼栋名称
              unitMap.get(item.unitId) || '', // 单元名称
              item.roomNumber || '',
              item.ownerNames || '', // 支持多个业主
              item.ownerMobiles || '', // 支持多个手机号
              item.ownerIdNumbers || '', // 支持多个身份证
              item.area || '',
              item.type || '',
              item.houseNumber || '',
              item.isBind === 1 ? '已绑定' : '未绑定'
            ])
          })

          // 创建工作簿和工作表
          const wb = XLSX.utils.book_new()
          const ws = XLSX.utils.aoa_to_sheet(exportData)

          // 设置列宽
          ws['!cols'] = [
            { wch: 20 }, // 小区
            { wch: 15 }, // 楼栋
            { wch: 15 }, // 单元
            { wch: 10 }, // 房号
            { wch: 15 }, // 业主姓名
            { wch: 15 }, // 手机号
            { wch: 25 }, // 身份证号码
            { wch: 12 }, // 建筑面积
            { wch: 15 }, // 物业类型
            { wch: 15 }, // 房屋编号
            { wch: 10 } // 绑定状态
          ]

          // 将工作表添加到工作簿
          XLSX.utils.book_append_sheet(wb, ws, '房屋信息数据')

          // 生成Excel文件并下载
          const excelBuffer = XLSX.write(wb, { bookType: 'xlsx', type: 'array' })
          const blob = new Blob([excelBuffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          FileSaver.saveAs(blob, `房屋信息数据_${new Date().getTime()}.xlsx`)

          this.$modal.msgSuccess('导出成功')
        })
      })
    },

    /** 验证Excel数据格式 */
    validateExcelData(data) {
      if (!data || data.length === 0) return false

      for (const row of data) {
        // 检查必填字段是否存在且不为空
        if (!row || row.length < 4) return false

        // 检查必填字段：小区、楼栋、单元、房号
        const [districtName, buildingName, unitName, roomNumber] = row
        if (!districtName?.toString().trim() ||
            !buildingName?.toString().trim() ||
            !unitName?.toString().trim() ||
            !roomNumber?.toString().trim()) {
          return false
        }
      }
      return true
    },

    /** 处理导入数据 */
    async processImportData(data) {
      try {
        this.upload.isUploading = true

        // 创建一个通知来显示进度
        const notification = this.$notify({
          title: '导入进度',
          message: '正在导入数据，请稍候...',
          duration: 0,
          customClass: 'import-progress-notification',
          position: 'top-right',
          type: 'info',
          showClose: false
        })

        // 初始化缓存映射
        const cache = {
          districts: new Map(),
          buildings: new Map(),
          units: new Map()
        }

        // 预加载所有现有数据
        await this.preloadExistingData(cache)

        // 批量处理每一行数据
        const total = data.length
        let success = 0
        let failed = 0
        const errors = []

        for (let i = 0; i < data.length; i++) {
          try {
            // 更新进度通知
            notification.message = `正在导入第 ${i + 1}/${total} 条数据...`

            const row = data[i]
            // 确保所有字段都转换为字符串并去除空格
            const [
              districtName = '',
              buildingName = '',
              unitName = '',
              roomNumber = '',
              ownerNames = '',
              ownerMobiles = '',
              ownerIdNumbers = '',
              area = '',
              type = '',
              houseNumber = ''
            ] = row.map(item => (item ?? '').toString().trim())

            // 处理一行数据
            await this.processRow({
              cache,
              rowData: {
                districtName,
                buildingName,
                unitName,
                roomNumber,
                ownerNames,
                ownerMobiles,
                ownerIdNumbers,
                area,
                type,
                houseNumber
              }
            })
            success++

            // 更新进度百分比
            const percent = Math.round((i + 1) / total * 100)
            notification.message = `已完成 ${percent}% (${i + 1}/${total})`
          } catch (error) {
            failed++
            const rowData = data[i]
            errors.push(`第${i + 1}行: ${rowData[0] || ''}-${rowData[1] || ''}-${rowData[2] || ''}-${rowData[3] || ''}, 错误: ${error.message}`)
            console.error('处理行数据失败:', error)
            continue // 继续处理下一行
          }
        }

        // 关闭进度通知
        notification.close()

        // 完成导入，显示结果
        this.upload.isUploading = false
        if (failed === 0) {
          this.$notify.success({
            title: '导入完成',
            message: `成功导入 ${total} 条数据`,
            duration: 3000
          })
        } else {
          this.$notify.warning({
            title: '导入完成',
            message: `成功 ${success} 条，失败 ${failed} 条`,
            duration: 0,
            onClick: () => {
              // 点击通知时显示详细错误信息
              this.$alert(errors.join('\n'), '导入错误详情', {
                type: 'warning',
                customClass: 'import-error-dialog',
                dangerouslyUseHTMLString: true
              })
            }
          })
        }

        this.upload.open = false
        this.getList() // 刷新列表
      } catch (error) {
        this.upload.isUploading = false
        this.$notify.error({
          title: '导入失败',
          message: error.message || '未知错误',
          duration: 0
        })
        console.error('导入总体失败:', error)
      }
    },

    /** 预加载现有数据 */
    async preloadExistingData(cache) {
      const [districts, buildings, units] = await Promise.all([
        listBuilding({ level: 1 }),
        listBuilding({ level: 2 }),
        listBuilding({ level: 3 })
      ])

      // 初始化缓存，使用更精确的键值对
      districts.rows.forEach(d => cache.districts.set(d.name, d))
      buildings.rows.forEach(b => cache.buildings.set(`${b.pid}-${b.name}`, b))
      units.rows.forEach(u => cache.units.set(`${u.pid}-${u.name}`, u))

      return cache
    },

    /** 处理单行数据 */
    async processRow({ cache, rowData }) {
      try {
        console.log('开始处理行数据:', rowData)
        const {
          districtName,
          buildingName,
          unitName,
          roomNumber,
          ownerNames,
          ownerMobiles,
          ownerIdNumbers,
          area,
          type,
          houseNumber
        } = rowData

        // 1. 处理小区
        const district = await this.getOrCreateDistrict(cache, districtName)
        console.log('获取到小区:', district)

        // 2. 处理楼栋
        const building = await this.getOrCreateBuilding(cache, {
          buildingName,
          districtId: district.id
        })
        console.log('获取到楼栋:', building)

        // 3. 处理单元
        const unit = await this.getOrCreateUnit(cache, {
          unitName,
          buildingId: building.id
        })
        console.log('获取到单元:', unit)

        // 4. 验证房屋是否重复
        await this.validateHouse({
          districtId: district.id,
          buildingId: building.id,
          unitId: unit.id,
          roomNumber
        })

        // 5. 创建房屋
        const result = await this.createHouse({
          districtId: district.id,
          buildingId: building.id,
          unitId: unit.id,
          roomNumber,
          ownerNames,
          ownerMobiles,
          ownerIdNumbers,
          area,
          type,
          houseNumber
        })
        console.log('创建房屋结果:', result)
        return result
      } catch (error) {
        console.error('处理行数据失败:', error)
        throw error
      }
    },

    /** 获取或创建小区 */
    async getOrCreateDistrict(cache, districtName) {
      try {
        console.log('处理小区:', districtName)
        let district = cache.districts.get(districtName)
        if (!district) {
          // 查询是否存在
          const res = await listBuilding({ level: 1, name: districtName })
          console.log('查询小区结果:', res)
          if (res.rows && res.rows.length > 0) {
            district = res.rows[0]
          } else {
            // 创建新小区
            const addRes = await addBuilding({ name: districtName, level: 1 })
            console.log('创建小区结果:', addRes)
            if (addRes.code !== 200) {
              throw new Error(`创建小区失败: ${addRes.msg || '未知错误'}`)
            }
            // 创建成功后，重新查询获取完整的小区信息
            const queryRes = await listBuilding({ level: 1, name: districtName })
            if (!queryRes.rows || queryRes.rows.length === 0) {
              throw new Error(`无法获取新创建的小区信息: ${districtName}`)
            }
            district = queryRes.rows[0]
          }
          if (!district || !district.id) {
            throw new Error(`小区数据无效: ${JSON.stringify(district)}`)
          }
          cache.districts.set(districtName, district)
        }
        return district
      } catch (error) {
        console.error('处理小区失败:', error)
        throw new Error(`处理小区 ${districtName} 失败: ${error.message}`)
      }
    },

    /** 获取或创建楼栋 */
    async getOrCreateBuilding(cache, { buildingName, districtId }) {
      try {
        console.log('处理楼栋:', { buildingName, districtId })
        // 使用复合键查找楼栋
        const cacheKey = `${districtId}-${buildingName}`
        let building = cache.buildings.get(cacheKey)

        if (!building) {
          // 查询特定小区下的楼栋
          const res = await listBuilding({ level: 2, name: buildingName, pid: districtId })
          console.log('查询楼栋结果:', res)
          if (res.rows && res.rows.length > 0) {
            // 找到同名楼栋，检查是否属于当前小区
            const existingBuilding = res.rows.find(b => b.pid === districtId)
            if (existingBuilding) {
              building = existingBuilding
            }
          }

          if (!building) {
            // 创建新楼栋
            const addRes = await addBuilding({
              name: buildingName,
              level: 2,
              pid: districtId
            })
            console.log('创建楼栋结果:', addRes)
            if (addRes.code !== 200) {
              throw new Error(`创建楼栋失败: ${addRes.msg || '未知错误'}`)
            }
            // 创建成功后，重新查询获取完整的楼栋信息
            const queryRes = await listBuilding({ level: 2, name: buildingName, pid: districtId })
            console.log('重新查询楼栋结果:', queryRes)
            if (!queryRes.rows || queryRes.rows.length === 0) {
              throw new Error(`无法获取新创建的楼栋信息: ${buildingName}`)
            }
            building = queryRes.rows[0]
          }

          if (!building || !building.id) {
            throw new Error(`楼栋数据无效: ${JSON.stringify(building)}`)
          }
          // 更新缓存
          cache.buildings.set(cacheKey, building)
        }

        return building
      } catch (error) {
        console.error('处理楼栋失败:', error)
        throw new Error(`处理楼栋 ${buildingName} 失败: ${error.message}`)
      }
    },

    /** 获取或创建单元 */
    async getOrCreateUnit(cache, { unitName, buildingId }) {
      try {
        console.log('处理单元:', { unitName, buildingId })
        // 使用复合键查找单元
        const cacheKey = `${buildingId}-${unitName}`
        let unit = cache.units.get(cacheKey)

        if (!unit) {
          // 查询特定楼栋下的单元
          const res = await listBuilding({ level: 3, name: unitName, pid: buildingId })
          console.log('查询单元结果:', res)
          if (res.rows && res.rows.length > 0) {
            // 找到同名单元，检查是否属于当前楼栋
            const existingUnit = res.rows.find(u => u.pid === buildingId)
            if (existingUnit) {
              unit = existingUnit
            }
          }

          if (!unit) {
            // 创建新单元
            const addRes = await addBuilding({
              name: unitName,
              level: 3,
              pid: buildingId
            })
            console.log('创建单元结果:', addRes)
            if (addRes.code !== 200) {
              throw new Error(`创建单元失败: ${addRes.msg || '未知错误'}`)
            }
            // 创建成功后，重新查询获取完整的单元信息
            const queryRes = await listBuilding({ level: 3, name: unitName, pid: buildingId })
            console.log('重新查询单元结果:', queryRes)
            if (!queryRes.rows || queryRes.rows.length === 0) {
              throw new Error(`无法获取新创建的单元信息: ${unitName}`)
            }
            unit = queryRes.rows[0]
          }

          if (!unit || !unit.id) {
            throw new Error(`单元数据无效: ${JSON.stringify(unit)}`)
          }
          // 更新缓存
          cache.units.set(cacheKey, unit)
        }

        return unit
      } catch (error) {
        console.error('处理单元失败:', error)
        throw new Error(`处理单元 ${unitName} 失败: ${error.message}`)
      }
    },

    /** 验证房屋是否重复 */
    async validateHouse({ districtId, buildingId, unitId, roomNumber }) {
      const existingHouses = await listHouses({
        districtId,
        buildingId,
        unitId,
        roomNumber
      })

      const hasDuplicate = existingHouses.rows.some(house =>
        house.roomNumber === roomNumber &&
        house.districtId === districtId &&
        house.buildingId === buildingId &&
        house.unitId === unitId
      )

      if (hasDuplicate) {
        throw new Error('该单元下已存在相同房号')
      }
    },

    /** 创建房屋 */
    async createHouse(houseData) {
      const addHouseRes = await addHouses(houseData)
      if (addHouseRes.code !== 200) {
        throw new Error(`创建房屋失败: ${addHouseRes.msg || '未知错误'}`)
      }
      return addHouseRes.data
    },

    /** 文件上传前的处理 */
    beforeUpload(file) {
      const isExcel = /\.(xlsx|xls)$/i.test(file.name)
      if (!isExcel) {
        this.$message.error('请上传 Excel 文件！')
        return false
      }

      const reader = new FileReader()
      reader.onload = e => {
        try {
          const data = new Uint8Array(e.target.result)
          const workbook = XLSX.read(data, { type: 'array' })
          const worksheet = workbook.Sheets[workbook.SheetNames[0]]
          const jsonData = XLSX.utils.sheet_to_json(worksheet, {
            header: 1,
            raw: false, // 将所有值转换为字符串
            defval: '' // 空单元格的默认值
          })

          // 移除表头
          jsonData.shift()

          // 验证数据格式
          if (!this.validateExcelData(jsonData)) {
            this.$message.error('Excel 数据格式不正确，请下载模板填写！')
            return
          }

          // 处理数据导入
          this.processImportData(jsonData)
        } catch (error) {
          this.$message.error('文件解析失败，请确保文件格式正确！')
          console.error('File parsing error:', error)
        }
      }

      reader.readAsArrayBuffer(file)
      return false // 阻止自动上传
    },

    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true
    },

    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.isUploading = false
      this.$refs.upload.clearFiles()
      this.$alert(response.msg || '导入成功', '导入结果', { type: response.code === 200 ? 'success' : 'error' })
      this.getList()
    },

    // 提交上传文件
    submitFileForm() {
      this.upload.open = false // 立即关闭对话框
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
    }
  }
}
</script>

<style scoped>
.el-upload-dragger {
  background: #fff;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  box-sizing: border-box;
  width: 360px;
  height: 180px;
  text-align: center;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.el-upload-dragger:hover {
  border-color: #409EFF;
}

.el-upload__tip {
  margin-top: 10px;
  color: #606266;
}

.el-upload__tip .el-link {
  margin-right: 10px;
}

/* 调整下拉选择器和输入框统一宽度 */
.el-select,
.el-input {
  width: 100%;
}

/* 绑定状态图标样式 */
.el-icon-connection {
  display: inline-block;
}

.success {
  color: #67C23A;
}

.danger {
  color: #F56C6C;
}

.warning {
  color: #E6A23C;
}

/* 导入进度通知样式 */
:deep(.import-progress-notification) {
  min-width: 300px;
}

:deep(.import-error-dialog) {
  max-height: 500px;
  overflow-y: auto;
}

:deep(.import-error-dialog .el-message-box__content) {
  max-height: 400px;
  overflow-y: auto;
}
</style>
