<template>
    <div class="record-management">
        <div class="search-section">
            <div class="search-label">搜索条件：</div>
            <div class="search-input">
                <el-input v-model="searchText" placeholder="请输入预约编号" />
                <el-button type="primary" @click="handleSearch">搜索预约</el-button>
            </div>
        </div>
        <el-table :data="tableData" border style="width: 100%">
            <el-table-column prop="appointmentId" label="预约编号" />
            <el-table-column prop="patientName" label="患者姓名" />
            <el-table-column prop="createTime" label="预约时间" />
            <el-table-column prop="doctorName" label="医生姓名" />
            <el-table-column prop="department" label="预约科室" />
            <el-table-column prop="appointStatus" label="预约状态">
                <template #default="scope">
                    <el-tag :type="scope.row.appointStatus === 'true' ? 'success' : 'warning'">
                        {{ scope.row.appointStatus === 'true' ? '已完成' : '未完成' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" />
            <el-table-column label="操作" fixed="right" width="200">
                <template #default="scope">
                    <el-button type="primary" size="small" @click="showModalDialog(scope.row)">预约详情</el-button>
                    <el-button type="danger" size="small" @click="cancelAppointment(scope.row)" 
                        :disabled="scope.row.appointStatus === 'true'">取消预约</el-button>
                </template>
            </el-table-column>
        </el-table>

        <div class="pagination">
            <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[10, 15, 20]"
                :total="total" layout="total, sizes, prev, pager, next" @size-change="handleSizeChange"
                @current-change="handleCurrentChange" />
        </div>

        <el-dialog v-model="dialogVisible" title="预约详情" width="50%" :before-close="handleClose">
            <div class="appointment-details" v-loading="loading">
                <h2 class="hospital-title">北京市某医院</h2>
                <h3 class="record-type">预约信息单</h3>
                <el-descriptions :column="2" border>
                    <el-descriptions-item label="患者姓名">{{ patientInfo.patientName }}</el-descriptions-item>
                    <el-descriptions-item label="编号">{{ patientInfo.appointmentId }}</el-descriptions-item>
                    <el-descriptions-item label="预约科室">{{ patientInfo.department }}</el-descriptions-item>
                    <el-descriptions-item label="预约时间">{{ patientInfo.createTime }}</el-descriptions-item>
                    <el-descriptions-item label="预约医生">{{ patientInfo.doctorName }}</el-descriptions-item>
                    <el-descriptions-item label="预约状态">{{ patientInfo.appointStatus === 'true' ? '已完成' : '未完成' }}</el-descriptions-item>
                </el-descriptions>

                <el-card class="record-content">
                    <el-descriptions direction="vertical" :column="1" border>
                        <el-descriptions-item label="备注信息">{{ patientInfo.remark || '无' }}</el-descriptions-item>
                    </el-descriptions>
                </el-card>

                <div class="print-time">
                    打印时间: {{ new Date().toLocaleDateString() }}
                </div>
            </div>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="handleClose">关闭</el-button>
                    <el-button type="primary" @click="printAppointment">打印预约单</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>
<script setup lang="ts">
import axios from 'axios';
import { onMounted, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';

const searchText = ref('');
const dialogVisible = ref(false);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const loading = ref(false);
const tableData = ref([]);
const patientInfo = reactive({
    patientName: "",
    appointmentId: "",
    department: "",
    doctorName: "",
    remark: "",
    createTime: "",
    appointStatus: "",
});

const handleSearch = () => {
    if(searchText.value == ""){
        ElMessage.error('请输入预约编号');
        return;
    }
    SearchAppointment();
};

const SearchAppointment = async () => {
    try {
        const response = await axios.get('/api/patient/getAppointmentById/', {
            params: { id: searchText.value }
        });
        if (response.status == 200) {
            patientInfo.patientName = response.data.patientName;
            patientInfo.appointmentId = response.data.appointmentId;
            patientInfo.department = response.data.department;
            patientInfo.doctorName = response.data.doctorName;
            patientInfo.remark = response.data.remark;
            patientInfo.createTime = response.data.createTime;
            patientInfo.appointStatus = response.data.appointStatus;
            dialogVisible.value = true;
        } else {
            ElMessage.error('查询失败');
        }
    } catch (error) {
        ElMessage.error('查询失败');
    }
};

const handleSizeChange = (val: number) => {
    pageSize.value = val;
    getAppointment();
};

const handleCurrentChange = (val: number) => {
    currentPage.value = val;
    getAppointment();
};

const showModalDialog = (row: any) => {
    dialogVisible.value = true;
    loading.value = true;
    Object.assign(patientInfo, row);
    loading.value = false;
};

const patientAddress = JSON.parse(localStorage.getItem('patientinfo') || '{}').accountAddress;

const cancelAppointment = (row: any) => {
    alert("暂未开通取消预约服务");
    // ElMessageBox.confirm('确定要取消该预约吗？', '提示', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     type: 'warning'
    // }).then(async () => {
    //     try {
    //         const response = await axios.post('/api/patient/cancelAppointment', {
    //             patientAddress: patientAddress,
    //             id: row.id,
    //         });
           
    //         if(response.data.code === 0) {
    //             ElMessage.success('取消预约成功');
    //             getAppointment();
    //         } else {
    //             ElMessage.error('取消预约失败');
    //         }
    //     } catch (error) {
    //         ElMessage.error('操作失败');
    //         console.error(error);
    //     }
    // });
};

const printAppointment = () => {
    window.print();
};

const handleClose = () => {
    dialogVisible.value = false;
    Object.assign(patientInfo, {
        doctor: "",
        department: "",
        createTime: "",
        status: "",
        mainComplaint: "",
        remark: ""
    });
};


const getAppointment = async () => {
    try {
        const response = await axios.get('/api/patient/getAppointmentByAll/', {
            params: { 
                address: patientAddress,
                page: currentPage.value,
                pageSize: pageSize.value
            }
        });
        console.log(response.data);
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
        console.error("Error during API call:", error);
        ElMessage.error('获取预约列表失败');
    }
};

onMounted(() => {
    getAppointment();
});
</script>

<style scoped>
.record-management {
    padding: 20px;
    background: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.search-section {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
}

.search-label {
    font-size: 14px;
    margin-right: 10px;
}

.search-input {
    display: flex;
    gap: 10px;
}

.search-input :deep(.el-input) {
    width: 200px;
}

.pagination {
    margin-top: 20px;
    display: flex;
    justify-content: center;
}

.appointment-details {
    padding: 20px;
}

.hospital-title {
    text-align: center;
    font-size: 24px;
    margin-bottom: 10px;
}

.record-type {
    text-align: center;
    font-size: 20px;
    margin-bottom: 20px;
}

.record-content {
    margin: 20px 0;
}

.print-time {
    text-align: right;
    margin-top: 20px;
    color: #909399;
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
}

@media print {
    .dialog-footer {
        display: none;
    }
}
</style>