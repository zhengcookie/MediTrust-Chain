<template>
    <div>
        <div style="display: flex; justify-content: space-between; margin-top: 20px">
            <el-card style="width: 48%">
                <template #header>
                    <div class="info-header">
                        <span class="header-text">医疗信息统计</span>
                    </div>
                </template>
                <div class="chart-stats">
                    <div class="stat-item">
                        <div class="stat-value">{{ blockchainStats.blockNumber }}</div>
                        <div class="stat-label">区块高度</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-value">{{ blockchainStats.transactionCount }}</div>
                        <div class="stat-label">交易总数</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-value">{{ blockchainStats.queryCount }}</div>
                        <div class="stat-label">总查询次数</div>
                    </div>
                </div>
                <div ref="pieChartRef" style="width: 100%; height: 400px"></div>
            </el-card>

            <el-card style="width: 48%">
                <template #header>
                    <div class="info-header">
                        <span class="header-text">系统数据统计</span>
                    </div>
                </template>
                <div class="chart-stats">
                    <div class="stat-item">
                        <div class="stat-value">{{ systemStats.appointments }}</div>
                        <div class="stat-label">预约数量</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-value">{{ systemStats.patients }}</div>
                        <div class="stat-label">患者数量</div>
                    </div>
                </div>
                <div ref="barChartRef" style="width: 100%; height: 400px"></div>
            </el-card>
        </div>
    </div>
</template>

<script setup lang="ts">
import axios from 'axios';
import * as echarts from 'echarts';
import { onMounted, ref, reactive } from 'vue';

const pieChartRef = ref<HTMLDivElement | null>(null);
const barChartRef = ref<HTMLDivElement | null>(null);

const blockchainStats = reactive({
    blockNumber: 0,
    transactionCount: 0,
    query: 0,
    queryCount: 0
});

const systemStats = reactive({
    appointments: 0,
    patients: 0,
    doctors: 0,
    records: 0
});

const initPieChart = async () => {
    try {
        const blockchainData = await axios.get('/api/blockByNumber');
        const chart = echarts.init(pieChartRef.value);

        // 更新统计数据
        Object.assign(blockchainStats, blockchainData.data);

        const option = {
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            legend: {
                orient: 'vertical',
                left: 'left'
            },
            series: [
                {
                    name: '区块链数据',
                    type: 'pie',
                    radius: '60%',
                    data: [
                        { value: blockchainData.data.blockNumber, name: '区块高度' },
                        { value: blockchainData.data.transactionCount, name: '交易总数' },
                        { value: blockchainData.data.query, name: '查询次数' },
                        { value: blockchainData.data.queryCount, name: '总查询次数' }
                    ],
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };

        chart.setOption(option);

        window.addEventListener('resize', () => {
            chart.resize();
        });
    } catch (error) {
        console.error('Failed to load blockchain data:', error);
    }
};
const doctor = JSON.parse(localStorage.getItem('doctorinfo') || '{}').accountAddress;

const initBarChart = async () => {
    console.log(doctor);
    try {
        const systemData = await axios.get('/api/doctor/getAll', {
            params: { doctorAddress: doctor }
        });

        const chart = echarts.init(barChartRef.value);

        // 更新统计数据
        systemStats.appointments = systemData.data[0];
        systemStats.patients = systemData.data[1];


        const option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            xAxis: {
                type: 'category',
                data: ['预约数量', '患者数量']
            },
            yAxis: {
                type: 'value'
            },
            series: [{
                data: [
                    systemData.data[0],
                    systemData.data[1],
                ],
                type: 'bar',
                showBackground: true,
                backgroundStyle: {
                    color: 'rgba(180, 180, 180, 0.2)'
                }
            }]
        };

        chart.setOption(option);

        window.addEventListener('resize', () => {
            chart.resize();
        });
    } catch (error) {
        console.error('Failed to load system data:', error);
    }
};

onMounted(() => {
    initPieChart();
    initBarChart();
});
</script>

<style scoped>
.chart-stats {
    display: flex;
    justify-content: space-around;
    margin-bottom: 20px;
}

.stat-item {
    text-align: center;
    padding: 10px;
}

.stat-value {
    font-size: 24px;
    font-weight: bold;
    color: #409EFF;
}

.stat-label {
    font-size: 14px;
    color: #606266;
    margin-top: 5px;
}
</style>