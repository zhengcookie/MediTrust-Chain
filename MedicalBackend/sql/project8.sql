/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50735
 Source Host           : localhost:3306
 Source Schema         : medical

 Target Server Type    : MySQL
 Target Server Version : 50735
 File Encoding         : 65001

 Date: 30/04/2023 11:44:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for medical_heath_case
-- ----------------------------
DROP TABLE IF EXISTS `medical_heath_case`;
CREATE TABLE `medical_heath_case` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',

   -- TODO:在次进行建表 sql 语句补全或使用工具创建表字段
  `case_number` VARCHAR(100) NOT NULL COMMENT '病历编号',
  `name` VARCHAR(100) NOT NULL COMMENT '用户姓名',
  `sex` VARCHAR(10) NOT NULL COMMENT '性别',
  `age` VARCHAR(10) NOT NULL COMMENT '年龄',
  `category` VARCHAR(50) NOT NULL COMMENT '科别',
  `time` VARCHAR(50) NOT NULL COMMENT '时间',
  `create_time` varchar(30) NOT NULL COMMENT '创建时间',
  `update_time` varchar(30) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for medical_record
-- ----------------------------
DROP TABLE IF EXISTS `medical_record`;
CREATE TABLE `medical_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

SET FOREIGN_KEY_CHECKS = 1;
