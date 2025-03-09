const http = require('../utils/request');

// 获取投票模板列表
const getVoteTemplateList = (params) => {
  return http.get('/system/vote/template/list', params);
};

// 获取投票模板详情
const getVoteTemplateDetail = (templateId) => {
  return http.get(`/system/template/detail/${templateId}`);
};

// 获取用户可参与的投票列表
const getUserVoteTemplateList = (appletId, keyword, status) => {
  return http.get(`/system/template/user/${appletId}`, {
      keyword,
      status
  });
};

// 提交投票
const submitVote = (data) => {
  return http.post('/system/record/submit', data);
};

// 获取我的投票记录
const getMyVoteList = (params) => {
  return http.get('/system/vote/my/list', params);
};

// 获取投票结果
const getVoteResult = (templateId) => {
  return http.get(`/system/vote/result/${templateId}`);
};

module.exports = {
  getVoteTemplateList,
  getVoteTemplateDetail,
  getUserVoteTemplateList,
  submitVote,
  getMyVoteList,
  getVoteResult
}; 