// pages/my-center/index.js
const userApi = require('../../api/user');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    isLoggedIn: false,
    userInfo: null,
    checkUserInfoComplete() {
      const userInfo = this.data.userInfo;
      if (!userInfo) return false;
      
      // 使用严格的空值检查，确保字段不为null且不为空字符串
      return userInfo.name !== null && 
             userInfo.name !== undefined && 
             userInfo.name !== '' &&
             userInfo.idNumber !== null && 
             userInfo.idNumber !== undefined && 
             userInfo.idNumber !== '' &&
             userInfo.mobile !== null && 
             userInfo.mobile !== undefined && 
             userInfo.mobile !== '';
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.checkLoginStatus();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    if (typeof this.getTabBar === 'function' &&
      this.getTabBar()) {
        this.getTabBar().init(2)
    }
    
    // 每次显示页面时检查登录状态
    this.checkLoginStatus();
  },

  /**
   * 检查登录状态
   */
  async checkLoginStatus() {
    const app = getApp();
    
    // 如果已经登录，直接使用全局状态
    if (app.globalData.isLoggedIn && app.globalData.userInfo) {
      // 处理头像URL
      const userInfo = {
        ...app.globalData.userInfo,
        avatar: app.globalData.userInfo.avatar ? await userApi.getAvatarUrl(app.globalData.userInfo.avatar) : 'https://img.yzcdn.cn/vant/cat.jpeg',
        avatarUrl: app.globalData.userInfo.avatar ? await userApi.getAvatarUrl(app.globalData.userInfo.avatar) : 'https://img.yzcdn.cn/vant/cat.jpeg'
      };
      
      this.setData({
        isLoggedIn: true,
        userInfo: userInfo
      });
      return;
    }

    // 如果没有登录状态，尝试获取用户信息
    try {
      const res = await userApi.getUserInfo();
      if (res.code === 200 && res.data) {
        // 处理头像URL
        const userInfo = {
          ...res.data,
          avatar: res.data.avatar ? await userApi.getAvatarUrl(res.data.avatar) : 'https://img.yzcdn.cn/vant/cat.jpeg',
          avatarUrl: res.data.avatar ? await userApi.getAvatarUrl(res.data.avatar) : 'https://img.yzcdn.cn/vant/cat.jpeg'
        };
        
        // 更新全局状态
        app.globalData.userInfo = userInfo;
        app.globalData.isLoggedIn = true;
        
        // 更新页面状态
        this.setData({
          isLoggedIn: true,
          userInfo: userInfo
        });
      } else {
        // 获取用户信息失败，清除登录状态
        this.setData({
          isLoggedIn: false,
          userInfo: null
        });
        app.globalData.isLoggedIn = false;
        app.globalData.userInfo = null;
        wx.removeStorageSync('token');
      }
    } catch (error) {
      console.error('获取用户信息失败:', error);
      // 发生错误时清除登录状态
      this.setData({
        isLoggedIn: false,
        userInfo: null
      });
      app.globalData.isLoggedIn = false;
      app.globalData.userInfo = null;
      wx.removeStorageSync('token');
    }
  },

  /**
   * 登录成功回调
   */
  async handleLoginSuccess(e) {
    console.log('[页面] 登录成功事件触发，接收到的数据:', e);
    
    // 从事件中获取用户信息
    const userInfo = e.detail && e.detail.userInfo ? e.detail.userInfo : {};
    console.log('[页面] 解析出的用户信息:', userInfo);
    
    // 确保有头像和昵称
    if (!userInfo.avatar) {
      userInfo.avatar = 'https://img.yzcdn.cn/vant/cat.jpeg';
    }
    
    if (!userInfo.nikeName) {
      userInfo.nikeName = '微信用户';
    }

    // 处理头像URL
    try {
      if (userInfo.avatar && userInfo.avatar !== 'https://img.yzcdn.cn/vant/cat.jpeg') {
        userInfo.avatar = await userApi.getAvatarUrl(userInfo.avatar);
        userInfo.avatarUrl = userInfo.avatar;
      } else {
        userInfo.avatarUrl = userInfo.avatar;
      }
    } catch (error) {
      console.error('[页面] 处理头像URL失败:', error);
      userInfo.avatar = 'https://img.yzcdn.cn/vant/cat.jpeg';
      userInfo.avatarUrl = 'https://img.yzcdn.cn/vant/cat.jpeg';
    }

    // 添加别名以兼容之前的代码
    userInfo.nickname = userInfo.nikeName;
    
    // 更新全局状态
    const app = getApp();
    app.globalData.isLoggedIn = true;
    app.globalData.userInfo = userInfo;
    
    // 更新页面状态
    this.setData({
      isLoggedIn: true,
      userInfo: userInfo
    });
    
    console.log('[页面] 更新后的页面数据:', this.data);
    console.log('[页面] 更新后的全局数据:', app.globalData);
    
    wx.showToast({
      title: '登录成功',
      icon: 'success'
    });
  },

  /**
   * 登录失败回调
   */
  handleLoginFail(e) {
    wx.showToast({
      title: e.detail.error || '登录失败',
      icon: 'none'
    });
  },

  /**
   * 点击需要登录的项目时处理
   */
  handleLogin(e) {
    if (!this.data.isLoggedIn) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return;
    }

    // 获取点击的cell的title
    const title = e.currentTarget.dataset.title;
    
    // 如果是"我的房屋"，需要检查用户信息是否完善
    if (title === '我的房屋') {
      if (!this.checkUserInfoComplete()) {
        wx.showModal({
          title: '提示',
          content: '请先完善个人信息（姓名、身份证号、手机号）',
          confirmText: '去完善',
          success: (res) => {
            if (res.confirm) {
              wx.navigateTo({
                url: '/pages/personal-info/index'
              });
            }
          }
        });
        return;
      }
    }
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },

  /**
   * 点击头像更换头像
   */
  handleChangeAvatar() {
    if (!this.data.isLoggedIn) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return;
    }

    // 保存当前头像URL，用于失败时恢复
    const currentAvatar = this.data.userInfo.avatar;
    const currentAvatarUrl = this.data.userInfo.avatarUrl;

    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: async (res) => {
        const tempFilePath = res.tempFilePaths[0];
        
        // 显示上传中提示
        wx.showLoading({
          title: '上传中...',
        });

        try {
          // 使用API上传头像
          const result = await userApi.uploadAvatar(tempFilePath);
          
          if (result.code === 200 && result.data) {
            // 获取头像URL，传入图片路径
            const avatarUrl = await userApi.getAvatarUrl(result.data);
            
            // 更新用户信息
            const userInfo = {
              ...this.data.userInfo,
              avatar: avatarUrl,
              avatarUrl: avatarUrl
            };

            // 更新全局状态
            const app = getApp();
            app.globalData.userInfo = userInfo;

            // 更新页面状态
            this.setData({
              userInfo: userInfo
            });

            wx.showToast({
              title: '头像更新成功',
              icon: 'success'
            });
          } else {
            throw new Error(result.msg || '上传失败');
          }
        } catch (error) {
          console.error('上传头像失败:', error);
          // 恢复原来的头像
          const userInfo = {
            ...this.data.userInfo,
            avatar: currentAvatar,
            avatarUrl: currentAvatarUrl
          };
          this.setData({ userInfo });
          
          wx.showToast({
            title: error.message || '上传失败',
            icon: 'none',
            duration: 2000
          });
        } finally {
          wx.hideLoading();
        }
      },
      fail: (err) => {
        console.error('选择图片失败:', err);
        // 恢复原来的头像
        const userInfo = {
          ...this.data.userInfo,
          avatar: currentAvatar,
          avatarUrl: currentAvatarUrl
        };
        this.setData({ userInfo });
        
        wx.showToast({
          title: '选择图片失败',
          icon: 'none'
        });
      }
    });
  },

  /**
   * 处理"我的房屋"点击事件
   */
  async handleHouseClick() {
    // 检查是否登录
    if (!this.data.isLoggedIn) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return;
    }

    try {
      // 调用接口获取最新的用户信息
      const res = await userApi.getUserInfo();
      if (res.code !== 200) {
        throw new Error(res.msg || '获取用户信息失败');
      }

      const userInfo = res.data;
      // 检查必要信息是否完善
      if (!userInfo || !userInfo.name || !userInfo.idNumber || !userInfo.mobile) {
        wx.showModal({
          title: '提示',
          content: '请先完善个人信息（姓名、身份证号、手机号）',
          confirmText: '去完善',
          success: (res) => {
            if (res.confirm) {
              wx.navigateTo({
                url: '/pages/personal-info/index'
              });
            }
          }
        });
        return;
      }

      // 如果信息完善，跳转到我的房屋页面
      wx.navigateTo({
        url: '/pages/my-house/index'
      });
    } catch (error) {
      console.error('验证用户信息失败:', error);
      wx.showToast({
        title: error.message || '系统错误',
        icon: 'none'
      });
    }
  },
})