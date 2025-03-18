package org.example.controller;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.example.Form.AppointmentInfo;
import org.example.Form.LoginForm;
import org.example.Form.RegisterForm;
import org.example.VO.ResultVO;
import org.example.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author admin
 * @since 2024-12-03
 */
@RestController
@RequestMapping("/patient")
public class PatientController {
//    自动注入
    @Autowired
//    导入服务类
    private PatientService patientService;
    @GetMapping("/login")
    public ResultVO login(LoginForm loginForm){
        ResultVO resultVO = this.patientService.login(loginForm);
        return resultVO;
    }

    @PostMapping("/register")
    public ResultVO register(@RequestBody RegisterForm registerForm){
        System.out.println(registerForm);
        ResultVO resultVO = this.patientService.register(registerForm);
        return resultVO;
    }
    @GetMapping("/getPatientInfo")
    public JSONArray getPatientInfo(@RequestParam("address") String address){
        JSONArray resultVO = this.patientService.getPatientInfo(address);
        return resultVO;
    }
    @PostMapping("/makeAppointment")
    public ResultVO makeAppointment(@RequestBody AppointmentInfo appointmentInfo){
        ResultVO resultVO = this.patientService.makeAppointment(appointmentInfo);
        return resultVO;
    }
    @GetMapping("/getPatientAppointment")
    public JSONArray getPatientAppointment(@RequestParam("patientAddress") String patientAddress){
        JSONArray result = this.patientService.getPatientAppointment(patientAddress);

        return result;
    }
    @GetMapping("/getAppointmentById")
    public JSONObject getAppointmentById(@RequestParam("id") String id){
        JSONObject result = this.patientService.getAppointmentById(id);
        return result;
    }
    @GetMapping("/getAppointmentByAll")
    public JSONArray  getAppointmentByAll(@RequestParam("address") String address){
        JSONArray result = this.patientService.getAppointmentByAll(address);
        return result;
    }
}

