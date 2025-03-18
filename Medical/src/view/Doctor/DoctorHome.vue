<template>
  <div id="base">
    <!-- 顶部导航栏 -->
    <div class="header">
      <div class="logo">
        <img src="../../assets/images/首页/u12.png" alt="logo" />
        <h1>区块链医疗健康管理平台</h1>
      </div>
      <div class="user-info">
        <img src="../../assets/images/主页面/u125.png" alt="notification" />
        <img src="../../assets/images/主页面/u123.png" alt="message" />
        <img src="../../assets/images/主页面/u119.jpg" alt="avatar" class="avatar" />
        <div class="user-dropdown" @click="toggleDropdown">
          <span>{{ username }}</span>
          <el-button type="info" @click="logout" style="margin-left: 10px">退出</el-button>
        </div>
      </div>
    </div>

    <!-- 侧边导航栏 -->
    <div class="sidebar">
      <div class="menu-item" @click="router.push('/doctor/index')">
        <img src="../../assets/images/首页/u16.svg" alt="首页" />
        <router-link to="/doctor/index">首页</router-link>
      </div>
      <div class="menu-item" @click="router.push('/doctor/info')">
        <img src="../../assets/images/主页面/u152.svg" alt="个人信息" />
        <router-link to="/doctor/Info">个人信息</router-link>
      </div>

      <div class="menu-item" @click="router.push('/doctor/patientManagement')">
        <img src="../../assets/images/主页面/u159.svg" alt="患者管理" />
        <router-link to="/doctor/patientManagement">患者管理</router-link>
      </div>

      <div class="menu-item" @click="router.push('/doctor/appointmentManagement')">
        <img src="../../assets/images/主页面/u164.svg" alt="预约管理" />
        <router-link to="/doctor/appointmentManagement">预约管理</router-link>
      </div>

      <div class="menu-item" @click="router.push('/doctor/records')">
        <img src="../../assets/images/主页面/u171.svg" alt="病历管理" />
        <router-link to="/doctor/records">病历管理</router-link>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <router-view></router-view>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import { ref, onMounted } from 'vue';

const router = useRouter();
const isDropdownOpen = ref(false);

const username = JSON.parse(localStorage.getItem("doctorinfo") || '{"username":"未登录"}').username;

// 在组件挂载时检查用户是否登录
onMounted(() => {
  const doctorInfo = localStorage.getItem("doctorinfo");
  if (!doctorInfo || JSON.parse(doctorInfo).username === "未登录") {
    router.push("/login");
  }
});

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value;
};

// 退出登录
const logout = () => {
  localStorage.removeItem("doctorinfo");
  router.push("/login");
};
</script>

<style scoped>
#base {
  min-height: 100vh;
  background-color: #f5f7fa;
  display: flex;
}

/* 顶部导航栏样式 */
.rotate {
  transform: rotate(180deg);
  transition: transform 0.3s;
}

.header {
  position: fixed;
  top: 0;
  left: 80px;
  right: 0;
  height: 70px;
  background: #ffffff;
  padding: 0 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  z-index: 1000;
  transition: all 0.3s ease;
}

.logo {
  display: flex;
  align-items: center;
  gap: 20px;
}

.logo img {
  width: 60px;
  height: 60px;
  transition: transform 0.3s ease;
}

.logo img:hover {
  transform: scale(1.05);
}

.logo h1 {
  font-size: 30px;
  font-weight: 700;
  color: #4a9e5c;
  margin: 0;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 28px;
}

.user-info img {
  width: 26px;
  height: 26px;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.user-info img:hover {
  transform: scale(1.1);
}

.user-info .avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 2px solid #4a9e5c;
}

.user-dropdown {
  position: relative;
  cursor: pointer;
  padding: 5px 10px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 5px 0;
  min-width: 100px;
}

.dropdown-item {
  padding: 8px 16px;
  color: #333;
  transition: background 0.3s;
}

.dropdown-item:hover {
  background: #f5f7fa;
}

/* 侧边导航栏样式 */
.sidebar {
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  width: 80px;
  background: linear-gradient(180deg, #4a9e5c 0%, #3d8a4e 100%);
  padding-top: 70px;
  box-shadow: 2px 0 8px rgba(0, 21, 41, 0.15);
  z-index: 999;
  transition: all 0.3s ease;
}

.menu-item {
  padding: 20px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.menu-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.menu-item img {
  width: 36px;
  height: 36px;
  transition: transform 0.3s ease;
}

.menu-item:hover img {
  transform: scale(1.1);
}

.menu-item a,
.menu-item span {
  font-size: 13px;
  color: #ffffff;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.menu-item:hover a {
  color: #e6f3e8;
}

/* 主要内容区域样式 */
.main-content {
  margin-left: 80px;
  margin-top: 70px;
  padding: 30px;
  flex: 1;
  background-color: #f5f7fa;
  min-height: calc(100vh - 70px);
}
</style>