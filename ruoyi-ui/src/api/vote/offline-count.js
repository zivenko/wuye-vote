import request from '@/utils/request'

// 获取投票模板详情
export function getTemplateDetail(templateId) {
  return request({
    url: '/system/template/' + templateId,
    method: 'get'
  })
}

// 获取房屋投票状态列表
export function listHouseVoteStatus(query) {
  return request({
    url: '/system/house/vote-status',
    method: 'get',
    params: query
  })
}

// 提交线下唱票结果
export function submitOfflineVote(data) {
  return request({
    url: '/system/vote/offline-count',
    method: 'post',
    data: data
  })
}
