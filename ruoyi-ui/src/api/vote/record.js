import request from '@/utils/request'

// 查询投票记录列表
export function listRecord(query) {
  return request({
    url: '/system/record/list',
    method: 'get',
    params: query
  })
}

// 查询投票记录详细
export function getRecord(voteId) {
  return request({
    url: '/system/record/' + voteId,
    method: 'get'
  })
}

// 新增投票记录
export function addRecord(data) {
  return request({
    url: '/system/record',
    method: 'post',
    data: data
  })
}

// 修改投票记录
export function updateRecord(data) {
  return request({
    url: '/system/record',
    method: 'put',
    data: data
  })
}

// 删除投票记录
export function delRecord(voteId) {
  return request({
    url: '/system/record/' + voteId,
    method: 'delete'
  })
}

// 根据姓名和手机号查找用户
export function searchAppletUser(data) {
  return request({
    url: '/system/record/searchUser',
    method: 'get',
    params: data
  })
}

// 根据房屋IDs获取可投票的模板列表
export function listTemplateByHouseIds(houseIds) {
  return request({
    url: '/system/record/templates/' + houseIds,
    method: 'get'
  })
}
