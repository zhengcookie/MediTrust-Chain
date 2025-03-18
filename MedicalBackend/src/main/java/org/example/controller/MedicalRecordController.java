package org.example.controller;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.example.entity.MedicalRecord;
import org.example.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2024-12-08
 */
@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {
    @Autowired
    private MedicalRecordService medicalRecordService;
    @PostMapping("/startConsultation")
    public void startConsultation(@RequestParam String patientAddress){
        this.medicalRecordService.startConsultation(patientAddress);
    }
    @PostMapping("/fillInMedicalHistory")
    public void fillInMedicalHistory(@RequestBody MedicalRecord medicalRecord){
        this.medicalRecordService.fillInMedicalHistory(medicalRecord);
    }
    @GetMapping("/getMedicalHistory")
    public JSONObject getMedicalHistory(@RequestParam String medicalRecordId){
       JSONObject result = this.medicalRecordService.getMedicalHistory(medicalRecordId);
        return result;
    }
    @GetMapping("/getMedicalHistoryByAll")
    public JSONArray getMedicalHistoryByAll(@RequestParam String doctorAddress){
        JSONArray result = this.medicalRecordService.getMedicalHistoryByAll(doctorAddress);
        return result;
    }

}

