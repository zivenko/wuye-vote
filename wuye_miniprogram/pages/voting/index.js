const voteApi = require('../../api/vote');

Page({
  data: {
    templateId: null,
    template: null,
    options: [],
    selectedOption: ''
  },

  onLoad(options) {
    if (options.id) {
      this.setData({
        templateId: options.id
      });
      this.loadVoteTemplate();
    } else {
      wx.showToast({
        title: '参数错误',
        icon: 'error'
      });
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    }
  },

  // 加载投票模板数据
  async loadVoteTemplate() {
    wx.showLoading({
      title: '加载中...'
    });

    try {
      const res = await voteApi.getVoteTemplateDetail(this.data.templateId);
      wx.hideLoading();
      
      if (res.code === 200 && res.data) {
        const template = res.data;
        
        // 解析选项数据
        let choicesObj = {};
        try {
          choicesObj = JSON.parse(template.choices || '{}');
          // 将对象格式转换为数组格式
          const options = Object.entries(choicesObj).map(([key, value]) => ({
            label: key,
            value: key,
            content: value.content
          }));

          this.setData({
            template: {
              ...template,
              // 格式化日期显示
              endTime: template.endTime ? template.endTime.split(' ')[0] : '未设置'
            },
            options
          });
        } catch (e) {
          console.error('解析选项数据失败:', e);
          wx.showToast({
            title: '选项数据格式错误',
            icon: 'error'
          });
        }
      } else {
        throw new Error(res.msg || '获取数据失败');
      }
    } catch (error) {
      wx.hideLoading();
      console.error('获取投票模板详情失败:', error);
      wx.showToast({
        title: error.message || '加载失败',
        icon: 'error'
      });
    }
  },

  handleOptionChange(e) {
    this.setData({
      selectedOption: e.detail.value
    });
  },

  async handleSubmit() {
    if (!this.data.selectedOption) {
      wx.showToast({
        title: '请选择一个选项',
        icon: 'none'
      });
      return;
    }

    // 检查用户信息
    const app = getApp();
    if (!app.globalData || !app.globalData.userInfo || !app.globalData.userInfo.appletId) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      // 可以在这里添加跳转到登录页面的逻辑
      return;
    }

    wx.showLoading({
      title: '提交中...'
    });

    try {
      const res = await voteApi.submitVote({
        templateId: this.data.templateId,
        appletId: app.globalData.userInfo.appletId,
        choices: this.data.selectedOption
      });

      wx.hideLoading();

      if (res.code === 200) {
        wx.showToast({
          title: '投票成功',
          icon: 'success',
          duration: 2000,
          success: () => {
            setTimeout(() => {
              wx.navigateBack();
            }, 2000);
          }
        });
      } else {
        throw new Error(res.msg || '投票失败');
      }
    } catch (error) {
      wx.hideLoading();
      wx.showToast({
        title: error.message || '投票失败',
        icon: 'error'
      });
      console.error('提交投票失败:', error);
    }
  }
});