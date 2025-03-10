const BASE_URL = 'http://localhost:8080'; // 开发环境地址，生产环境需要修改

// 获取存储的 token
const getToken = () => {
  return wx.getStorageSync('token') || '';
};

// 设置 token
const setToken = (token) => {
  wx.setStorageSync('token', token);
};

// 清除 token
const clearToken = () => {
  wx.removeStorageSync('token');
};

// 请求拦截器
const request = (method, url, data = {}) => {
  return new Promise((resolve, reject) => {
    // 获取 token
    const token = getToken();
    
    // 请求头
    const header = {
      'Content-Type': 'application/json'
    };
    
    // 如果有 token，添加到请求头
    if (token) {
      header['Authorization'] = `Bearer ${token}`;
    }
    
    wx.request({
      url: `${BASE_URL}${url}`,
      method,
      data,
      header,
      success: (res) => {
        // 如果接口返回未登录状态码（如 401），可以跳转到登录页
        if (res.statusCode === 401) {
          clearToken();
          wx.navigateTo({
            url: '/pages/login/index'
          });
          reject(new Error('未登录或登录已过期'));
          return;
        }
        resolve(res.data);
      },
      fail: (err) => {
        reject(err);
      }
    });
  });
};

// HTTP 方法封装
const http = {
  get: (url, data) => request('GET', url, data),
  post: (url, data) => request('POST', url, data),
  put: (url, data) => request('PUT', url, data),
  delete: (url, data) => request('DELETE', url, data)
};

// 导出 http 作为默认导出，保持向后兼容
module.exports = http;

// 同时导出其他工具函数
http.getToken = getToken;
http.setToken = setToken;
http.clearToken = clearToken; 