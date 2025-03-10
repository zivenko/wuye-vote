const userApi = require('../../api/user');
const http = require('../../utils/request');

Component({
  properties: {
    buttonText: {
      type: String,
      value: '微信登录'
    },
    type: {
      type: String,
      value: 'primary' // primary, default
    },
    size: {
      type: String,
      value: 'default' // default, mini
    }
  },

  data: {
    loading: false
  },

  methods: {
    handleLogin() {
      if (this.data.loading) return;
      
      this.setData({ loading: true });
      
      // 调用微信登录接口获取 code
      wx.login({
        success: async (res) => {
          if (res.code) {
            try {
              // 发送 code 到后端
              const loginRes = await userApi.wxLogin(res.code);
              
              if (loginRes.code === 200) {
                // 保存 token
                http.setToken(loginRes.token);
                
                try {
                  // 获取用户信息
                  const userInfo = await this.getUserInfo();
                  
                  // 登录成功事件，传递用户信息
                  this.triggerEvent('loginsuccess', { userInfo });
                } catch (error) {
                  console.error('获取用户信息失败:', error);
                  // 使用默认用户信息
                  const defaultUserInfo = {
                    avatar: 'https://img.yzcdn.cn/vant/cat.jpeg',
                    nikeName: '微信用户'
                  };
                  this.triggerEvent('loginsuccess', { userInfo: defaultUserInfo });
                }
              } else {
                // 登录失败事件
                this.triggerEvent('loginfail', { error: loginRes.msg || '登录失败' });
              }
            } catch (error) {
              console.error('微信登录失败:', error);
              // 登录失败事件
              this.triggerEvent('loginfail', { error: error.message || '登录失败' });
            }
          } else {
            console.error('获取微信 code 失败:', res.errMsg);
            // 登录失败事件
            this.triggerEvent('loginfail', { error: '获取微信授权失败' });
          }
          
          this.setData({ loading: false });
        },
        fail: (err) => {
          console.error('wx.login 调用失败:', err);
          this.triggerEvent('loginfail', { error: '微信登录接口调用失败' });
          this.setData({ loading: false });
        }
      });
    },
    
    // 获取用户信息
    async getUserInfo() {
      try {
        const res = await userApi.getUserInfo();
        if (res.code === 200 && res.data) {
          // 更新全局用户信息
          const app = getApp();
          app.globalData.userInfo = res.data;
          app.globalData.isLoggedIn = true;
          
          return res.data;
        }
      } catch (error) {
        console.error('获取用户信息失败:', error);
        throw error;
      }
    }
  }
}); 