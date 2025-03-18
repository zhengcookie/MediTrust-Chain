<template>
    <div class="login">
        <div class="is-login">   
            <h3 class="title">区块链医疗健康管理系统</h3>
            <!-- 使用 ref 获取表单实例 -->
            <el-form ref="formRef" :model="loginItem" :rules="rules" @submit.prevent="onLogin" style="max-width: 400px" label-width="auto" class="login-form">
                <el-form-item label="登录名称" prop="username">
                    <el-input style="width: 300px" type="text" class="form-control" id="username" placeholder="请输入登录名称"
                        autocomplete="off" v-model.trim="loginItem.username">
                    </el-input>
                </el-form-item>

                <el-form-item label="登录密码" prop="password">
                    <el-input style="width: 300px" type="password" class="form-control" id="password"
                        placeholder="请输入登录密码" autocomplete="off" v-model.trim="loginItem.password">
                    </el-input>
                </el-form-item>

                <el-form-item label="用户类型" prop="userType">
                    <el-radio-group v-model="loginItem.userType">
                        <el-radio label="doctor">医生</el-radio>
                        <el-radio label="patient">病人</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <!-- 登录按钮 -->
            <div class="form-group">
                <!-- 点击时触发表单验证 -->
                <el-button type="primary" class="login-btn" native-type="submit" @click="onLogin" :loading="loading">登录</el-button>
                <el-button class="reset-btn" @click="clearLogin">重置</el-button>
            </div>
            <div class="check">
               <p><router-link to="/register" class="register-link">立即注册</router-link></p>
                <p class="forgot-password">忘记密码？</p>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { FormInstance } from 'element-plus';  // 引入FormInstance类型
import axios from "axios";
import { useRouter } from "vue-router";

// 响应式对象，存储登录信息
const loginItem = reactive({
    username: "",
    password: "",
    userType: "patient" // 默认为病人登录
});

// 表单验证规则
const rules = {
    username: [
        { required: true, message: '请输入登录名称', trigger: 'blur' },
        { min: 1, max: 10, message: '用户名长度应在 3 到 10 个字符之间', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入登录密码', trigger: 'blur' },
        { min: 6, max: 12, message: '密码长度应在 6 到 12 个字符之间', trigger: 'blur' }
    ],
    userType: [
        { required: true, message: '请选择用户类型', trigger: 'change' }
    ]
};

// 使用ref创建表单实例
const formRef = ref<FormInstance | null>(null);

// 懒加载路由
const router = useRouter();

// loading状态
const loading = ref(false);

// 登录逻辑
const onLogin = () => {
    // 获取表单实例
    if (!formRef.value) {
        console.log('formRef is null');
        return;  // 确保formRef有效
    }

    // 执行表单验证
    formRef.value.validate((valid) => {
        if (valid) {
            // 设置loading状态为true
            loading.value = true;

            // 根据用户类型选择不同的API接口
            const apiUrl = loginItem.userType === 'doctor' ? '/api/doctor/login' : '/api/patient/login';

            // 发送登录请求
            axios.get(apiUrl, {
                params: loginItem
            }).then((res) => {
                setTimeout(() => {
                    if (res.data.code === -1) {
                        alert("用户不存在！");
                    } else if (res.data.code === -2) {
                        alert("密码错误");
                    } else {
                        // 根据用户类型存储信息和跳转
                        if (loginItem.userType === 'doctor') {
                            localStorage.setItem("doctorinfo", JSON.stringify(res.data.msg));
                            router.push("/doctor");
                        } else {
                            localStorage.setItem("patientinfo", JSON.stringify(res.data.msg));
                            router.push("/patient");
                        }
                    }
                    console.log(res.data.msg);
                    loading.value = false;
                }, 1000);
            }).catch((error) => {
                console.error(error);
                loading.value = false;
            });
        } else {
            console.log('表单验证失败');
        }
    });
};

// 重置登录信息
const clearLogin = () => {
    loginItem.username = "";
    loginItem.password = "";
    loginItem.userType = "patient";
};
</script>

<style scoped>
.login {
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background: url("../assets/images/注册页面/u18.jpg") center/cover no-repeat;
  display: flex;
  justify-content: center;
  align-items: center;
}

.is-login {
    box-sizing: border-box;
    background: rgba(255, 255, 255, 0.95);
    width: 550px;
    min-height: 400px;
    display: flex;
    flex-flow: column;
    justify-content: space-around;
    align-items: center;
    padding: 40px;
    border-radius: 20px;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.title {
    color: #2c3e50;
    font-size: 28px;
    margin-bottom: 30px;
    font-weight: 600;
}

.login-form {
    width: 100%;
    margin: 20px 0;
}

.form-group {
    margin: 25px 0;
}

.login-btn {
    width: 120px;
    height: 40px;
    margin-right: 15px;
    font-size: 16px;
}

.reset-btn {
    width: 120px;
    height: 40px;
    font-size: 16px;
}

.check {
    width: 300px;
    display: flex;
    justify-content: space-between;
    font-size: 14px;
    margin-top: 20px;
}

.register-link {
    text-decoration: none;
    color: #409EFF;
    transition: color 0.3s ease;
}

.register-link:hover {
    color: #66b1ff;
}

.forgot-password {
    color: #909399;
    cursor: pointer;
    transition: color 0.3s ease;
}

.forgot-password:hover {
    color: #606266;
}

.is-login :deep(.el-input__wrapper) {
    box-shadow: 0 0 0 1px #dcdfe6 inset;
}

.is-login :deep(.el-input__wrapper:hover) {
    box-shadow: 0 0 0 1px #c0c4cc inset;
}

.is-login :deep(.el-input__wrapper.is-focus) {
    box-shadow: 0 0 0 1px #409EFF inset;
}
</style>
