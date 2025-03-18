/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 80037
Source Host           : localhost:3306
Source Database       : medical

Target Server Type    : MYSQL
Target Server Version : 80037
File Encoding         : 65001

Date: 2024-12-29 16:49:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for doctor
-- ----------------------------
DROP TABLE IF EXISTS `doctor`;
CREATE TABLE `doctor` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `accountAddress` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for medical_heath_case
-- ----------------------------
DROP TABLE IF EXISTS `medical_heath_case`;
CREATE TABLE `medical_heath_case` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `case_number` varchar(100) NOT NULL COMMENT '病历编号',
  `name` varchar(100) NOT NULL COMMENT '用户姓名',
  `sex` varchar(10) NOT NULL COMMENT '性别',
  `age` varchar(10) NOT NULL COMMENT '年龄',
  `category` varchar(50) NOT NULL COMMENT '科别',
  `time` varchar(50) NOT NULL COMMENT '时间',
  `create_time` varchar(30) NOT NULL COMMENT '创建时间',
  `update_time` varchar(30) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Table structure for medical_record
-- ----------------------------
DROP TABLE IF EXISTS `medical_record`;
CREATE TABLE `medical_record` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `patientName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `hospitalName` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `department` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `doctorName` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `registrationInfo` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `pastMedicalHistory` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `currentMedicalHistory` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for patient
-- ----------------------------
DROP TABLE IF EXISTS `patient`;
CREATE TABLE `patient` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `accountAddress` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=97696 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
