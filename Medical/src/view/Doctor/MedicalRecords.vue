<template>
    <div class="medical-records">
        <div class="search-section">
            <div class="search-label">搜索条件：</div>
            <div class="search-input">
                <el-input v-model="searchText" placeholder="请输入病历ID" />
                <el-button type="primary" @click="handleSearch">搜索病历</el-button>
                <el-button type="primary" @click="openFillInDialog">填写病历</el-button>
            </div>
        </div>

        <el-table :data="tableData" border style="width: 100%">
            <el-table-column prop="MedicalRecordId" label="病历ID" width="120" />
            <el-table-column prop="patientName" label="患者姓名" width="120" />
            <el-table-column prop="hospitalName" label="医院名称" width="180" />
            <el-table-column prop="department" label="科室" width="120" />
            <el-table-column prop="doctorName" label="医生姓名" width="120" />
            <!-- <el-table-column prop="isFilled" label="状态" width="100">
                <template #default="scope">
                    <el-tag :type="scope.row.isFilled ? 'success' : 'info'">
                        {{ scope.row.isFilled ? '已完成' : '未完成' }}
                    </el-tag>
                </template>
            </el-table-column> -->
            <el-table-column label="操作" width="300" fixed="right">
                <template #default="scope">
                    <el-button type="success" size="small" @click="viewRecord(scope.row)">查看</el-button>
                    <!-- <el-button type="warning" size="small" @click="editRecord(scope.row)">编辑</el-button> -->
                    <!-- <el-button type="info" size="small" @click="openFillInDialog(scope.row)">填写病历</el-button> -->
                </template>
            </el-table-column>
        </el-table>

        <div class="pagination">
            <el-pagination
                v-model:current-page="currentPage"
                v-model:page-size="pageSize"
                :page-sizes="[10, 20, 30, 50]"
                :total="total"
                layout="total, sizes, prev, pager, next"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
            />
        </div>

        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="60%" @close="resetRecordForm">
            <el-form :model="recordForm" label-width="120px">
                <el-form-item label="患者姓名">
                    <el-input v-model="recordForm.patientName" :disabled="viewOnly" />
                </el-form-item>
                <el-form-item label="医院名称">
                    <el-input v-model="recordForm.hospitalName" :disabled="viewOnly" />
                </el-form-item>
                <el-form-item label="科室">
                    <el-input v-model="recordForm.department" :disabled="viewOnly" />
                </el-form-item>
                <el-form-item label="医生姓名">
                    <el-input v-model="recordForm.doctorName" :disabled="viewOnly" />
                </el-form-item>
                <el-form-item label="挂号信息">
                    <el-input v-model="recordForm.registrationInfo" type="textarea" :rows="2" :disabled="viewOnly" />
                </el-form-item>
                <el-form-item label="既往病史">
                    <el-input v-model="recordForm.pastMedicalHistory" type="textarea" :rows="3" :disabled="viewOnly" />
                </el-form-item>
                <el-form-item label="现病史">
                    <el-input v-model="recordForm.currentMedicalHistory" type="textarea" :rows="3" :disabled="viewOnly" />
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="saveRecord" v-if="!viewOnly">保存</el-button>
                </span>
            </template>
        </el-dialog>

        <el-dialog v-model="infoDialogVisible" title="病历详情" width="60%">
            <div>
                <p><strong>病历ID:</strong> {{ medicalRecordDetails.MedicalRecordId }}</p>
                <p><strong>患者姓名:</strong> {{ medicalRecordDetails.patientName }}</p>
                <p><strong>医院名称:</strong> {{ medicalRecordDetails.hospitalName }}</p>
                <p><strong>科室:</strong> {{ medicalRecordDetails.department }}</p>
                <p><strong>医生姓名:</strong> {{ medicalRecordDetails.doctorName }}</p>
                <p><strong>挂号信息:</strong> {{ medicalRecordDetails.registrationInfo }}</p>
                <p><strong>既往病史:</strong> {{ medicalRecordDetails.pastMedicalHistory }}</p>
                <p><strong>现病史:</strong> {{ medicalRecordDetails.currentMedicalHistory }}</p>
            </div>
            <template #footer>
                <el-button @click="infoDialogVisible = false">关闭</el-button>
            </template>
        </el-dialog>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onBeforeMount } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const searchText = ref('')
