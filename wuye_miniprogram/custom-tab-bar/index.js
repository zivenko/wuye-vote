Page({
  data: {
    active: 0,
    list: [{
      pagePath: "/pages/index/index",
      icon: "home-o",
      text: "首页"
      }, 
      {
        pagePath: "/pages/my-vote/index",
        icon: "passed",
        text: "我的投票"
      },
      {
        pagePath: "/pages/my-center/index",
        icon: "contact-o",
        text: "个人中心"
      }
    ]
  },
  onChange(e) {
    const idx = e.detail

    const { pagePath } = this.data.list[idx]
    wx.switchTab({url: pagePath})
    
  },
  init(idx) {
    console.log(idx, 'init')
    this.setData({
      active: idx
    })
  }
});
