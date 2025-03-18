<template>
    <div class="appointment-management">
        <div class="search-section">
            <div class="search-label">搜索条件：</div>
            <div class="search-input">
                <el-input placeholder="请输入预约编号" v-model="appointmentId"/>
                <el-button type="primary" @click="handleSearch">搜索预约</el-button>
            </div>
        </div>
        <el-table :data="tableData" border style="min-width: 100%">
            <el-table-column prop="patientName" label="患者姓名" width="120" />地址" width="180" />
            <el-table-column fixed prop="appointmentId" label="预约编号" width="180" />
            <el-table-column prop="department" label="科室" width="120" />
            <el-table-column prop="doctorName" label="医生姓名" width="180" />
            <el-table-column prop="remark" label="备注" width="180"/>
            <el-table-column prop="appointmentTime" label="预约时间" width="180"/>
            <el-table-column prop="appointStatus" label="预约状态">
                <template #default="scope">
                    <el-tag :type="scope.row.appointStatus ? 'success' : 'warning'">
                        {{ scope.row.appointStatus ? '已完成' : '未完成' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="180">
                <template #default="{ row }">
                    <el-button type="primary" @click="completeAppointment(row)">完成</el-button>
                    <!-- <el-button type="danger" @click="handleDelete(row)">删除</el-button> -->
                </template>
            </el-table-column>
        </el-table>
        <div class="pagination">
            <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize"
                :page-sizes="[10, 20, 30, 50]" :total="total" layout="total, sizes, prev, pager, next"
                @size-change="handleSizeChange" @current-change="handleCurrentChange" />
        </div>

        <el-dialog v-model="dialogVisible" :title="dialogTitle" width="50%">
            <el-descriptions :column="2" border>
                <el-descriptions-item label="患者姓名">
                    <el-input v-model="appointmentForm.patientName" :disabled="viewOnly" />
                </el-descriptions-item>
                <el-descriptions-item label="预约科室">
                    <el-input v-model="appointmentForm.department" :disabled="viewOnly" />
                </el-descriptions-item>
                <el-descriptions-item label="预约时间">
                    <el-input v-model="appointmentForm.createTime" :disabled="viewOnly" />
                </el-descriptions-item>
                <el-descriptions-item label="状态">{{ appointmentForm.appointStatus == "true" ? '已完成' : '未完成' }}</el-descriptions-item>
                <el-descriptions-item label="备注" :span="2">
                    <el-input v-model="appointmentForm.remark" type="textarea" :rows="3" :disabled="viewOnly" />
                </el-descriptions-item>
            </el-descriptions>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogVisible = false">关闭</el-button>
                    <el-button type="primary" @click="saveAppointment" v-if="!viewOnly">保存</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const dialogVisible = ref(false)
const dialogTitle = ref('预约详情')
const viewOnly = ref(true)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])

const appointmentForm = reactive({
    patientName: '',
    appointmentId: '',
    department: '',
    doctorName: '',
    createTime: '',
    appointStatus: false,
    remark: ''
})
const appointmentId = ref();

const handleSearch = async () => {
    if (!appointmentId.value) {
        ElMessage.error('请输入预约编号');
        return;
    }
    try {
        const response = await axios.get(`/api/patient/getAppointmentById?id=${appointmentId.value}`);
        console.log(response.data);
        if (response.data) {
            appointmentForm.patientName = response.data.patientName;
            appointmentForm.appointmentId = response.data.appointmentId;
            appointmentForm.department = response.data.department;
            appointmentForm.doctorName = response.data.doctorName;
            appointmentForm.createTime = response.data.createTime;
            appointmentForm.appointStatus = response.data.appointStatus;
            appointmentForm.remark = response.data.remark;
            dialogVisible.value = true;
        } else {
            ElMessage.error('未找到该预约编号');
        }
    } catch (error) {
        ElMessage.error('搜索失败');
    }
}

const saveAppointment = async () => {
    try {
        await axios.post(`/api/doctor/fillInAppointment?appointmentId=${appointmentId.value}`);
        ElMessage.success('预约保存成功');
        dialogVisible.value = false;
        getAppointmentInfoByDoctor();
    } catch (error) {
        ElMessage.error('保存预约失败');
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

const completeAppointment = (row: any) => {
    ElMessageBox.confirm('确认完成此预约?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(async () => {
        try {
            await axios.post(`/api/doctor/fillInAppointment?appointmentId=${row.appointmentId}`)
            ElMessage.success('操作成功')
            getAppointmentInfoByDoctor()
        } catch (error) {
            ElMessage.error('操作失败')
        }
    })
}

const doctor = JSON.parse(localStorage.getItem('doctorinfo') || '{}').accountAddress;

const formatDateTime = (dateTimeStr: string) => {
    const date = new Date(dateTimeStr)
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hours = String(date.getHours()).padStart(2, '0')
    const minutes = String(date.getMinutes()).padStart(2, '0')
    const seconds = String(date.getSeconds()).padStart(2, '0')
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

const getAppointmentInfoByDoctor = async () => {
    try {
        const response = await axios.get('/api/doctor/getAppointmentInfoByDoctor', {
            params: {
                doctorAddress: doctor
            }
        })
        if (response.status === 200) {
            const data = JSON.parse(response.data);
            const parsedData = data.map((item: any) => ({
                patientName: item[0],
                appointmentId: item[1],
                department: item[2],
                doctorName: item[3],
                remark: item[4] || '',
                appointmentTime: item[5],
                appointStatus: item[6],
            }));
            tableData.value = parsedData;
            total.value = parsedData.length;
        }
    } catch (error) {
        ElMessage.error('获取预约信息失败')
    }
}

onMounted(() => {
    getAppointmentInfoByDoctor()
})
</script>

<style scoped>
.appointment-management {
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
