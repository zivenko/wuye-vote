// pages/my-vote/index.js
const voteApi = require('../../api/vote');

Page({
  pageLifetimes: {
    show() {
      if (typeof this.getTabBar === 'function' &&
        this.getTabBar()) {
          this.getTabBar().init(1)
      }
    }
  },
  
  /**
   * 页面的初始数据
   */
  data: {
    active: 0,
    voteList: [], // 投票列表
    filteredVoteList: [], // 过滤后的投票列表
    value: '' // 搜索关键词
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.loadVoteList();
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
        this.getTabBar().init(1)
    }
    this.loadVoteList();
  },

  // 加载投票列表
  async loadVoteList() {
    try {
      const res = await voteApi.getUserVoteTemplateList(1, this.data.value, this.data.active - 1);
      if (res.code === 200) {
        this.setData({
          voteList: res.data,
          filteredVoteList: res.data
        });
      }
    } catch (error) {
      console.error('获取投票列表失败:', error);
    }
  },

  // 过滤投票列表
  filterVoteList() {
    const { active, voteList, value } = this.data;
    let filteredList = voteList;
    
    // 根据标签过滤
    if (active === 1) { // 进行中
      filteredList = voteList.filter(item => item.status === 1);
    } else if (active === 2) { // 已结束
      filteredList = voteList.filter(item => item.status === 2);
    }

    // 根据搜索关键词过滤
    if (value) {
      filteredList = filteredList.filter(item => 
        item.title.toLowerCase().includes(value.toLowerCase()) ||
        item.description.toLowerCase().includes(value.toLowerCase())
      );
    }
    
    this.setData({
      filteredVoteList: filteredList
    });
  },

  // 标签切换
  onChange(e) {
    this.setData({
      active: e.detail.index
    });
    this.loadVoteList();
  },

  // 搜索
  onSearch(e) {
    this.setData({
      value: e.detail
    });
    this.loadVoteList();
  },

  // 取消搜索
  onCancel() {
    this.setData({
      value: ''
    });
    this.loadVoteList();
  },

  // 跳转到投票页面
  toVoting(e) {
    const templateId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/voting/index?id=${templateId}`
    });
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