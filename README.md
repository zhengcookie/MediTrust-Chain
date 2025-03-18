# **区块链医疗健康管理系统**

> 基于SpringBoot+Vue+FISCO-BCOS的医疗健康管理平台

## 🌟 简介
本项目是基于区块链技术的医疗健康管理系统，通过FISCO-BCOS联盟链实现医疗数据的安全存证与可信共享。系统包含：
- 👨⚕️ **医生端**：患者管理、电子病历存证、预约审核
- 👨💻 **患者端**：在线挂号、挂号管理，预约管理。
- 🔒 **区块链**：关键医疗数据上链存证，保障数据不可篡改

## 🛠️ 安装指南

### 环境要求
- JDK 1.8+/11
- Node.js 18+
- MySQL 8.0.15
- FISCO-BCOS节点（[搭建指南](https://fisco-bcos-documentation.readthedocs.io/)）

# **区块链医疗健康管理系统**

> 基于SpringBoot+Vue+FISCO-BCOS的医疗健康管理平台

## 🌟 简介
本项目是基于区块链技术的医疗健康管理系统，通过FISCO-BCOS联盟链实现医疗数据的安全存证与可信共享。系统包含：
- 👨⚕️ **医生端**：患者管理、电子病历存证、预约审核
- 👨💻 **患者端**：在线挂号、预约查询、病历查看
- 🔒 **区块链**：关键医疗数据上链存证，保障数据不可篡改

## 🛠️ 安装指南

### 环境要求
- JDK 1.8+
- Node.js 14+
- MySQL 5.7+
- FISCO-BCOS节点（[搭建指南](https://fisco-bcos-documentation.readthedocs.io/)）

### 后端部署
```bash
cd MedicalBackend/backend
mvn clean install
```

### 前端部署

```
nvm use 18
cd medical
npm install
# 配置vite.config.ts中的API地址
npm run dev
```

## 🖥️ 使用说明

### 医生界面

| 功能模块     | 说明                      |
| :----------- | :------------------------ |
| 首页         | 显示当日预约/患者人数统计 |
| 个人信息     | 医生信息                  |
| 患者管理     | 查看/搜索患者基本信息     |
| 预约审核     | 审批患者预约申请          |
| 电子病历管理 | 创建病历并生成区块链存证  |

### 患者界面

| 功能模块 | 说明                     |
| :------- | :----------------------- |
| 个人信息 | 患者信息                 |
| 挂号管理 | 挂号预约                 |
| 预约管理 | 查看个人预约信息         |
| 我的病历 | 查看区块链存证的加密病历 |

## 💻 维护者

- [zhengcookie] - 项目架构师（[z2132085753@outlook.com]）

##  版本历史

- 1.0.0 (2023-08-20)
  - 实现核心医疗存证功能
  - 完成医患双端基础功能模块
  - 集成FISCO-BCOS存证SDK

## 📄 许可证

本项目采用 [MIT License](https://license/)

##  致谢

- FISCO-BCOS开源社区提供区块链技术支持

## 🖼️ 