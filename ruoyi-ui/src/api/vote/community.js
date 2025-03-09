import request from '@/utils/request'

// 查询小区列表
export function listCommunity(query) {
  return request({
    url: '/system/community/list',
    method: 'get',
    params: query
  })
}

// 查询小区详细
export function getCommunity(communityId) {
  return request({
    url: '/system/community/' + communityId,
    method: 'get'
  })
}
