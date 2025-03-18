package org.example.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.MedicalRecord;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2024-12-08
 */
public interface MedicalRecordService extends IService<MedicalRecord> {
    public void startConsultation(String patientAddress);
    public void fillInMedicalHistory(MedicalRecord medicalRecord);

    public JSONObject getMedicalHistory(String medicalRecordId);

    public JSONArray getMedicalHistoryByAll(String doctorAddress);

}
