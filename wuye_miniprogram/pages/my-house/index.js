
Page({
  data: {
    houseList: [
      {
        id: 1,
        title: '武汉数字运营中心项目酒店专用A1栋1单元101',
        time: '2024/01/23 14:03',
        status: '审核中',
        image: 'https://pic2.zhimg.com/v2-4905ebb2e010c1fe2780fb1cbae3ceb6_r.jpg', // 替换为实际的图片路径
        size: '100',
        description: '产权证明'
      },
      // 可以添加更多房屋数据
    ]
  },

  onHouseClick(e) {
    const id = e.currentTarget.dataset.id;
    // 处理点击事件，比如跳转到详情页
    wx.navigateTo({
      url: `/pages/house-detail/index?id=${id}`
    });
  },
  onAddHouse() {
    // 处理添加房屋的点击事件
    wx.navigateTo({
      url: '/pages/add-house/index'
    });
  }
});