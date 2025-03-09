const BASE_URL = 'http://localhost:8080'; // 开发环境地址，生产环境需要修改

// 封装请求方法
const request = (url, options = {}) => {
  return new Promise((resolve, reject) => {
    wx.request({
      url: `${BASE_URL}${url}`,
      ...options,
      header: {
        'Content-Type': 'application/json',
        'Authorization': wx.getStorageSync('token'), // 从本地存储获取token
        ...options.header
      },
      success: (res) => {
        if (res.statusCode === 200) {
          if (res.data.code === 200) {
            resolve(res.data);
          } else {
            wx.showToast({
              title: res.data.msg || '请求失败',
              icon: 'none'
            });
            reject(res.data);
          }
        } else if (res.statusCode === 401) {
          // token失效，需要重新登录
          wx.removeStorageSync('token');
          wx.navigateTo({
            url: '/pages/login/index'
          });
          reject(res.data);
        } else {
          wx.showToast({
            title: '服务器错误',
            icon: 'none'
          });
          reject(res.data);
        }
      },
      fail: (err) => {
        wx.showToast({
          title: '网络错误',
          icon: 'none'
        });
        reject(err);
      }
    });
  });
};

// 封装常用的请求方法
const http = {
  get: (url, data) => {
    return request(url, {
      method: 'GET',
      data
    });
  },
  post: (url, data) => {
    return request(url, {
      method: 'POST',
      data
    });
  },
  put: (url, data) => {
    return request(url, {
      method: 'PUT',
      data
    });
  },
  delete: (url, data) => {
    return request(url, {
      method: 'DELETE',
      data
    });
  }
};

module.exports = http; 