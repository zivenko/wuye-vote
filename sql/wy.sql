CREATE TABLE IF NOT EXISTS `wuye_building` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT ,
  `pid` BIGINT DEFAULT 0 COMMENT '父级id',
  `name` VARCHAR(100) COMMENT '名称',
	`address` VARCHAR(255) COMMENT '地址',
  `level` INT DEFAULT 0 COMMENT '层级',
	`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	`create_by` BIGINT COMMENT '创建人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '物业小区层级表';

CREATE TABLE `wuye_houses` (
    `house_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '房屋ID',
    `district_id` BIGINT COMMENT '小区ID',
    `building_id` BIGINT COMMENT '楼栋ID',
    `unit_id` BIGINT COMMENT '单元ID',
    `room_number` VARCHAR(20) COMMENT '房号',
    `area` DECIMAL(10,2) COMMENT '建筑面积',
	  `type` VARCHAR(100) COMMENT '物业类型(高层、洋房、别墅、联排、商铺)',
		`house_number` VARCHAR(255) COMMENT '房屋编号',
		`owner_names` VARCHAR(255) COMMENT '业主姓名',
    `owner_mobiles` VARCHAR(255) COMMENT '业主电话',
    `owner_id_numbers` VARCHAR(255) COMMENT '业主身份证号',
		`is_bind` INT DEFAULT 0 COMMENT '小程序绑定人(0：未绑定，1：已绑定)',
		`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
		`update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
		`create_by` BIGINT COMMENT '创建人'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '物业房屋表';




CREATE TABLE `wuye_applet_users` (
    `applet_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '小程序用户ID',
    `nike_name` VARCHAR(100) COMMENT '姓名',
		`openid` VARCHAR(255) COMMENT '小程序用户OPENID',
		`avatar` VARCHAR(255) COMMENT '头像',
		`name` VARCHAR(255) COMMENT '业主姓名',
    `mobile` VARCHAR(255) COMMENT '业主电话',
    `id_number` VARCHAR(255) COMMENT '业主身份证号',
		`house_ids` VARCHAR(512) COMMENT '房屋IDs(保存形式如：1,2,3)',
		`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
		`update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '小程序用户表';



CREATE TABLE `wuye_house_bind_check` (
    `check_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '检查表ID',
		`applet_id` BIGINT COMMENT '小程序用户ID',
		`house_id` BIGINT COMMENT '房屋ID',
		`certificate` VARCHAR(255) COMMENT '房屋证明',
		`check_status` VARCHAR(20) DEFAULT 'uncheck' COMMENT '审核状态（success, fail, uncheck)',
		`check_error_msg` VARCHAR(512) COMMENT '审核失败信息',
		`check_time` DATETIME COMMENT '审核时间',
		`user_id` BIGINT COMMENT '审核员ID',
		`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
		`update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '房屋绑定审核表';



CREATE TABLE `wuye_vote_template` (
    `template_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '模板ID',
    `title` VARCHAR(100) COMMENT '标题',
		`description` VARCHAR(255) COMMENT '描述',
		`choices` VARCHAR(255) COMMENT '选项(JSON形式)',
		`vote_times` INT COMMENT '投票次数',
    `rule` VARCHAR(255) COMMENT '规则',
		`community_ids` VARCHAR(512) COMMENT '小区IDs',
		`start_time` DATETIME COMMENT '开始时间',
		`end_time` DATETIME COMMENT '结束时间',
		`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
		`update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
		`create_by` BIGINT COMMENT '操作员ID'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '投票模板表';



CREATE TABLE `wuye_vote_record` (
    `vote_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '投票记录ID',
    `applet_id` BIGINT NOT NULL COMMENT '用户ID',
    `template_id` BIGINT NOT NULL COMMENT '模板ID',
    `choices` VARCHAR(255) NOT NULL COMMENT '用户选择的选项',
    `vote_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '投票时间',
    `status` INT DEFAULT 1 COMMENT '投票状态(有效: 1/无效: 0)',
		`by_admin` BIGINT DEFAULT -1 COMMENT '通过管理员投票',
		`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
		`update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='投票记录表';



