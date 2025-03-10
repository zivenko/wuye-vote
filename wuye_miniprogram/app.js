// app.js
const userApi = require('./api/user');
const http = require('./utils/request');

App({
  globalData: {
    userInfo: null,
    isLoggedIn: false,
    baseUrl: 'http://localhost:8080'
  },

  onLaunch() {
    // 检查本地是否有token
    const token = wx.getStorageSync('token');
    if (token) {
      // 如果有token，设置token并获取用户信息
      http.setToken(token);
      this.getUserInfo().then(success => {
        if (!success) {
          // 如果获取用户信息失败，说明token可能过期，清除token
          wx.removeStorageSync('token');
          this.globalData.isLoggedIn = false;
          this.globalData.userInfo = null;
        }
      });
    }
  },

  // 自动登录
  autoLogin() {
    return new Promise((resolve, reject) => {
      wx.login({
        success: async (res) => {
          if (res.code) {
            try {
              const loginRes = await userApi.wxLogin(res.code);
              if (loginRes.code === 200 && loginRes.token) {
                // 保存 token
                http.setToken(loginRes.token);
                wx.setStorageSync('token', loginRes.token);
                // 获取用户信息
                const success = await this.getUserInfo();
                resolve(success);
              } else {
                reject(new Error(loginRes.msg || '登录失败'));
              }
            } catch (error) {
              console.error('微信登录失败:', error);
              reject(error);
            }
          } else {
            console.error('微信登录失败:', res.errMsg);
            reject(new Error(res.errMsg));
          }
        },
        fail: (err) => {
          console.error('wx.login 调用失败:', err);
          reject(err);
        }
      });
    });
  },

  // 获取用户信息
  async getUserInfo() {
    try {
      const res = await userApi.getUserInfo();
      if (res.code === 200 && res.data) {
        this.globalData.userInfo = res.data;
        this.globalData.isLoggedIn = true;
        return true;
      }
      return false;
    } catch (error) {
      console.error('获取用户信息失败:', error);
      return false;
    }
  },

  // 检查登录状态
  checkLoginStatus() {
    return this.globalData.isLoggedIn;
  }
})
