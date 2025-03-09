import request from '@/utils/request'

// 查询房屋绑定审核列表
export function listCheck(query) {
  return request({
    url: '/system/check/list',
    method: 'get',
    params: query
  })
}

// 查询房屋绑定审核详细
export function getCheck(checkId) {
  return request({
    url: '/system/check/' + checkId,
    method: 'get'
  })
}

// 新增房屋绑定审核
export function addCheck(data) {
  return request({
    url: '/system/check',
    method: 'post',
    data: data
  })
}

// 修改房屋绑定审核
export function updateCheck(data) {
  return request({
    url: '/system/check',
    method: 'put',
    data: data
  })
}

// 删除房屋绑定审核
export function delCheck(checkId) {
  return request({
    url: '/system/check/' + checkId,
    method: 'delete'
  })
}
