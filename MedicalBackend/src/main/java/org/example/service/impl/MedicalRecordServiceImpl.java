package org.example.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
//import org.example.mapper.MedicalRecordMapper;
import org.example.entity.MedicalRecord;
import org.example.mapper.MedicalRecordMapper;
import org.example.service.MedicalRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.utils.WeBASEUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2024-12-08
 */
@Service
public class MedicalRecordServiceImpl extends ServiceImpl<MedicalRecordMapper, MedicalRecord> implements MedicalRecordService {
    @Autowired
    WeBASEUtils weBASEUtils;

    private static final String OWNER_ADDRESS = "0x7835965bac8e4ca91c73af6afe704767dd7c70e7";

    private static final String contraceAddress="0x56d37a5f5bbffe00ff2d4cd9ebf52a0f38d494e2";
    public static final String ABI = org.example.utils.IOUtil.readResourceAsString("abi/MedicalPlatform.abi");
    @Autowired

    private MedicalRecordMapper medicalRecordMapper;
    @Override
    public void startConsultation(String patientAddress) {
        String funcName = "startConsultation";
//        List<MedicalRecord> medicalRecords = medicalRecordMapper.selectList(null);
        List<Object> params = new ArrayList();
        params.add(patientAddress);
        String funcResult =weBASEUtils.funcPost(contraceAddress,ABI,OWNER_ADDRESS,funcName,params);
        System.out.println(funcResult);
    }

    @Override
    public void fillInMedicalHistory(MedicalRecord medicalRecord) {
        String funcName = "fillInMedicalHistory";
        List<Object> params = new ArrayList();
        params.add(medicalRecord.getPatientName());
        params.add(medicalRecord.getHospitalName());
        params.add(medicalRecord.getDepartment());
        params.add(medicalRecord.getDoctorName());
        params.add(medicalRecord.getRegistrationInfo());
        params.add(medicalRecord.getPastMedicalHistory());
        params.add(medicalRecord.getCurrentMedicalHistory());
        String funcResult = weBASEUtils.funcPost(contraceAddress,ABI,OWNER_ADDRESS,funcName,params);
        System.out.println(funcResult);
        medicalRecordMapper.insert(medicalRecord);
    }

    @Override
    public JSONObject getMedicalHistory(String medicalRecordId) {
        String funcName = "getMedicalHistory";
        List<Object> params = new ArrayList();
        params.add(medicalRecordId);
        String funcResult = weBASEUtils.funcPost(contraceAddress,ABI,OWNER_ADDRESS,funcName,params);
        System.out.println(funcResult);
        JSONArray result = JSONUtil.parseArray(funcResult);
        System.out.println(result);
        JSONObject _output = new JSONObject();
        _output.put("MedicalRecordId",result.get(0));
        _output.put("patientName",result.get(1));
        _output.put("hospitalName",result.get(2));
        _output.put("department",result.get(3));
        _output.put("doctorName",result.get(4));
        _output.put("registrationInfo",result.get(5));
        _output.put("pastMedicalHistory",result.get(6));
        _output.put("currentMedicalHistory",result.get(7));
        return _output;
    }
    @Override
    public JSONArray getMedicalHistoryByAll(String doctorAddress) {
        JSONArray num_list = getMedicalHistoryNumber(doctorAddress);
        System.out.println("num_list"+num_list);
        JSONArray num_list2 = num_list.getJSONArray(0);
        JSONArray resList = new JSONArray();

        for (int i = 0; i < num_list2.size(); i++) {
            String medicalRecordId = num_list2.get(i).toString();
            String medicalRecoredDetails = getMedicalHistory(medicalRecordId).toString();
            resList.add(medicalRecoredDetails);
        }
        return resList;
    }
    public JSONArray getMedicalHistoryNumber(String doctorAddress) {
        String funcName = "getMedicalHistoryNumber";
        List<Object> params = new ArrayList();
        params.add(doctorAddress);
        String funcResult = weBASEUtils.funcPost(contraceAddress,ABI,OWNER_ADDRESS,funcName,params);
        System.out.println(funcResult);
        JSONArray result = JSONUtil.parseArray(funcResult);
        System.out.println(result);
        return result;
    }




}
