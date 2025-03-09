import request from '@/utils/request'

// 查询物业房屋列表
export function listHouses(query) {
  return request({
    url: '/system/houses/list',
    method: 'get',
    params: query
  })
}

// 查询物业房屋详细
export function getHouses(houseId) {
  return request({
    url: '/system/houses/' + houseId,
    method: 'get'
  })
}

// 新增物业房屋
export function addHouses(data) {
  return request({
    url: '/system/houses',
    method: 'post',
    data: data
  })
}

// 修改物业房屋
export function updateHouses(data) {
  return request({
    url: '/system/houses',
    method: 'put',
    data: data
  })
}

// 删除物业房屋
export function delHouses(houseId) {
  return request({
    url: '/system/houses/' + houseId,
    method: 'delete'
  })
}
