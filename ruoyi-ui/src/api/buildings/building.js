import request from '@/utils/request'

// 查询物业小区层级列表
export function listBuilding(query) {
  return request({
    url: '/system/building/list',
    method: 'get',
    params: query
  })
}

// 查询物业小区层级详细
export function getBuilding(id) {
  return request({
    url: '/system/building/' + id,
    method: 'get'
  })
}

// 新增物业小区层级
export function addBuilding(data) {
  return request({
    url: '/system/building',
    method: 'post',
    data: data
  })
}

// 修改物业小区层级
export function updateBuilding(data) {
  return request({
    url: '/system/building',
    method: 'put',
    data: data
  })
}

// 删除物业小区层级
export function delBuilding(id) {
  return request({
    url: '/system/building/' + id,
    method: 'delete'
  })
}
export function handleImport(file) {
  const formData = new FormData();
  formData.append('file', file);
  
  request.post('/system/building/import', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  }).then(response => {
    this.$modal.msgSuccess('导入成功');
    this.getList(); // 刷新列表
  }).catch(error => {
    this.$modal.msgError('导入失败：' + error.message);
  });
}
