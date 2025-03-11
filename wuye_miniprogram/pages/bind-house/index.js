const userApi = require('../../api/user');
const app = getApp();

Page({
  data: {
    baseURL: app.globalData.baseUrl,
    isEdit: false,
    houseId: '',
    districtId: '',
    buildingId: '',
    unitId: '',
    roomNumber: '',
    certificate: '',
    districtName: '',
    buildingName: '',
    unitName: '',
    type: '',
    area: '',
    fileList: [],
    loading: false,
    showDistrictPicker: false,
    showBuildingPicker: false,
    showUnitPicker: false,
    showRoomPicker: false,
    districtList: [],
    buildingList: [],
    unitList: [],
    roomList: [],
    columns: [],
    checkId: ''
  },

  onLoad(options) {
    if (options) {
      this.setData({
        isEdit: options.isEdit === 'true',
        houseId: options.houseId || '',
        checkId: options.checkId || ''
      });

      // 如果是编辑模式且有房屋ID，先获取房屋详情
      if (options.isEdit === 'true' && options.houseId) {
        this.loadHouseDetail(options.houseId);
      }
    }
  },

  // 加载房屋详情
  async loadHouseDetail(houseId) {
    try {
      const res = await userApi.getHouseDetail(houseId);
      console.log('房屋详情:', res);
      
      if (res.code === 200 && res.data) {
        const house = res.data;
        
        // 设置房屋基本信息
        this.setData({
          districtId: house.districtId,
          buildingId: house.buildingId,
          unitId: house.unitId,
          roomNumber: house.roomNumber,
          districtName: house.districtName,
          buildingName: house.buildingName,
          unitName: house.unitName,
          type: house.type,
          area: house.area
        });

        // 如果有证书图片，添加到文件列表
        if (house.certificate) {
          this.setData({
            certificate: house.certificate,
            fileList: [{
              url: `${this.data.baseURL}/system/check/certificate?imagePath=${house.certificate}`,
              name: '房屋证明',
              isImage: true
            }]
          });
        }

        // 加载相关选项列表
        await this.loadDistrictList();
        if (house.districtId) {
          await this.loadBuildingList(house.districtId);
        }
        if (house.buildingId) {
          await this.loadUnitList(house.buildingId);
        }
        if (house.unitId) {
          await this.loadRoomList(house.unitId);
        }
      } else {
        wx.showToast({
          title: res.msg || '获取房屋信息失败',
          icon: 'none'
        });
      }
    } catch (error) {
      console.error('获取房屋信息失败:', error);
      wx.showToast({
        title: '获取房屋信息失败',
        icon: 'none'
      });
    }
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
      console.log('楼栋列表返回数据:', res, districtId);
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
    const { name, id } = value;

    console.log('小区选择器确认:', value, text);

    this.setData({
      districtId: id,
      districtName: name,
      buildingId: '',
      buildingName: '',
      unitId: '',
      unitName: '',
      houseId: '',
      roomNumber: '',
      showDistrictPicker: false
    });
    this.loadBuildingList(id);
  },

  // 楼栋选择器确认
  onBuildingPickerConfirm(event) {
    const { value, text } = event.detail;
    const { name, id } = value;

    this.setData({
      buildingId: id,
      buildingName: name,
      unitId: '',
      unitName: '',
      houseId: '',
      roomNumber: '',
      showBuildingPicker: false
    });
    this.loadUnitList(id);
  },

  // 单元选择器确认
  onUnitPickerConfirm(event) {
    const { value, text } = event.detail;
    const { name, id } = value;
    this.setData({
      unitId: id,
      unitName: name,
      houseId: '',
      roomNumber: '',
      showUnitPicker: false
    });
    this.loadRoomList(id);
  },

  // 房间选择器确认
  onRoomPickerConfirm(event) {
    const { value, text } = event.detail.value;
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

  // 上传图片
  afterRead(event) {
    const { file } = event.detail;
    const { fileList = [] } = this.data;
    fileList.push({ ...file, status: 'done' });
    this.setData({ fileList });
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
      fileList,
      houseId,
      isEdit,
      checkId,
      certificate
    } = this.data;
    
    // 表单验证
    if (!houseId) {
      wx.showToast({ title: '请选择房屋', icon: 'none' });
      return;
    }
    if (fileList.length === 0) {
      wx.showToast({ title: '请上传房屋证明', icon: 'none' });
      return;
    }

    this.setData({ loading: true });

    try {
      if (isEdit) {
        // 更新模式
        if (!checkId) {
          wx.showToast({ title: '缺少审核记录ID', icon: 'none' });
          return;
        }

        // 判断是否更换了图片
        const isNewFile = !fileList[0].url.startsWith('http');
        
        if (isNewFile) {
          // 如果上传了新图片，使用新图片路径
          const result = await userApi.updateHouseCheck(checkId, houseId, fileList[0].url);
          if (result.code === 200) {
            wx.showToast({ title: '修改成功', icon: 'success' });
            setTimeout(() => {
              wx.navigateBack();
            }, 1500);
          } else {
            wx.showToast({ title: result.msg || '修改失败', icon: 'none' });
          }
        } else {
          // 如果没有更换图片，使用普通的POST请求
          const result = await userApi.updateHouseCheckWithoutFile(checkId, houseId);
          if (result.code === 200) {
            wx.showToast({ title: '修改成功', icon: 'success' });
            setTimeout(() => {
              wx.navigateBack();
            }, 1500);
          } else {
            wx.showToast({ title: result.msg || '修改失败', icon: 'none' });
          }
        }
      } else {
        // 新增模式
        wx.uploadFile({
          url: getApp().globalData.baseUrl + '/system/check',
          filePath: fileList[0].url,
          name: 'file',
          formData: {
            houseId: String(houseId)
          },
          header: {
            'Authorization': wx.getStorageSync('token')
          },
          success: (res) => {
            const result = JSON.parse(res.data);
            if (result.code === 200) {
              wx.showToast({ title: '提交成功', icon: 'success' });
              setTimeout(() => {
                wx.navigateBack();
              }, 1500);
            } else {
              wx.showToast({ title: result.msg || '提交失败', icon: 'none' });
            }
          },
          fail: () => {
            wx.showToast({ title: '提交失败', icon: 'none' });
          }
        });
      }
    } catch (error) {
      console.error('提交失败:', error);
      wx.showToast({ 
        title: isEdit ? '修改失败' : '提交失败', 
        icon: 'none' 
      });
    } finally {
      this.setData({ loading: false });
    }
  }
}); 