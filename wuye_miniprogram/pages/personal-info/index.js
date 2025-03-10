// pages/personal-info/index.js
const userApi = require('../../api/user');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    form: {
      name: '',
      idNumber: '',
      mobile: ''
    },
    loading: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.getUserInfo();
  },

  /**
   * 获取用户信息
   */
  async getUserInfo() {
    try {
      const res = await userApi.getUserInfo();
      if (res.code === 200 && res.data) {
        this.setData({
          form: {
            name: res.data.name || '',
            idNumber: res.data.idNumber || '',
            mobile: res.data.mobile || ''
          }
        });
      }
    } catch (error) {
      console.error('获取用户信息失败:', error);
      wx.showToast({
        title: '获取用户信息失败',
        icon: 'none'
      });
    }
  },

  /**
   * 输入框内容变化时触发
   */
  onFieldChange(e) {
    const { field } = e.currentTarget.dataset;
    this.setData({
      [`form.${field}`]: e.detail
    });
  },

  /**
   * 提交表单
   */
  async onSubmit() {
    const { form } = this.data;
    
    // 表单验证
    if (!form.name.trim()) {
      wx.showToast({
        title: '请输入姓名',
        icon: 'none'
      });
      return;
    }
    if (!form.idNumber.trim()) {
      wx.showToast({
        title: '请输入身份证号',
        icon: 'none'
      });
      return;
    }
    if (!/^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/.test(form.idNumber)) {
      wx.showToast({
        title: '请输入正确的身份证号',
        icon: 'none'
      });
      return;
    }
    if (!form.mobile.trim()) {
      wx.showToast({
        title: '请输入手机号码',
        icon: 'none'
      });
      return;
    }
    if (!/^1[3-9]\d{9}$/.test(form.mobile)) {
      wx.showToast({
        title: '请输入正确的手机号码',
        icon: 'none'
      });
      return;
    }

    this.setData({ loading: true });

    try {
      const res = await userApi.updateUserInfo({
        name: form.name,
        idNumber: form.idNumber,
        mobile: form.mobile
      });

      if (res.code === 200) {
        wx.showToast({
          title: '保存成功',
          icon: 'success'
        });
      } else {
        throw new Error(res.msg || '保存失败');
      }
    } catch (error) {
      console.error('保存用户信息失败:', error);
      wx.showToast({
        title: error.message || '保存失败',
        icon: 'none'
      });
    } finally {
      this.setData({ loading: false });
    }
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

  }
})