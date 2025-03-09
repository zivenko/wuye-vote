-- 创建用户表
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户唯一标识符，自动递增',
    user_name VARCHAR(255) NOT NULL COMMENT '用户的姓名',
    phone_number VARCHAR(20) COMMENT '用户的电话号码',
    wechat_id VARCHAR(255) UNIQUE NOT NULL COMMENT '用户的微信ID，用于微信登录，必须唯一',
    email VARCHAR(255) UNIQUE COMMENT '用户的电子邮件地址，必须唯一',
    password_hash VARCHAR(255) NOT NULL COMMENT '用户密码的哈希值，存储哈希值而不是明文密码以提高安全性'
) COMMENT='用户表';

-- 创建小区表
CREATE TABLE communities (
    community_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '小区唯一标识符，自动递增',
    community_name VARCHAR(255) NOT NULL COMMENT '小区的名称',
    address VARCHAR(255) COMMENT '小区的地址',
    manager_id INT COMMENT '管理该小区的管理员的用户ID'
) COMMENT='小区表';

-- 创建楼栋表
CREATE TABLE buildings (
    building_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '楼栋唯一标识符，自动递增',
    community_id INT COMMENT '楼栋所属小区的ID',
    building_name VARCHAR(255) NOT NULL COMMENT '楼栋的名称',
    building_number VARCHAR(50) COMMENT '楼栋的编号'
) COMMENT='楼栋表';

-- 创建单元表
CREATE TABLE units (
    unit_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '单元唯一标识符，自动递增',
    building_id INT COMMENT '单元所属楼栋的ID',
    unit_number VARCHAR(50) COMMENT '单元的编号'
) COMMENT='单元表';

-- 创建房屋表
CREATE TABLE houses (
    house_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '房屋唯一标识符，自动递增',
    unit_id INT COMMENT '房屋所属单元的ID',
    room_number VARCHAR(50) COMMENT '房屋的房间号',
    property_type VARCHAR(50) COMMENT '房屋的类型（如高层、洋房、别墅等）',
    building_area DECIMAL(10, 2) COMMENT '房屋的建筑面积',
    owner_name VARCHAR(255) COMMENT '房屋所有者的姓名',
    owner_id_number VARCHAR(18) COMMENT '房屋所有者的身份证号码',
    owner_phone_number VARCHAR(20) COMMENT '房屋所有者的电话号码'
) COMMENT='房屋表';

-- 创建业主表
CREATE TABLE owners (
    owner_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '业主唯一标识符，自动递增',
    house_id INT COMMENT '业主所属房屋的ID',
    owner_name VARCHAR(255) COMMENT '业主的姓名',
    owner_id_number VARCHAR(18) COMMENT '业主的身份证号码',
    phone_number VARCHAR(20) COMMENT '业主的电话号码'
) COMMENT='业主表';

-- 创建投票模板表
CREATE TABLE vote_templates (
    template_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '投票模板唯一标识符，自动递增',
    title VARCHAR(255) NOT NULL COMMENT '投票模板的标题',
    description TEXT COMMENT '投票模板的描述',
    options TEXT COMMENT '投票模板的选项',
    rules TEXT COMMENT '投票模板的规则'
) COMMENT='投票模板表';

-- 创建投票表
CREATE TABLE votes (
    vote_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '投票唯一标识符，自动递增',
    template_id INT COMMENT '投票所使用的模板ID',
    start_date DATETIME NOT NULL COMMENT '投票开始日期和时间',
    end_date DATETIME NOT NULL COMMENT '投票结束日期和时间',
    status VARCHAR(50) COMMENT '投票的状态'
) COMMENT='投票表';

-- 创建投票选项表
CREATE TABLE vote_options (
    option_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '投票选项唯一标识符，自动递增',
    vote_id INT COMMENT '投票选项所属投票的ID',
    option_text VARCHAR(255) NOT NULL COMMENT '投票选项的文本'
) COMMENT='投票选项表';

-- 创建投票记录表
CREATE TABLE vote_records (
    record_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '投票记录唯一标识符，自动递增',
    vote_id INT COMMENT '投票记录所属投票的ID',
    owner_id INT COMMENT '投票记录所属业主的ID',
    selected_option_id INT COMMENT '投票记录中选择的选项ID',
    vote_date DATETIME COMMENT '投票的日期和时间'
) COMMENT='投票记录表';

-- 创建投票统计表
CREATE TABLE vote_statistics (
    statistic_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '投票统计唯一标识符，自动递增',
    vote_id INT COMMENT '投票统计所属投票的ID',
    total_votes INT COMMENT '投票的总票数',
    total_area DECIMAL(10, 2) COMMENT '投票的总面积'
) COMMENT='投票统计表';

-- 创建管理员表
CREATE TABLE admins (
    admin_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '管理员唯一标识符，自动递增',
    user_id INT COMMENT '管理员的用户ID',
    role VARCHAR(50) COMMENT '管理员的角色'
) COMMENT='管理员表';

-- 创建审核记录表
CREATE TABLE review_records (
    review_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '审核记录唯一标识符，自动递增',
    owner_id INT COMMENT '审核记录所属业主的ID',
    review_status VARCHAR(50) COMMENT '审核的状态',
    review_date DATETIME COMMENT '审核的日期和时间',
    reason TEXT COMMENT '审核不通过的原因'
) COMMENT='审核记录表';