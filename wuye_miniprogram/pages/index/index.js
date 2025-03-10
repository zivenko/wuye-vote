const voteApi = require('../../api/vote');

Page({

    data: {
        activeTab: 0,
        voteList: [], // 投票列表
        filteredVoteList: [], // 过滤后的投票列表
        value: '' // 搜索值
    },

    onLoad() {
        this.loadVoteList();
    },

    onShow: function() {
      console.log(this.getTabBar(), 'init')
      if (typeof this.getTabBar === 'function' &&
        this.getTabBar()) {
          this.getTabBar().init(0)
        }
      this.loadVoteList();
    },
    
    onChange: function(e) {
        this.setData({
            activeTab: e.detail.index
        });
        this.loadVoteList();
    },

    // 加载投票列表
    async loadVoteList() {
        try {
            const res = await voteApi.getUserVoteTemplateList(this.data.value, this.data.activeTab - 1);
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

    // 跳转到投票页面
    toVoting(e) {
        const templateId = e.currentTarget.dataset.id;
        wx.navigateTo({
            url: `/pages/voting/index?id=${templateId}`
        });
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
    }
});

