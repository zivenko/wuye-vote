const voteApi = require('../../api/vote');

Page({
  data: {
    templateId: null,
    template: null,
    options: [],
    selectedOption: [],
    isLoggedIn: false,
    hasVoted: false,
    remainingChoices: 0
  },

  // 添加判断选项是否选中的方法
  isOptionSelected(optionLabel) {
    return this.data.selectedOption && this.data.selectedOption.indexOf(optionLabel) !== -1;
  },

  onLoad(options) {
    // 检查登录状态
    const app = getApp();
    const isLoggedIn = app.globalData.isLoggedIn;
    
    this.setData({ isLoggedIn });

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

  // 登录成功处理
  handleLoginSuccess() {
    this.setData({ isLoggedIn: true });
    wx.showToast({
      title: '登录成功',
      icon: 'success'
    });
  },

  // 登录失败处理
  handleLoginFail(e) {
    wx.showToast({
      title: e.detail.error || '登录失败',
      icon: 'none'
    });
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
        const { template, userChoice } = res.data;
        
        // 解析选项数据
        try {
          const choicesObj = JSON.parse(template.choices || '{}');
          // 将对象格式转换为数组格式，并按字母顺序排序
          const options = Object.entries(choicesObj)
            .map(([key, value]) => ({
              label: key,
              value: key,
              content: value.content,
              isSelected: false
            }))
            .sort((a, b) => a.label.localeCompare(b.label));

          // 处理用户已选择的选项，确保是数组格式
          let selectedChoices = [];
          if (userChoice) {
            // 如果是逗号分隔的字符串，分割成数组
            selectedChoices = userChoice.includes(',') ? userChoice.split(',') : [userChoice];
            // 标记选中的选项
            options.forEach(option => {
              option.isSelected = selectedChoices.indexOf(option.label) !== -1;
            });
          }

          this.setData({
            template: {
              ...template,
              voteTimes: parseInt(template.voteTimes) || 1,
              // 格式化日期显示
              endTime: template.endTime ? template.endTime.split(' ')[0] : '未设置'
            },
            options,
            hasVoted: !!userChoice,
            selectedOption: selectedChoices,
            remainingChoices: (parseInt(template.voteTimes) || 1) - selectedChoices.length
          });

          console.log('Selected options:', selectedChoices); // 调试日志
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
    const selectedValues = e.detail.value;
    const maxChoices = this.data.template.voteTimes || 1;
    
    if (selectedValues.length > maxChoices) {
      wx.showToast({
        title: `最多选择${maxChoices}项`,
        icon: 'none'
      });
      return;
    }

    const options = this.data.options.map(option => ({
      ...option,
      isSelected: selectedValues.indexOf(option.label) !== -1
    }));

    this.setData({
      options,
      selectedOption: selectedValues,
      remainingChoices: maxChoices - selectedValues.length
    });
  },

  async handleSubmit() {
    if (!this.data.selectedOption.length) {
      wx.showToast({
        title: '请至少选择一个选项',
        icon: 'none'
      });
      return;
    }

    if (this.data.selectedOption.length > (this.data.template.voteTimes || 1)) {
      wx.showToast({
        title: `最多选择${this.data.template.voteTimes || 1}项`,
        icon: 'none'
      });
      return;
    }

    wx.showLoading({
      title: '提交中...'
    });

    try {
      const app = getApp();
      const res = await voteApi.submitVote({
        templateId: this.data.templateId,
        appletId: app.globalData.userInfo.appletId,
        choices: this.data.selectedOption.join(',')
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