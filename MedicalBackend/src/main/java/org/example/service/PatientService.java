package org.example.service;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.example.Form.AppointmentInfo;
import org.example.Form.LoginForm;
import org.example.Form.RegisterForm;
import org.example.VO.ResultVO;
import org.example.entity.Patient;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2024-12-03
 */
public interface PatientService extends IService<Patient> {
    public ResultVO login(LoginForm loginForm);
    public ResultVO register(RegisterForm registerForm);

    public ResultVO makeAppointment(AppointmentInfo appointmentInfo);

    public JSONArray getPatientAppointment(String patientAddress);

    public JSONArray getAppointmentByAll(String patientAddress);

    public JSONObject getAppointmentById(String id);

    public JSONArray getPatientInfo(String address);
}
