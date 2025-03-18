package org.example.controller;


import cn.hutool.core.lang.Tuple;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.example.Form.FillInMedical;
import org.example.Form.LoginForm;
import org.example.Form.RegisterForm;
import org.example.VO.ResultVO;
import org.example.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2024-12-03
 */
@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
//    导入服务类
    private DoctorService doctorService;
    @GetMapping("/login")
    public ResultVO login(LoginForm loginForm){
        ResultVO resultVO = this.doctorService.login(loginForm);
        return resultVO;
    }

    @PostMapping("/register")
    public ResultVO register(@RequestBody RegisterForm registerForm){
        System.out.println(registerForm);
        ResultVO resultVO = this.doctorService.register(registerForm);
        return resultVO;
    }
    @GetMapping("/getDoctorInfo")
    public JSONArray getPatientInfo(@RequestParam("address") String address){
        JSONArray resultVO = this.doctorService.getDoctorInfo(address);
        return resultVO;
    }
    @GetMapping("/getPatientsInfoByDoctor")
    public JSONArray getPatientsInfoByDoctor(@RequestParam String doctorAddress){
        JSONArray result = this.doctorService.getPatientsInfoByDoctor(doctorAddress);
        return result;
    }
    @GetMapping("/getAppointmentInfoByDoctor")
    public JSONArray getgetAppointmentInfoByDoctor(@RequestParam String doctorAddress){
        JSONArray result = this.doctorService.getAppointmentInfoByDoctor(doctorAddress);
        return result;
    }
    //    更新预约状态
    @PostMapping("/fillInAppointment")
    public void fillInAppointment(@RequestParam Integer appointmentId){
        System.out.println("appointmentId:"+appointmentId);
        this.doctorService.fillInAppointment(appointmentId);
    }
    @GetMapping("/getAll")
    public JSONArray getAll(@RequestParam String doctorAddress){
        JSONArray result = this.doctorService.getAll(doctorAddress);
        return result;
    }

//    @PostMapping("/fillInMedicalHistory")
//    public void fillInMedicalHistory(@RequestBody FillInMedical fillInMedical){
//        System.out.println(fillInMedical);
////        this.doctorService.fillInMedicalHistory(fillInMedical);
//    }
//    @GetMapping("/getMedicalHistory")
//    public JSONArray getMedicalHistory(@RequestParam String doctorAddress){
//        System.out.println("doctorAddress:"+doctorAddress);
//
//    }

}

