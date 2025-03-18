<template>
    <div class="personal-info">
        <el-card>
            <template #header>
                <div class="info-header">
                    <span class="header-text">基本信息</span>
                </div>
            </template>
            <el-descriptions :column="1" border>
                <!-- <el-descriptions-item label="账户ID">
                    {{ PersonalInformation.id }}
                </el-descriptions-item> -->
                <el-descriptions-item label="性别">
                    {{ PersonalInformation.gender }}
                </el-descriptions-item>
                <el-descriptions-item label="姓名">
                    {{ PersonalInformation.username }}
                </el-descriptions-item>
                <el-descriptions-item label="年龄">
                    {{ PersonalInformation.age }}
                </el-descriptions-item>
                <el-descriptions-item label="地址">
                    {{ PersonalInformation.accountAddress }}
                </el-descriptions-item>
                
            </el-descriptions>
        </el-card>
    </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref} from 'vue';

import axios from 'axios';
const patientId = ref('');
const PersonalInformation = reactive({
    username: '',
    gender: '',
    age: '',
    accountAddress: ''
});
onMounted(() => {
    // 获取用户信息
    patientId.value = JSON.parse(localStorage.getItem("patientinfo") || "{}").accountAddress;
    // 获取患者信息
    axios.get('/api/patient/getPatientInfo',{params:{address:patientId.value}}).then((res) => {
        console.log(res.data);
        PersonalInformation.username = res.data[0];
        PersonalInformation.gender = res.data[1];
        PersonalInformation.age = res.data[2];
        PersonalInformation.accountAddress = res.data[3];
        console.log(PersonalInformation);
    });
});
</script>

<style scoped>
.personal-info {
    width: 100%;
}

.info-header {
    display: flex;
    align-items: center;
}

.header-text {
    font-size: 18px;
    font-weight: 600;
    color: rgba(0,0,0,0.85);
}

:deep(.el-descriptions__label) {
    min-width: 90px;
    color: rgba(0,0,0,0.85);
    font-size: 18px;
}

:deep(.el-descriptions__content) {
    color: rgba(0,0,0,0.65);
    font-size: 18px;
}
</style>