<template>
  <div class="app-container">
    <!-- 投票信息卡片 -->
    <el-card v-loading="loading" class="box-card">
      <div slot="header" class="clearfix">
        <span>投票信息</span>
      </div>
      <el-descriptions :column="4" border>
        <el-descriptions-item label="投票标题">{{ voteInfo.title }}</el-descriptions-item>
        <el-descriptions-item label="所属小区">{{ voteInfo.communityName }}</el-descriptions-item>
        <el-descriptions-item label="投票状态">
          <el-tag :type="getStatusType(voteInfo.status)">{{ getStatusLabel(voteInfo.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="投票规则">每人可投{{ voteInfo.voteTimes || 1 }}票</el-descriptions-item>
        <el-descriptions-item label="投票选项" :span="4">
          <div v-for="(choice, key) in voteChoices" :key="key">
            {{ key }}: {{ choice.content }}
          </div>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 搜索表单 -->
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" :inline="true" label-width="68px" class="mt20">
      <el-form-item label="楼栋" prop="buildingNo">
        <el-input
          v-model="queryParams.buildingNo"
          placeholder="请输入楼栋号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单元" prop="unitNo">
        <el-input
          v-model="queryParams.unitNo"
          placeholder="请输入单元号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="房号" prop="roomNo">
        <el-input
          v-model="queryParams.roomNo"
          placeholder="请输入房号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投票状态" prop="hasVoted">
        <el-select v-model="queryParams.hasVoted" placeholder="投票状态" clearable size="small">
          <el-option label="全部" :value="null" />
          <el-option label="已投票" :value="true" />
          <el-option label="未投票" :value="false" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 房屋列表 -->
    <el-table v-loading="loading" :data="houseList">
      <el-table-column label="楼栋" align="center" prop="buildingNo" />
      <el-table-column label="单元" align="center" prop="unitNo" />
      <el-table-column label="房号" align="center" prop="roomNo" />
      <el-table-column label="业主姓名" align="center" prop="ownerNames" />
      <el-table-column label="业主手机号" align="center" prop="ownerMobiles" width="120" />
      <el-table-column label="业主身份证" align="center" prop="ownerIdNumbers" width="180" />
      <el-table-column label="投票状态" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.hasVoted ? 'success' : 'info'">
            {{ scope.row.hasVoted ? '已投票' : '未投票' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template slot-scope="scope">
          <el-button
            v-if="!scope.row.hasVoted"
            size="mini"
            type="primary"
            @click="handleVote(scope.row)"
          >唱票</el-button>
          <el-button
            v-else
            size="mini"
            type="info"
            @click="handleViewVote(scope.row)"
          >查看</el-button>
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

    <!-- 投票对话框 -->
    <el-dialog :title="'唱票 - ' + currentHouse.buildingNo + '栋' + currentHouse.unitNo + '单元' + currentHouse.roomNo + '室'" :visible.sync="voteDialogVisible" width="600px" append-to-body>
      <div class="vote-info-box">
        <h3>{{ voteInfo.title }}</h3>
        <p class="vote-description">{{ voteInfo.description }}</p>
        <div class="vote-rule">投票规则：每人可投{{ voteInfo.voteTimes || 1 }}票</div>
      </div>
      <el-form ref="voteForm" :model="voteForm" :rules="voteRules" label-width="80px">
        <el-form-item label="投票选项" prop="choices">
          <el-checkbox-group
            v-model="voteForm.choices"
            :min="0"
            :max="voteInfo.voteTimes || 1"
          >
            <div v-for="(choice, key) in voteChoices" :key="key" class="vote-option">
              <el-checkbox :label="key">
                <div class="vote-choice-content">
                  <div class="vote-choice-title">{{ choice.title }}</div>
                  <div class="vote-choice-desc">{{ choice.content }}</div>
                </div>
              </el-checkbox>
            </div>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitVote">确 定</el-button>
        <el-button @click="voteDialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getTemplate } from '@/api/vote/template'
import { listHouseVoteStatus } from '@/api/vote/offline-count'
import request from '@/utils/request'

export default {
  name: 'OfflineCount',
  data() {
    const validateChoices = (rule, value, callback) => {
      if (!value || value.length === 0) {
        callback(new Error('请至少选择一个选项'))
      } else if (value.length > (this.voteInfo.voteTimes || 1)) {
        callback(new Error('最多只能选择 ' + (this.voteInfo.voteTimes || 1) + ' 个选项'))
      } else {
        callback()
      }
    }
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 房屋列表
      houseList: [],
      // 投票信息
      voteInfo: {},
      // 投票选项
      voteChoices: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        templateId: null,
        buildingNo: null,
        unitNo: null,
        roomNo: null,
        hasVoted: null
      },
      // 当前操作的房屋
      currentHouse: {},
      // 投票对话框显示状态
      voteDialogVisible: false,
      // 投票表单
      voteForm: {
        choices: null
      },
      // 投票表单校验规则
      voteRules: {
        choices: [
          { validator: validateChoices, trigger: 'change' }
        ]
      }
    }
  },
  created() {
    const templateId = this.$route.query.templateId
    if (templateId) {
      this.queryParams.templateId = templateId
      this.getVoteInfo()
      this.getList()
    }
  },
  methods: {
    /** 查询投票信息 */
    getVoteInfo() {
      getTemplate(this.queryParams.templateId).then(response => {
        this.voteInfo = response.data
        try {
          this.voteChoices = JSON.parse(this.voteInfo.choices || '{}')
        } catch (e) {
          this.voteChoices = {}
        }
        // 初始化表单
        this.voteForm.choices = []
      })
    },
    /** 查询房屋列表 */
    getList() {
      this.loading = true
      listHouseVoteStatus(this.queryParams).then(response => {
        this.houseList = response.rows
        this.total = response.total
        this.loading = false
      })
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
    /** 获取状态标签样式 */
    getStatusType(status) {
      if (status === 0) return 'info' // 未开始
      if (status === 1) return 'success' // 进行中
      if (status === 2) return 'danger' // 已结束
      return 'info'
    },
    /** 获取状态文字 */
    getStatusLabel(status) {
      if (status === 0) return '未开始'
      if (status === 1) return '进行中'
      if (status === 2) return '已结束'
      return '未知'
    },
    /** 唱票操作 */
    handleVote(row) {
      this.currentHouse = row
      // 重置表单
      this.voteForm.choices = []
      this.voteDialogVisible = true
    },
    /** 查看投票结果 */
    handleViewVote(row) {
      let choices = row.choices
      try {
        if (typeof choices === 'string') {
          const choiceArray = choices.split(',')
          choices = choiceArray.map(key => this.voteChoices[key]?.content || key).join('、')
        }
      } catch (e) {
        // 如果解析失败，直接显示原始值
      }
      this.$alert('投票选项：' + choices, '投票结果', {
        confirmButtonText: '确定'
      })
    },
    /** 提交投票 */
    submitVote() {
      this.$refs['voteForm'].validate(valid => {
        if (valid) {
          if (this.voteForm.choices.length === 0) {
            this.$message.warning('请至少选择一个选项')
            return
          }
          const data = {
            templateId: this.queryParams.templateId,
            houseId: this.currentHouse.houseId,
            choices: this.voteForm.choices.join(',')
          }
          request.post('/system/record/offline/submit', data).then(response => {
            this.$modal.msgSuccess('投票成功')
            this.voteDialogVisible = false
            this.getList()
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.mt20 {
  margin-top: 20px;
}
.vote-info-box {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}
.vote-info-box h3 {
  margin: 0 0 10px 0;
  font-size: 16px;
  color: #303133;
}
.vote-description {
  margin: 10px 0;
  color: #606266;
  line-height: 1.5;
}
.vote-rule {
  color: #409EFF;
  font-size: 14px;
  margin-top: 10px;
}
.vote-options-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.vote-option {
  padding: 15px;
  background-color: #fff;
  border: 1px solid #DCDFE6;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.vote-option:hover {
  border-color: #409EFF;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
}

.vote-option-selected {
  background-color: #ecf5ff;
  border-color: #409EFF;
}

.vote-choice-content {
  margin-left: 0;
}

.vote-choice-title {
  font-weight: bold;
  color: #303133;
}

.vote-choice-desc {
  margin-top: 4px;
  color: #606266;
  font-size: 13px;
}
</style>
