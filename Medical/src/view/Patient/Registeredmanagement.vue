<template>
  <div class="registration-form">
    <!-- 挂号预约标题 -->
    <div class="title-section">
      <div class="title-icon">
        <i class="el-icon-edit-outline"></i>
      </div>
      <h2>挂号预约</h2>
    </div>

    <!-- 挂号表单 -->
    <el-form :model="formData" ref="formRef" label-width="120px" class="form-container">
      <el-card class="form-card" shadow="hover">
        <div class="form-row">
          <el-form-item label="挂号科室：" required>
            <el-select v-model="formData.department" placeholder="请选择科室" class="form-select">
              <el-option v-for="dept in departments" 
                :key="dept"
                :label="dept"
                :value="dept">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="挂号医生：" required>
            <el-select v-model="formData.doctorName" placeholder="请选择医生" class="form-select">
              <el-option v-for="doc in doctorName"
                :key="doc"
                :label="doc"
                :value="doc">
              </el-option>
            </el-select>
          </el-form-item>
         
        </div>

        <div class="form-row">
          <el-form-item label="挂号时间：" required>
            <el-date-picker
              v-model="formData.createTime"
              type="datetime"
              placeholder="选择日期时间"
              class="form-date-picker"
              :disabled-date="disabledDate"
              :disabled-time="disabledTime">
            </el-date-picker>
          </el-form-item>

          <el-form-item label="备注：">
            <el-input 
              v-model="formData.remark" 
              type="textarea" 
              placeholder="请输入备注信息（选填）"
              :rows="3"
              class="form-textarea">
            </el-input>
          </el-form-item>
        </div>

        <!-- 挂号按钮 -->
        <div class="register-btn">
          <el-button type="primary" size="large" @click="handleRegister">
            <i class="el-icon-check"></i>
            确认挂号
          </el-button>
        </div>
      </el-card>
    </el-form>

    <!-- 预约信息确认对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="预约信息确认"
      width="50%"
      :before-close="handleClose"
    >
      <el-descriptions :column="1" border>
        <el-descriptions-item label="病人地址">{{ formData.patientName }}</el-descriptions-item>
        <el-descriptions-item label="科室">{{ formData.department }}</el-descriptions-item>
        <el-descriptions-item label="医生">{{ formData.doctorName }}</el-descriptions-item>
       
        <el-descriptions-item label="预约时间">{{ formData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ formData.remark || '无' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmAppointment">确认预约</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import type { FormInstance } from 'element-plus'
import axios from 'axios';
import { ElMessage } from 'element-plus'

const formRef = ref<FormInstance>()
const dialogVisible = ref(false)
const patientinfo = ref();
patientinfo.value = JSON.parse(localStorage.getItem("patientinfo") || "{}");
console.log(patientinfo.value.accountAddress)

const departments = [
  '神经内科',
  '眼科', 
  '神经外科',
  '骨科',
  '内分泌科',
  '皮肤科',
  '头颈外科'
]

const doctorName = [
  '张天成',
  '黄炳成',
  '廖华凯',
  '罗雪阳'
]

const formData = reactive({
  patientName:patientinfo.value.username,
  department: '',
  doctorName: '',
  createTime: '',
  remark: '',
  appointStatus: false
})

// 禁用当前时间之前的日期
const disabledDate = (time: Date) => {
  return time.getTime() < Date.now() - 8.64e7 // 禁用今天之前的日期
}

// 禁用当前时间之前的时间
const disabledTime = (date: Date) => {
  const now = new Date()
  if (date.getDate() === now.getDate() && 
      date.getMonth() === now.getMonth() && 
      date.getFullYear() === now.getFullYear()) {
    return {
      hours: Array.from({length: now.getHours()}, (_, i) => i),
      minutes: date.getHours() === now.getHours() ? Array.from({length: now.getMinutes()}, (_, i) => i) : []
    }
  }
  return {
    hours: [],
    minutes: []
  }
}

const handleClose = () => {
  dialogVisible.value = false
}

const handleRegister = async () => {
  // 检查必填字段是否已填写
  if (!formData.department || !formData.doctorName || !formData.createTime) {
    ElMessage.warning('请选择预约信息')
    return
  }
  dialogVisible.value = true
}

const confirmAppointment = async () => {
  
  if (!formRef.value) return
  await formRef.value.validate((valid) => {
    if (valid) {
      axios.post("/api/patient/makeAppointment",formData).then((res)=>{
        console.log(res.data)
        if (res.data.code == -2){
          ElMessage.error("预约失败！")
        }
        if (res.data.code == 0){
          ElMessage.success("预约成功")
          dialogVisible.value = false
        }
      })
    }
  })
}
</script>

<style scoped>
.registration-form {
  padding: 30px;
  background: #ffffff;
  min-height: 100vh;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  border-radius: 8px;
}

.title-section {
  display: flex;
  align-items: center;
  margin-bottom: 40px;
  padding: 0 20px;
  border-bottom: 2px solid #ebeef5;
  padding-bottom: 20px;
}

.title-section h2 {
  color: #303133;
  margin: 0;
  font-weight: 600;
  font-size: 24px;
}

.title-icon {
  margin-right: 15px;
  font-size: 28px;
  color: #409EFF;
}

.form-card {
  background: #fff;
  border-radius: 8px;
  padding: 40px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
}

.form-container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
  margin-bottom: 30px;
}

.form-row :deep(.el-form-item) {
  width: 100%;
  margin-bottom: 20px;
}

.form-select,
.form-date-picker,
.form-textarea {
  width: 100%;
}

.form-textarea :deep(.el-textarea__inner) {
  min-height: 120px;
  resize: vertical;
}

.register-btn {
  text-align: center;
  margin-top: 50px;
}

.register-btn :deep(.el-button) {
  padding: 15px 50px;
  font-size: 16px;
  border-radius: 25px;
  transition: all 0.3s ease;
}

.register-btn :deep(.el-button:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64,158,255,0.4);
}

:deep(.el-input__inner) {
  height: 45px;
  line-height: 45px;
  border-radius: 8px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
  font-size: 15px;
}

.dialog-footer {
  margin-top: 20px;
  text-align: right;
}

:deep(.el-dialog) {
  border-radius: 12px;
}

:deep(.el-dialog__header) {
  padding: 20px;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-dialog__body) {
  padding: 30px;
}

:deep(.el-descriptions) {
  padding: 20px;
  border-radius: 8px;
  background: #f8f9fa;
}
</style>