const dialogVisible = ref(false)
const infoDialogVisible = ref(false)
const dialogTitle = ref('')
const viewOnly = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])
const medicalRecordDetails = reactive({
    MedicalRecordId: '',
    patientName: '',
    hospitalName: '',
    department: '',
    doctorName: '',
    registrationInfo: '',
    pastMedicalHistory: '',
    currentMedicalHistory: ''
})

// Define recordForm as a reactive object
const recordForm = reactive({
    patientName: '',
    hospitalName: '',
    department: '',
    doctorName: '',
    registrationInfo: '',
    pastMedicalHistory: '',
    currentMedicalHistory: '',
    isFilled: false
});

const handleSearch = async () => {
    try {
        const response = await axios.get('/api/medicalRecord/getMedicalHistory', {
            params: {
                medicalRecordId: searchText.value,
                page: currentPage.value,
                pageSize: pageSize.value
            }
        })
        console.log(response.data)
        Object.assign(medicalRecordDetails, response.data);
        infoDialogVisible.value = true;
    } catch (error) {
        // ElMessage.error('搜索失败');
    }
}

const handleSizeChange = (val: number) => {
    pageSize.value = val
    handleSearch()
}

const handleCurrentChange = (val: number) => {
    currentPage.value = val
    handleSearch()
}

const startConsultation = async () => {
    try {
        await axios.post('/api/medicalRecord/startConsultation', {
            patientAddress: searchText.value
        })
        ElMessage.success('开始问诊')
        handleSearch()
    } catch (error) {
        ElMessage.error('操作失败')
    }
}

const endConsultation = async () => {
    try {
        await axios.post('/api/doctor/endConsultation', {
            patientAddress: searchText.value
        })
        ElMessage.success('问诊结束')
        handleSearch()
    } catch (error) {
        ElMessage.error('操作失败')
    }
}

const openFillInDialog = (row: any) => {
    Object.assign(recordForm, row)
    dialogTitle.value = '填写病历'
    viewOnly.value = false
    dialogVisible.value = true
}

const saveRecord = async () => {
    try {
        await axios.post('/api/medicalRecord/fillInMedicalHistory', {
            patientName: recordForm.patientName,
            hospitalName: recordForm.hospitalName,
            department: recordForm.department,
            doctorName: recordForm.doctorName,
            registrationInfo: recordForm.registrationInfo,
            pastMedicalHistory: recordForm.pastMedicalHistory,
            currentMedicalHistory: recordForm.currentMedicalHistory
        })
        
        ElMessage.success('病历填写成功')
        dialogVisible.value = false
        handleSearch()
        resetRecordForm(); // Clear the form after saving
    } catch (error) {
        ElMessage.error('填写病历失败')
    }
}

const viewRecord = (row: any) => {
    Object.assign(recordForm, row)
    dialogTitle.value = '病历详情'
    viewOnly.value = true
    dialogVisible.value = true
}

const editRecord = (row: any) => {
    Object.assign(recordForm, row)
    dialogTitle.value = '编辑病历'
    viewOnly.value = false
    dialogVisible.value = true
}

const getMedicalRecord = async () => {
    const doctorAddress = JSON.parse(localStorage.getItem('doctorinfo')!).accountAddress
    console.log(doctorAddress)
    try {
        const response = await axios.get('/api/medicalRecord/getMedicalHistoryByAll?doctorAddress=' +doctorAddress)
        if (response.status === 200) {
            const data = response.data;
            const parsedData = data.map((item: any) => {
                try {
                    return JSON.parse(item);
                } catch (error) {
                    console.error("Error parsing item:", item, error);
                    return {};
                }
            });

            tableData.value = parsedData;
            
            total.value = response.data.total || parsedData.length;
        } else {
            ElMessage.error('获取预约列表失败');
        }
    } catch (error) {
        console.error(error);
        ElMessage.error('获取预约列表失败');
    }
}

const resetRecordForm = () => {
    recordForm.patientName = '';
    recordForm.hospitalName = '';
    recordForm.department = '';
    recordForm.doctorName = '';
    recordForm.registrationInfo = '';
    recordForm.pastMedicalHistory = '';
    recordForm.currentMedicalHistory = '';
    recordForm.isFilled = false;
}

onBeforeMount(() => {
    getMedicalRecord()
})
// 初始加载
// handleSearch()
</script>

<style scoped>
.medical-records {
    padding: 20px;
}

.search-section {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
}

.search-label {
    margin-right: 10px;
    font-weight: bold;
}

.search-input {
    display: flex;
    gap: 10px;
}

.pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
}
</style>