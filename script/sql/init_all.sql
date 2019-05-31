/*
 Navicat Premium Data Transfer

 Source Server         : shop146
 Source Server Type    : MySQL
 Source Server Version : 50642
 Source Host           : 192.168.0.146:3306
 Source Schema         : shop

 Target Server Type    : MySQL
 Target Server Version : 50642
 File Encoding         : 65001

 Date: 31/05/2019 15:29:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for BASE
-- ----------------------------
DROP TABLE IF EXISTS `BASE`;
CREATE TABLE `BASE`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for BASE_DEL
-- ----------------------------
DROP TABLE IF EXISTS `BASE_DEL`;
CREATE TABLE `BASE_DEL`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for CORE_BANK_CARD
-- ----------------------------
DROP TABLE IF EXISTS `CORE_BANK_CARD`;
CREATE TABLE `CORE_BANK_CARD`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `bank_type` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '银行名称',
  `bank_address` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开户行地区',
  `bank_name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开户行名称',
  `card_num` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '银行卡号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for CORE_SHOP
-- ----------------------------
DROP TABLE IF EXISTS `CORE_SHOP`;
CREATE TABLE `CORE_SHOP`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `logo` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Logo',
  `banner` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'Banner',
  `summary` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '简介',
  `notice` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公告',
  `scope` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '经营范围',
  `address` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `wechat` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '微信',
  `telephone` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `hours` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '营业时间',
  `return_address` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '退货地址',
  `authenticate` tinyint(2) NULL DEFAULT NULL COMMENT '认证 1,0',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态 1,0',
  `valid_date` datetime(0) NULL DEFAULT NULL COMMENT '有效期',
  `enterprise` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '企业名称',
  `license_num` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '营业执照号',
  `license_pic` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '营业执照图',
  `corporation_name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '法人姓名',
  `corporation_card` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '法人身份证号',
  `corporation_pic` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '法人身份证图',
  `balance` decimal(16, 2) NULL DEFAULT NULL COMMENT '资金余额',
  `frozen` decimal(16, 2) NULL DEFAULT NULL COMMENT '冻结余额',
  `deposit` decimal(16, 2) NULL DEFAULT NULL COMMENT '保证金额',
  `recharge` decimal(16, 2) NULL DEFAULT NULL COMMENT '充值金额',
  `secret` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for CORE_SHOP_DEPOSIT
-- ----------------------------
DROP TABLE IF EXISTS `CORE_SHOP_DEPOSIT`;
CREATE TABLE `CORE_SHOP_DEPOSIT`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态 1,0',
  `amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for CORE_SHOP_FLOW
-- ----------------------------
DROP TABLE IF EXISTS `CORE_SHOP_FLOW`;
CREATE TABLE `CORE_SHOP_FLOW`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `order_sn` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单号',
  `payment_type` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付方式',
  `payment_sn` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付流水',
  `product_name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品名称',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态 1,0',
  `type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '类型',
  `amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '金额',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_shop_id`(`shop_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for CORE_SHOP_KEEPER
-- ----------------------------
DROP TABLE IF EXISTS `CORE_SHOP_KEEPER`;
CREATE TABLE `CORE_SHOP_KEEPER`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `account` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `access_token` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '令牌',
  `access_expired` datetime(0) NULL DEFAULT NULL COMMENT '令牌时效',
  `register_ip` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '注册IP',
  `login_date` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `login_ip` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态 1,0',
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `role` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  `remark` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique`(`account`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for CORE_SHOP_WITHDRAW
-- ----------------------------
DROP TABLE IF EXISTS `CORE_SHOP_WITHDRAW`;
CREATE TABLE `CORE_SHOP_WITHDRAW`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `shop_keeper_id` bigint(20) NULL DEFAULT NULL COMMENT '店员ID',
  `shop_keeper_account` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '店员账号',
  `amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '金额',
  `arrival_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '到账金额',
  `status` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `bank_type` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '银行名称',
  `bank_address` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开户行地区',
  `bank_name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开户行名称',
  `card_num` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '银行卡号',
  `pay_type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付方式',
  `pay_sn` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付流水',
  `pay_date` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `pay_account` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付账号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_shop_id`(`shop_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for LOG_SMS
-- ----------------------------
DROP TABLE IF EXISTS `LOG_SMS`;
CREATE TABLE `LOG_SMS`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `mobile` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '验证码',
  `ip` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `deadline` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态 0,1',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique`(`mobile`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for M_ADDRESS
-- ----------------------------
DROP TABLE IF EXISTS `M_ADDRESS`;
CREATE TABLE `M_ADDRESS`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `member_id` bigint(20) NULL DEFAULT NULL COMMENT '会员ID',
  `member_account` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '会员账号',
  `code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '行政编码',
  `province` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '城市',
  `district` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '区县',
  `address` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `mobile` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机',
  `defaults` tinyint(2) NULL DEFAULT NULL COMMENT '默认',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_member_id`(`member_id`) USING BTREE,
  INDEX `idx_defaults`(`defaults`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for M_DRAWEE
-- ----------------------------
DROP TABLE IF EXISTS `M_DRAWEE`;
CREATE TABLE `M_DRAWEE`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '行政编码',
  `province` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '城市',
  `district` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '区县',
  `address` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `mobile` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_member_id`(`shop_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for M_MEMBER
-- ----------------------------
DROP TABLE IF EXISTS `M_MEMBER`;
CREATE TABLE `M_MEMBER`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `account` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `access_token` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '令牌',
  `access_expired` datetime(0) NULL DEFAULT NULL COMMENT '令牌时效',
  `register_ip` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '注册IP',
  `login_date` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `login_ip` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态 1,0',
  `sex` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `consume_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '消费金额',
  `consume_times` int(10) NULL DEFAULT NULL COMMENT '消费次数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique`(`account`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for M_MEMBER_COUPON
-- ----------------------------
DROP TABLE IF EXISTS `M_MEMBER_COUPON`;
CREATE TABLE `M_MEMBER_COUPON`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `shop_name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '店铺名',
  `member_id` bigint(20) NULL DEFAULT NULL COMMENT '会员ID',
  `coupon_id` bigint(20) NULL DEFAULT NULL COMMENT '优惠券ID',
  `coupon_name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `service_start_date` datetime(0) NULL DEFAULT NULL COMMENT '使用固定期限-开始',
  `service_end_date` datetime(0) NULL DEFAULT NULL COMMENT '使用固定期限-结束',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态 1,0',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_member_id`(`member_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for M_MEMBER_DUMMY
-- ----------------------------
DROP TABLE IF EXISTS `M_MEMBER_DUMMY`;
CREATE TABLE `M_MEMBER_DUMMY`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `avatar` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10001 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for M_MEMBER_FEEDBACK
-- ----------------------------
DROP TABLE IF EXISTS `M_MEMBER_FEEDBACK`;
CREATE TABLE `M_MEMBER_FEEDBACK`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `shop_keeper_id` bigint(20) NULL DEFAULT NULL COMMENT '店员ID',
  `shop_keeper_account` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '店员账号',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for M_MEMBER_FOCUS
-- ----------------------------
DROP TABLE IF EXISTS `M_MEMBER_FOCUS`;
CREATE TABLE `M_MEMBER_FOCUS`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `member_id` bigint(20) NULL DEFAULT NULL COMMENT '会员ID',
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique`(`member_id`, `shop_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for M_MEMBER_SHOP
-- ----------------------------
DROP TABLE IF EXISTS `M_MEMBER_SHOP`;
CREATE TABLE `M_MEMBER_SHOP`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `member_id` bigint(20) NULL DEFAULT NULL COMMENT '会员ID',
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique`(`member_id`, `shop_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for M_OAUTH
-- ----------------------------
DROP TABLE IF EXISTS `M_OAUTH`;
CREATE TABLE `M_OAUTH`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `channel` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '渠道',
  `open_id` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'openID',
  `union_id` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'unionID',
  `nickname` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `access_token` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '令牌',
  `access_expired` datetime(0) NULL DEFAULT NULL COMMENT '令牌时效',
  `account` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '绑定账号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique`(`union_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for O_ALLOT
-- ----------------------------
DROP TABLE IF EXISTS `O_ALLOT`;
CREATE TABLE `O_ALLOT`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `sn` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '调拨单号',
  `status` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `shop_keeper_id` bigint(20) NULL DEFAULT NULL COMMENT '发起店员ID',
  `shop_keeper_account` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发起店员账号',
  `from_shop_id` bigint(20) NULL DEFAULT NULL COMMENT '发货方店铺ID',
  `from_name` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发货方收货人',
  `from_mobile` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发货方联系电话',
  `from_address` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发货方收货地址',
  `to_shop_id` bigint(20) NULL DEFAULT NULL COMMENT '收货方店铺ID',
  `to_name` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收货方收货人',
  `to_mobile` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收货方联系电话',
  `to_address` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收货方收货地址',
  `product_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '商品总价',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for O_ALLOT_ITEM
-- ----------------------------
DROP TABLE IF EXISTS `O_ALLOT_ITEM`;
CREATE TABLE `O_ALLOT_ITEM`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `allot_id` bigint(20) NULL DEFAULT NULL COMMENT '调拨单ID',
  `allot_sn` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '调拨单号',
  `status` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `from_shop_id` bigint(20) NULL DEFAULT NULL COMMENT '发货方店铺ID',
  `from_shop_name` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发货方店铺名',
  `to_shop_id` bigint(20) NULL DEFAULT NULL COMMENT '收货方店铺ID',
  `target_id` bigint(20) NULL DEFAULT NULL COMMENT '目标ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_sku_id` bigint(20) NULL DEFAULT NULL COMMENT '商品SKU的ID',
  `quantity` int(10) NULL DEFAULT NULL COMMENT '商品数量',
  `standard` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品规格',
  `product_name` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_pic` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品图片',
  `product_price` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品价格',
  `supply_price` decimal(20, 2) NULL DEFAULT NULL COMMENT '供货价',
  `total_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '明细总价',
  `logistics_code` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '物流代码',
  `logistics_com` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '物流公司',
  `logistics_num` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '物流单号',
  `actual_quantity` int(10) NULL DEFAULT NULL COMMENT '实收数量',
  `difference` tinyint(2) NULL DEFAULT NULL COMMENT '差异标识 0,1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for O_CART_ITEM
-- ----------------------------
DROP TABLE IF EXISTS `O_CART_ITEM`;
CREATE TABLE `O_CART_ITEM`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `member_id` bigint(20) NULL DEFAULT NULL COMMENT '会员ID',
  `member_account` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '会员账号',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_sku_id` bigint(20) NULL DEFAULT NULL COMMENT '商品SKU的ID',
  `quantity` int(10) NULL DEFAULT NULL COMMENT '商品数量',
  `standard` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品规格',
  `product_name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_pic` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品图片',
  `product_price` decimal(16, 2) NULL DEFAULT NULL COMMENT '商品单价',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_member_shop_id`(`shop_id`, `member_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for O_CROWD_GROUP
-- ----------------------------
DROP TABLE IF EXISTS `O_CROWD_GROUP`;
CREATE TABLE `O_CROWD_GROUP`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_pic` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品图片',
  `standard` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品规格',
  `product_price` decimal(16, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `crowd_price` decimal(16, 2) NULL DEFAULT NULL COMMENT '实时单价',
  `crowd_num` int(10) NULL DEFAULT NULL COMMENT '成团人数',
  `attend_num` int(10) NULL DEFAULT NULL COMMENT '参团人数',
  `paid_num` int(10) NULL DEFAULT NULL COMMENT '付款人数',
  `deadline` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态',
  `virtual` tinyint(2) NULL DEFAULT NULL COMMENT '虚拟 1,0',
  `avatars` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '头像列表',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for O_ORDER
-- ----------------------------
DROP TABLE IF EXISTS `O_ORDER`;
CREATE TABLE `O_ORDER`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `offline` tinyint(2) NULL DEFAULT NULL COMMENT '线下',
  `member_id` bigint(20) NULL DEFAULT NULL COMMENT '会员ID',
  `member_account` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '会员账号',
  `province` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '城市',
  `district` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '区县',
  `address` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `mobile` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机',
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `shop_name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '店铺名',
  `crowd_id` bigint(20) NULL DEFAULT NULL COMMENT '拼团团ID',
  `sn` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单号',
  `type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '类型',
  `status` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `product_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '商品总价',
  `coupon_id` bigint(20) NULL DEFAULT NULL COMMENT '优惠券ID',
  `coupon_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '优惠券折减',
  `pay_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '实际支付',
  `payment_type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付方式',
  `payment_sn` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付流水',
  `expire_date` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_sn`(`sn`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for O_ORDER_ITEM
-- ----------------------------
DROP TABLE IF EXISTS `O_ORDER_ITEM`;
CREATE TABLE `O_ORDER_ITEM`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `offline` tinyint(2) NULL DEFAULT NULL COMMENT '线下',
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `shop_name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '店铺名',
  `member_id` bigint(20) NULL DEFAULT NULL COMMENT '会员ID',
  `member_account` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '会员账号',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_sku_id` bigint(20) NULL DEFAULT NULL COMMENT '商品SKU的ID',
  `quantity` int(10) NULL DEFAULT NULL COMMENT '商品数量',
  `standard` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品规格',
  `product_name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_pic` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品图片',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单ID',
  `order_sn` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单号',
  `type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '类型',
  `status` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `virtual` tinyint(2) NULL DEFAULT NULL COMMENT '虚拟 1,0',
  `payment_type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付方式',
  `payment_sn` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付流水',
  `product_price` decimal(16, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `real_price` decimal(16, 2) NULL DEFAULT NULL COMMENT '实时单价',
  `pay_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '实际支付',
  `coupon_weighting_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '优惠券加权折减',
  `crowd_id` bigint(20) NULL DEFAULT NULL COMMENT '拼团团ID',
  `logistics_code` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '物流代码',
  `logistics_com` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '物流公司',
  `logistics_num` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '物流单号',
  `after_type` tinyint(2) NULL DEFAULT NULL COMMENT '售后类型',
  `after_date` datetime(0) NULL DEFAULT NULL COMMENT '售后时间',
  `after_pic` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '售后凭证',
  `after_memo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '售后理由',
  `after_quantity` int(10) NULL DEFAULT NULL COMMENT '售后数量',
  `after_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '售后金额',
  `old_status` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '先前状态',
  `reship_logistics_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '退货物流代码',
  `reship_logistics_com` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '退货物流公司',
  `reship_logistics_num` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '退货物流单号',
  `reship_name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '退货收货人',
  `reship_mobile` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '退货联系电话',
  `reship_address` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '退货收货地址',
  `refuse_memo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '售后拒绝理由',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_sn`(`order_sn`) USING BTREE,
  INDEX `idx_type`(`type`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_crowd_id`(`crowd_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for O_PACKAGE
-- ----------------------------
DROP TABLE IF EXISTS `O_PACKAGE`;
CREATE TABLE `O_PACKAGE`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `shop_keeper_id` bigint(20) NULL DEFAULT NULL COMMENT '店员ID',
  `shop_keeper_account` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '店员账号',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_sku_id` bigint(20) NULL DEFAULT NULL COMMENT '商品SKU的ID',
  `quantity` int(10) NULL DEFAULT NULL COMMENT '商品数量',
  `standard` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品规格',
  `product_name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_pic` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品图片',
  `product_price` decimal(16, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `buyout` bigint(20) NULL DEFAULT NULL COMMENT '买断 1,0',
  `allot` bigint(20) NULL DEFAULT NULL COMMENT '调拨 1,0',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_shop_keeper_id`(`shop_keeper_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for O_PAYMENT
-- ----------------------------
DROP TABLE IF EXISTS `O_PAYMENT`;
CREATE TABLE `O_PAYMENT`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `order_sn` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单号',
  `trade_type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付场景',
  `payment_type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付方式',
  `payment_sn` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付流水',
  `prepay_id` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '预付单ID',
  `app_id` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '应用号ID',
  `mch_id` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商户号ID',
  `open_id` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'openID',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态',
  `amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '支付金额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique`(`order_sn`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for O_PURCH
-- ----------------------------
DROP TABLE IF EXISTS `O_PURCH`;
CREATE TABLE `O_PURCH`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `sn` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '调拨单号',
  `status` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `shop_keeper_id` bigint(20) NULL DEFAULT NULL COMMENT '发起店员ID',
  `shop_keeper_account` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发起店员账号',
  `from_shop_id` bigint(20) NULL DEFAULT NULL COMMENT '发货方店铺ID',
  `from_name` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发货方收货人',
  `from_mobile` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发货方联系电话',
  `from_address` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发货方收货地址',
  `to_shop_id` bigint(20) NULL DEFAULT NULL COMMENT '收货方店铺ID',
  `to_name` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收货方收货人',
  `to_mobile` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收货方联系电话',
  `to_address` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收货方收货地址',
  `product_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '商品总价',
  `pay_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '实际支付',
  `payment_type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付方式',
  `payment_sn` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付流水',
  `expire_date` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for O_PURCH_ITEM
-- ----------------------------
DROP TABLE IF EXISTS `O_PURCH_ITEM`;
CREATE TABLE `O_PURCH_ITEM`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `purch_id` bigint(20) NULL DEFAULT NULL COMMENT '采购单ID',
  `purch_sn` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '采购单号',
  `status` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `from_shop_id` bigint(20) NULL DEFAULT NULL COMMENT '发货方店铺ID',
  `from_shop_name` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发货方店铺名',
  `to_shop_id` bigint(20) NULL DEFAULT NULL COMMENT '收货方店铺ID',
  `target_id` bigint(20) NULL DEFAULT NULL COMMENT '目标ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `product_sku_id` bigint(20) NULL DEFAULT NULL COMMENT '商品SKU的ID',
  `quantity` int(10) NULL DEFAULT NULL COMMENT '商品数量',
  `standard` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品规格',
  `product_name` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品名称',
  `product_pic` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品图片',
  `product_price` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品价格',
  `supply_price` decimal(20, 2) NULL DEFAULT NULL COMMENT '供货价',
  `total_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '明细总价',
  `logistics_code` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '物流代码',
  `logistics_com` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '物流公司',
  `logistics_num` varchar(160) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '物流单号',
  `actual_quantity` int(10) NULL DEFAULT NULL COMMENT '实收数量',
  `difference` tinyint(2) NULL DEFAULT NULL COMMENT '差异标识 0,1',
  `payment_type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付方式',
  `payment_sn` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付流水',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for O_SETTLEMENT
-- ----------------------------
DROP TABLE IF EXISTS `O_SETTLEMENT`;
CREATE TABLE `O_SETTLEMENT`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `shop_keeper_id` bigint(20) NULL DEFAULT NULL COMMENT '店员ID',
  `shop_keeper_account` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '店员账号',
  `sn` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '结算单号',
  `status` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `goods` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '货品小计',
  `pay_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '实际支付',
  `payment_type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付方式',
  `payment_sn` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付流水',
  `expire_date` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `from_shop_id` bigint(20) NULL DEFAULT NULL COMMENT '来源店铺ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for P_ALLOT_SKU
-- ----------------------------
DROP TABLE IF EXISTS `P_ALLOT_SKU`;
CREATE TABLE `P_ALLOT_SKU`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `from_shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `to_shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `sku_id` bigint(20) NULL DEFAULT NULL COMMENT 'skuID',
  `stock` int(10) NULL DEFAULT NULL COMMENT '库存',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for P_BRAND
-- ----------------------------
DROP TABLE IF EXISTS `P_BRAND`;
CREATE TABLE `P_BRAND`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `logo` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'LOGO',
  `qr_code` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '二维码',
  `address` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for P_COUPON
-- ----------------------------
DROP TABLE IF EXISTS `P_COUPON`;
CREATE TABLE `P_COUPON`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `shop_name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '店铺名称',
  `name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '类型',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态 1,0',
  `crowd` tinyint(2) NULL DEFAULT NULL COMMENT '拼团 1,0',
  `need_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '满XX元',
  `amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '减XX元',
  `circulation` int(10) NULL DEFAULT NULL COMMENT '总发行量',
  `consumption` int(10) NULL DEFAULT NULL COMMENT '已领用量',
  `restriction` int(10) NULL DEFAULT NULL COMMENT '单人限领',
  `receive_start_date` datetime(0) NULL DEFAULT NULL COMMENT '领取期限-开始',
  `receive_end_date` datetime(0) NULL DEFAULT NULL COMMENT '领取期限-结束',
  `service_start_date` datetime(0) NULL DEFAULT NULL COMMENT '使用固定期限-开始',
  `service_end_date` datetime(0) NULL DEFAULT NULL COMMENT '使用固定期限-结束',
  `service_sustain` int(10) NULL DEFAULT NULL COMMENT '使用顺延期限-小时',
  `remark` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注说明',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_shop_id`(`shop_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for P_COUPON_PRODUCT
-- ----------------------------
DROP TABLE IF EXISTS `P_COUPON_PRODUCT`;
CREATE TABLE `P_COUPON_PRODUCT`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `coupon_id` bigint(20) NULL DEFAULT NULL COMMENT '优惠券ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '类型 1,-1',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique`(`coupon_id`, `product_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for P_FREIGHT
-- ----------------------------
DROP TABLE IF EXISTS `P_FREIGHT`;
CREATE TABLE `P_FREIGHT`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `formula` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for P_PRODUCT
-- ----------------------------
DROP TABLE IF EXISTS `P_PRODUCT`;
CREATE TABLE `P_PRODUCT`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `source_id` bigint(20) NULL DEFAULT NULL COMMENT '来源ID',
  `sn` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '条码',
  `name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `pic` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片',
  `video` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '视频',
  `supply_price` decimal(16, 2) NULL DEFAULT NULL COMMENT '供货价',
  `price` decimal(16, 2) NULL DEFAULT NULL COMMENT '销售价',
  `profit` decimal(16, 2) NULL DEFAULT NULL COMMENT '利润',
  `stock` int(10) NULL DEFAULT NULL COMMENT '商品库存',
  `freight` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '运费公式',
  `brand_id` bigint(20) NULL DEFAULT NULL COMMENT '品牌ID',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '类目ID',
  `classify_id` bigint(20) NULL DEFAULT NULL COMMENT '分类ID',
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `status` int(10) NULL DEFAULT NULL COMMENT '状态 1,0',
  `buyout` int(10) NULL DEFAULT NULL COMMENT '买断 1,0',
  `allot` int(10) NULL DEFAULT NULL COMMENT '调拨 1,0',
  `virtual` int(10) NULL DEFAULT NULL COMMENT '虚拟 1,0',
  `crowd` int(10) NULL DEFAULT NULL COMMENT '拼团 1,0',
  `crowd_price` decimal(16, 2) NULL DEFAULT NULL COMMENT '拼团价',
  `crowd_start_date` datetime(0) NULL DEFAULT NULL COMMENT '拼团开始时间',
  `crowd_end_date` datetime(0) NULL DEFAULT NULL COMMENT '拼团结束时间',
  `crowd_group_time` float(10, 2) NULL DEFAULT NULL COMMENT '拼团的成团时间',
  `crowd_group_num` int(10) NULL DEFAULT NULL COMMENT '拼团的商品X人团',
  `coupon_id` bigint(20) NULL DEFAULT NULL COMMENT '拼团优惠券ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_shop_id`(`shop_id`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for P_PRODUCT_CATEGORY
-- ----------------------------
DROP TABLE IF EXISTS `P_PRODUCT_CATEGORY`;
CREATE TABLE `P_PRODUCT_CATEGORY`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父级ID',
  `pic` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片',
  `level` int(10) NULL DEFAULT NULL COMMENT '级别',
  `sort` int(10) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_shop_id`(`shop_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for P_PRODUCT_CLASSIFY
-- ----------------------------
DROP TABLE IF EXISTS `P_PRODUCT_CLASSIFY`;
CREATE TABLE `P_PRODUCT_CLASSIFY`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `name` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父级ID',
  `pic` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片',
  `level` int(10) NULL DEFAULT NULL COMMENT '级别',
  `sort` int(10) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for P_PRODUCT_DETAIL
-- ----------------------------
DROP TABLE IF EXISTS `P_PRODUCT_DETAIL`;
CREATE TABLE `P_PRODUCT_DETAIL`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for P_PRODUCT_SKU
-- ----------------------------
DROP TABLE IF EXISTS `P_PRODUCT_SKU`;
CREATE TABLE `P_PRODUCT_SKU`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `shop_id` bigint(20) NULL DEFAULT NULL COMMENT '店铺ID',
  `source_id` bigint(20) NULL DEFAULT NULL COMMENT '来源ID',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '商品ID',
  `standard` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '规格',
  `supply_price` decimal(16, 2) NULL DEFAULT NULL COMMENT '供货价',
  `sell_price` decimal(16, 2) NULL DEFAULT NULL COMMENT '销售价',
  `status` tinyint(2) NULL DEFAULT NULL COMMENT '状态 1,0',
  `virtual` tinyint(2) NULL DEFAULT NULL COMMENT '虚拟 1,0',
  `stock` int(10) NULL DEFAULT NULL COMMENT '库存',
  `warn_stock` int(10) NULL DEFAULT NULL COMMENT '预警库存',
  `external_sn` varchar(180) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '外部编码',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_id`(`product_id`) USING BTREE,
  INDEX `idx_stock`(`stock`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `BLOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `CRON_EXPRESSION` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ENTRY_ID` varchar(95) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `IS_DURABLE` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `STR_PROP_1` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `STR_PROP_2` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `STR_PROP_3` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `INT_PROP_1` int(11) NULL DEFAULT NULL,
  `INT_PROP_2` int(11) NULL DEFAULT NULL,
  `LONG_PROP_1` bigint(20) NULL DEFAULT NULL,
  `LONG_PROP_2` bigint(20) NULL DEFAULT NULL,
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for QRTZ_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
CREATE TABLE `QRTZ_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PRIORITY` int(11) NULL DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) NULL DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) NULL DEFAULT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `SCHED_NAME`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  CONSTRAINT `QRTZ_TRIGGERS_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for SYS_MENU
-- ----------------------------
DROP TABLE IF EXISTS `SYS_MENU`;
CREATE TABLE `SYS_MENU`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `name` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `path` varchar(180) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `route` varchar(180) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `logo` varchar(180) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `assembly` varchar(180) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `type` varchar(60) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `parent_id` bigint(20) NULL DEFAULT NULL,
  `sort` int(10) NULL DEFAULT NULL,
  `status` tinyint(2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique`(`path`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for SYS_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `SYS_ROLE`;
CREATE TABLE `SYS_ROLE`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `code` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `name` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for SYS_ROLE_MENU
-- ----------------------------
DROP TABLE IF EXISTS `SYS_ROLE_MENU`;
CREATE TABLE `SYS_ROLE_MENU`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `role_id` bigint(20) NULL DEFAULT NULL,
  `menu_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique`(`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for SYS_USER
-- ----------------------------
DROP TABLE IF EXISTS `SYS_USER`;
CREATE TABLE `SYS_USER`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `deleted` tinyint(2) NULL DEFAULT NULL,
  `username` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `password` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `status` tinyint(2) NULL DEFAULT NULL,
  `access_token` varchar(180) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `access_expired` datetime(0) NULL DEFAULT NULL,
  `register_ip` varchar(180) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `login_date` datetime(0) NULL DEFAULT NULL,
  `login_ip` varchar(180) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for SYS_USER_ROLE
-- ----------------------------
DROP TABLE IF EXISTS `SYS_USER_ROLE`;
CREATE TABLE `SYS_USER_ROLE`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `role_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique`(`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for V_MODULE
-- ----------------------------
DROP TABLE IF EXISTS `V_MODULE`;
CREATE TABLE `V_MODULE`  (
  `id` bigint(20) NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `create_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `modify_date` datetime(0) NULL DEFAULT NULL,
  `modify_man` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `value` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Procedure structure for UPPERCASE_TABLENAMES
-- ----------------------------
DROP PROCEDURE IF EXISTS `UPPERCASE_TABLENAMES`;
delimiter ;;
CREATE PROCEDURE `UPPERCASE_TABLENAMES`(IN dbname VARCHAR(200))
BEGIN
  DECLARE done INT DEFAULT 0;
  DECLARE oldname VARCHAR(200);
  DECLARE cur CURSOR FOR SELECT table_name FROM information_schema.TABLES WHERE table_schema = dbname;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
OPEN cur;
REPEAT
  FETCH cur INTO oldname;
  SET @newname = UPPER(oldname);
  SET @isNotSame = @newname <> BINARY oldname;
  IF NOT done && @isNotSame THEN
    SET @SQL = CONCAT('rename table `',oldname,'` to `', LOWER(@newname), '_tmp` ');
    PREPARE tmpstmt FROM @SQL;
    EXECUTE tmpstmt;
    SET @SQL = CONCAT('rename table `',LOWER(@newname),'_tmp` to `',@newname, '`');
    PREPARE tmpstmt FROM @SQL;
    EXECUTE tmpstmt;
    DEALLOCATE PREPARE tmpstmt;
  END IF;
UNTIL done END REPEAT;
CLOSE cur;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for UP_CHANGE_UTF8MB4
-- ----------------------------
DROP PROCEDURE IF EXISTS `UP_CHANGE_UTF8MB4`;
delimiter ;;
CREATE PROCEDURE `UP_CHANGE_UTF8MB4`()
BEGIN
  DECLARE $i INT;
  DECLARE $cnt INT;
  DECLARE $NAME VARCHAR(64);

  #创建临时表,代替游标
  DROP TABLE IF EXISTS tmp_Table_name;
  CREATE TEMPORARY TABLE tmp_Table_name (
    id INT NOT NULL AUTO_INCREMENT,
    table_name VARCHAR(64) NOT NULL,
    PRIMARY KEY (`id`)
  );

  #插入要处理的表名到临时表中
  INSERT INTO tmp_Table_name (table_name)
    SELECT
      table_name
    FROM information_schema.`TABLES`
    WHERE TABLE_TYPE = 'BASE TABLE'
    AND TABLE_SCHEMA = DATABASE();

  #循环处理每一张表,改表的字符集
  SET $i = 1;
  SELECT
    COUNT(1) INTO $cnt
  FROM tmp_Table_name;
  WHILE $i <= $cnt DO
    SELECT
      table_name INTO $NAME
    FROM tmp_Table_name
    WHERE id = $i;
    
    SET @asql = CONCAT('ALTER TABLE ', $NAME, '  CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci; ');
    PREPARE asql FROM @asql;
    EXECUTE asql;
    
    SET @asql = CONCAT('ALTER TABLE ', $NAME, ' CONVERT TO CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci; ');
    PREPARE asql FROM @asql;
    SELECT @asql;
    EXECUTE asql;

    SET $i = $i + 1;
  END WHILE;
  DEALLOCATE PREPARE asql;
  DROP TABLE tmp_Table_name;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
