const userApi = require('../../api/user');

Page({
  data: {
    houses: []
  },

  onShow() {
    this.loadHousesList();
  },

  // 加载房屋列表
  async loadHousesList() {
    try {
      const res = await userApi.getHousesList();
      if (res.code === 200) {
        this.setData({
          houses: res.data || []
        });
      } else {
        wx.showToast({
          title: res.msg || '获取房屋列表失败',
          icon: 'none'
        });
      }
    } catch (error) {
      wx.showToast({
        title: '获取房屋列表失败',
        icon: 'none'
      });
    }
  },

  // 解绑房屋
  async unbindHouse(event) {
    const { houseId } = event.currentTarget.dataset;
    
    try {
      const res = await wx.showModal({
        title: '提示',
        content: '确定要解绑该房屋吗？',
        confirmText: '确定',
        cancelText: '取消'
      });

      if (res.confirm) {
        const result = await userApi.unbindHouse(houseId);
        if (result.code === 200) {
          wx.showToast({
            title: '解绑成功',
            icon: 'success'
          });
          this.loadHousesList(); // 重新加载列表
        } else {
          wx.showToast({
            title: result.msg || '解绑失败',
            icon: 'none'
          });
        }
      }
    } catch (error) {
      wx.showToast({
        title: '解绑失败',
        icon: 'none'
      });
    }
  },

  // 跳转到绑定房屋页面
  navigateToBindHouse() {
    wx.navigateTo({
      url: '/pages/bind-house/index'
    });
  }
});