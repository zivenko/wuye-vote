const http = require('../utils/request');

// 微信登录
const wxLogin = (code) => {
  console.log('[API] 调用登录接口，code:', code);
  return http.post('/system/users/applet/login', { code });
};

// 获取用户信息 - 使用正确的接口路径
const getUserInfo = async () => {
  console.log('[API] 尝试获取用户信息');
  
  try {
    // 使用正确的接口路径
    const res = await http.get('/system/users/applet/info');
    console.log('[API] 用户信息接口返回:', res);
    return res;
  } catch (error) {
    console.error('[API] 获取用户信息失败:', error);
    // 返回一个基本结构，以便后续代码不会出错
    return {
      code: 500,
      msg: error.message || '获取用户信息失败',
      data: null
    };
  }
};

// 更新用户信息
const updateUserInfo = (data) => {
  return http.post('/system/users/info', data);
};

// 上传头像
const uploadAvatar = (filePath) => {
  return new Promise((resolve, reject) => {
    const app = getApp();
    const baseUrl = app.globalData.baseUrl;
    
    if (!baseUrl) {
      reject(new Error('未配置服务器地址'));
      return;
    }

    console.log('[API] 开始上传头像，URL:', `${baseUrl}/system/users/applet/avatar`);
    
    wx.uploadFile({
      url: `${baseUrl}/system/users/applet/avatar`,
      filePath: filePath,
      name: 'avatarfile',
      header: {
        'Authorization': wx.getStorageSync('token')
      },
      success: (res) => {
        console.log('[API] 上传头像响应:', res);
        try {
          const data = JSON.parse(res.data);
          resolve(data);
        } catch (e) {
          console.error('[API] 解析响应数据失败:', e);
          reject(new Error('解析响应数据失败'));
        }
      },
      fail: (err) => {
        console.error('[API] 上传头像失败:', err);
        reject(err);
      }
    });
  });
};

// 获取用户头像
const getAvatarUrl = async (imagePath) => {
  try {
    const app = getApp();
    const baseUrl = app.globalData.baseUrl;
    
    if (!baseUrl) {
      throw new Error('未配置服务器地址');
    }

    if (!imagePath) {
      throw new Error('图片路径不能为空');
    }

    // 返回带图片路径的URL
    return `${baseUrl}/system/users/avatar?imagePath=${encodeURIComponent(imagePath)}`;
  } catch (error) {
    console.error('[API] 获取头像失败:', error);
    // 返回默认头像
    return 'https://img.yzcdn.cn/vant/cat.jpeg';
  }
};

// 获取用户房屋列表
const getHousesList = () => {
  return http.get('/system/houses/');
};

// 解绑房屋
const unbindHouse = (houseId) => {
  return http.post(`/system/houses/unbind/${houseId}`);
};

// 绑定房屋
const bindHouse = (data) => {
  return http.post('/system/check', data);
};

// 获取小区列表
const getDistrictList = () => {
  return http.get('/system/building/list', { level: 1 });
};

// 获取楼栋列表
const getBuildingList = (districtId) => {
  return http.get('/system/building/list', { level: 2, pid: districtId });
};

// 获取单元列表
const getUnitList = (buildingId) => {
  return http.get('/system/building/list', { level: 3, pid: buildingId });
};

// 获取房间列表
const getRoomList = (unitId) => {
  return http.get('/system/houses/list', { unitId: unitId, isBind: 0 });
};

module.exports = {
  wxLogin,
  getUserInfo,
  updateUserInfo,
  uploadAvatar,
  getAvatarUrl,
  getHousesList,
  unbindHouse,
  bindHouse,
  getDistrictList,
  getBuildingList,
  getUnitList,
  getRoomList
}; 