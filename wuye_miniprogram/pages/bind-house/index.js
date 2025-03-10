const userApi = require('../../api/user');

Page({
  data: {
    fileList: [], // 房屋证明图片列表
    ownerName: '', // 业主姓名
    ownerIdNumber: '', // 业主身份证号
    loading: false, // 提交按钮loading状态

    // 房屋选择相关
    districtId: '', // 小区ID
    districtName: '', // 小区名称
    buildingId: '', // 楼栋ID
    buildingName: '', // 楼栋名称
    unitId: '', // 单元ID
    unitName: '', // 单元名称
    houseId: '', // 房屋ID
    roomNumber: '', // 房间号

    // 选择器相关
    showDistrictPicker: false,
    showBuildingPicker: false,
    showUnitPicker: false,
    showRoomPicker: false,
    districtList: [],
    buildingList: [],
    unitList: [],
    roomList: [],
    columns: []
  },

  onLoad() {
    this.loadDistrictList();
  },

  // 加载小区列表
  async loadDistrictList() {
    try {
      const res = await userApi.getDistrictList();
      console.log('小区列表返回数据:', res);
      if (res.code === 200 && res.rows) {
        const districts = res.rows.map(item => ({
          text: item.name,
          value: item.id,
          ...item
        }));
        this.setData({ 
          districtList: districts,
          columns: districts
        });
      } else {
        wx.showToast({
          title: res.msg || '获取小区列表失败',
          icon: 'none'
        });
      }
    } catch (error) {
      console.error('获取小区列表失败:', error);
      wx.showToast({
        title: '获取小区列表失败',
        icon: 'none'
      });
    }
  },

  // 加载楼栋列表
  async loadBuildingList(districtId) {
    try {
      const res = await userApi.getBuildingList(districtId);
      console.log('楼栋列表返回数据:', res);
      if (res.code === 200 && res.rows) {
        const buildings = res.rows.map(item => ({
          text: item.name,
          value: item.id,
          ...item
        }));
        this.setData({ 
          buildingList: buildings,
          columns: buildings
        });
      } else {
        wx.showToast({
          title: res.msg || '获取楼栋列表失败',
          icon: 'none'
        });
      }
    } catch (error) {
      console.error('获取楼栋列表失败:', error);
      wx.showToast({
        title: '获取楼栋列表失败',
        icon: 'none'
      });
    }
  },

  // 加载单元列表
  async loadUnitList(buildingId) {
    try {
      const res = await userApi.getUnitList(buildingId);
      console.log('单元列表返回数据:', res);
      if (res.code === 200 && res.rows) {
        const units = res.rows.map(item => ({
          text: item.name,
          value: item.id,
          ...item
        }));
        this.setData({ 
          unitList: units,
          columns: units
        });
      } else {
        wx.showToast({
          title: res.msg || '获取单元列表失败',
          icon: 'none'
        });
      }
    } catch (error) {
      console.error('获取单元列表失败:', error);
      wx.showToast({
        title: '获取单元列表失败',
        icon: 'none'
      });
    }
  },

  // 加载房间列表
  async loadRoomList(unitId) {
    try {
      const res = await userApi.getRoomList(unitId);
      console.log('房间列表返回数据:', res);
      if (res.code === 200 && res.rows) {
        const rooms = res.rows.map(item => ({
          text: item.roomNumber,
          value: item.houseId,
          ...item
        }));
        this.setData({ 
          roomList: rooms,
          columns: rooms
        });
      } else {
        wx.showToast({
          title: res.msg || '获取房间列表失败',
          icon: 'none'
        });
      }
    } catch (error) {
      console.error('获取房间列表失败:', error);
      wx.showToast({
        title: '获取房间列表失败',
        icon: 'none'
      });
    }
  },

  // 显示小区选择器
  showDistrictPicker() {
    if (this.data.districtList.length === 0) {
      this.loadDistrictList();
    }
    this.setData({ showDistrictPicker: true });
  },

  // 显示楼栋选择器
  showBuildingPicker() {
    if (!this.data.districtId) {
      wx.showToast({
        title: '请先选择小区',
        icon: 'none'
      });
      return;
    }
    if (this.data.buildingList.length === 0) {
      this.loadBuildingList(this.data.districtId);
    }
    this.setData({ showBuildingPicker: true });
  },

  // 显示单元选择器
  showUnitPicker() {
    if (!this.data.buildingId) {
      wx.showToast({
        title: '请先选择楼栋',
        icon: 'none'
      });
      return;
    }
    if (this.data.unitList.length === 0) {
      this.loadUnitList(this.data.buildingId);
    }
    this.setData({ showUnitPicker: true });
  },

  // 显示房间选择器
  showRoomPicker() {
    if (!this.data.unitId) {
      wx.showToast({
        title: '请先选择单元',
        icon: 'none'
      });
      return;
    }
    if (this.data.roomList.length === 0) {
      this.loadRoomList(this.data.unitId);
    }
    this.setData({ showRoomPicker: true });
  },

  // 小区选择器确认
  onDistrictPickerConfirm(event) {
    const { value, text } = event.detail;
    this.setData({
      districtId: value,
      districtName: text,
      buildingId: '',
      buildingName: '',
      unitId: '',
      unitName: '',
      houseId: '',
      roomNumber: '',
      showDistrictPicker: false
    });
    this.loadBuildingList(value);
  },

  // 楼栋选择器确认
  onBuildingPickerConfirm(event) {
    const { value, text } = event.detail;
    this.setData({
      buildingId: value,
      buildingName: text,
      unitId: '',
      unitName: '',
      houseId: '',
      roomNumber: '',
      showBuildingPicker: false
    });
    this.loadUnitList(value);
  },

  // 单元选择器确认
  onUnitPickerConfirm(event) {
    const { value, text } = event.detail;
    this.setData({
      unitId: value,
      unitName: text,
      houseId: '',
      roomNumber: '',
      showUnitPicker: false
    });
    this.loadRoomList(value);
  },

  // 房间选择器确认
  onRoomPickerConfirm(event) {
    const { value, text } = event.detail;
    this.setData({
      houseId: value,
      roomNumber: text,
      showRoomPicker: false
    });
  },

  // 关闭选择器
  onDistrictPickerClose() {
    this.setData({ showDistrictPicker: false });
  },
  onBuildingPickerClose() {
    this.setData({ showBuildingPicker: false });
  },
  onUnitPickerClose() {
    this.setData({ showUnitPicker: false });
  },
  onRoomPickerClose() {
    this.setData({ showRoomPicker: false });
  },

  // 业主姓名变更
  onOwnerNameChange(event) {
    this.setData({
      ownerName: event.detail
    });
  },

  // 身份证号变更
  onOwnerIdNumberChange(event) {
    this.setData({
      ownerIdNumber: event.detail
    });
  },

  // 上传图片
  afterRead(event) {
    const { file } = event.detail;
    const { fileList = [] } = this.data;
    fileList.push({ ...file, status: 'uploading', message: '上传中' });
    this.setData({ fileList });
    
    // 上传图片
    wx.uploadFile({
      url: getApp().globalData.baseUrl + '/system/check/upload',
      filePath: file.url,
      name: 'file',
      header: {
        'Authorization': wx.getStorageSync('token')
      },
      success: (res) => {
        const { fileList } = this.data;
        fileList[fileList.length - 1].status = 'done';
        fileList[fileList.length - 1].url = JSON.parse(res.data).data;
        this.setData({ fileList });
      },
      fail: () => {
        const { fileList } = this.data;
        fileList[fileList.length - 1].status = 'failed';
        this.setData({ fileList });
      }
    });
  },

  // 删除图片
  deleteImg(event) {
    const { index } = event.detail;
    const { fileList } = this.data;
    fileList.splice(index, 1);
    this.setData({ fileList });
  },

  // 提交表单
  async submitForm() {
    const { 
      ownerName, 
      ownerIdNumber, 
      fileList,
      houseId,
      districtName,
      buildingName,
      unitName,
      roomNumber
    } = this.data;
    
    // 表单验证
    if (!houseId) {
      wx.showToast({ title: '请选择房屋', icon: 'none' });
      return;
    }
    if (!ownerName) {
      wx.showToast({ title: '请输入业主姓名', icon: 'none' });
      return;
    }
    if (!ownerIdNumber) {
      wx.showToast({ title: '请输入身份证号', icon: 'none' });
      return;
    }
    if (fileList.length === 0) {
      wx.showToast({ title: '请上传房屋证明', icon: 'none' });
      return;
    }

    this.setData({ loading: true });

    try {
      const res = await userApi.bindHouse({
        houseId,
        ownerName,
        ownerIdNumber,
        certificate: fileList[0].url
      });

      if (res.code === 200) {
        wx.showToast({ title: '提交成功', icon: 'success' });
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      } else {
        wx.showToast({ title: res.msg || '提交失败', icon: 'none' });
      }
    } catch (error) {
      wx.showToast({ title: '提交失败', icon: 'none' });
    } finally {
      this.setData({ loading: false });
    }
  }
}); 