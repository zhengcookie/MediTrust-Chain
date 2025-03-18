<template>
    <div class="register">
        <div class="is-register">
            <h3 class="title">区块链医疗健康管理系统</h3>
            <el-form ref="formRef" :model="registerItem" :rules="rules" @submit.prevent="onRegister" style="max-width: 400px" label-width="auto" class="register-form">
                <el-form-item label="注册名称" prop="username">
                    <el-input style="width: 300px" type="text" class="form-control" id="username" placeholder="请输入注册名称"
                        autocomplete="off" v-model.trim="registerItem.username">
                    </el-input>
                </el-form-item>
                <el-form-item label="区块链地址" prop="accountAddress">
                    <el-input style="width: 300px" type="text" class="form-control" id="address"
                        placeholder="请输入区块链地址" autocomplete="off" v-model.trim="registerItem.accountAddress">
                    </el-input>
                </el-form-item>
                <el-form-item label="注册密码" prop="password">
                    <el-input style="width: 300px" type="password" class="form-control" id="password"
                        placeholder="请输入注册密码" autocomplete="off" v-model.trim="registerItem.password">
                    </el-input>
                </el-form-item>
                <el-form-item label="年龄" prop="age">
                    <el-input-number v-model="registerItem.age" :min="1" :max="100" />
                </el-form-item>
                <el-form-item label="性别" prop="gender">
                    <el-radio-group v-model="sex">
                        <el-radio :value="1" size="small">男</el-radio>
                        <el-radio :value="2" size="small">女</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="用户类型" prop="userType">
                    <el-radio-group v-model="registerItem.userType">
                        <el-radio :value="1" size="small">医生</el-radio>
                        <el-radio :value="2" size="small">病人</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <div class="form-group">
                <el-button type="primary" class="register-btn" @click="onRegister" native-type="submit" :loading="loading">注册</el-button>
                <el-button class="reset-btn" @click="clearRegister">重置</el-button>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue"
import type { FormInstance } from "element-plus";
import axios from "axios";
import router from "../router";

const formRef = ref<FormInstance | null>(null);
const sex = ref(1);
const loading = ref(false);

const rules = {
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 1, max: 10, message: '用户名长度应在3到10个字符之间', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 12, message: '密码长度应在6到12个字符之间', trigger: 'blur' }
    ],
    accountAddress: [
        { required: true, message: '请输入区块链地址', trigger: 'blur' },
        { pattern: /^0x[a-fA-F0-9]{40}$/, message: '请输入有效的区块链地址', trigger: 'blur' }
    ],
    age: [
        { required: true, message: '请输入年龄', trigger: 'blur' }
    ],
    userType: [
        { required: true, message: '请选择用户类型', trigger: 'change' }
    ]
}

const registerItem = reactive({
    password: "",
    gender: "男",
    accountAddress: "",
    age: 0,
    username: "",
    userType: 2 // 默认选择病人
})

const onRegister = () => {
    if (sex.value == 1) registerItem.gender = "男"
    else registerItem.gender = "女"
    console.log(registerItem)
    if (!formRef.value) {
        console.log('formRef is null');
        return;
    }
    formRef.value.validate((valid) => {
        if (valid) {
            loading.value = true;
            const apiPath = registerItem.userType === 1 ? '/api/doctor/register' : '/api/patient/register';
            axios.post(apiPath, registerItem).then((res) => {
                setTimeout(() => {
                    res.data.code == -1 ? alert("用户已存在！") :
                    res.data.code == -2 ? alert("数据异常") :
                    router.push("/login")
                    loading.value = false;
                }, 1000);
            }).catch((error) => {
                console.log(error)
                loading.value = false;
            });
        }
    })
}

const clearRegister = () => {
    registerItem.username = "",
    registerItem.gender = "男",
    registerItem.age = 0,
    registerItem.accountAddress = '',
    registerItem.password = '',
    registerItem.userType = 2
}
</script>

<style scoped>
.register {
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

.is-register {
    box-sizing: border-box;
    background: rgba(255, 255, 255, 0.95);
    width: 550px;
    min-height: 450px;
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

.register-form {
    width: 100%;
    margin-bottom: 20px;
}

.form-group {
    display: flex;
    gap: 10px;
    margin-top: 10px;
}

.register-btn, .reset-btn {
    min-width: 100px;
    height: 40px;
    font-size: 16px;
    border-radius: 8px;
    transition: all 0.3s ease;
}

.register-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
</style>