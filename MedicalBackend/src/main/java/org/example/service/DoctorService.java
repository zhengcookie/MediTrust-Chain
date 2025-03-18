package org.example.service;

import cn.hutool.json.JSONArray;
import org.example.Form.LoginForm;
import org.example.Form.RegisterForm;
import org.example.VO.ResultVO;
import org.example.entity.Doctor;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author admin
 * @since 2024-12-03
 */
public interface DoctorService extends IService<Doctor> {
    public ResultVO login(LoginForm loginForm);
    public ResultVO register(RegisterForm registerForm);

    public JSONArray getDoctorInfo(String address);
    public JSONArray getPatientsInfoByDoctor(String doctorAddress);

    public JSONArray getAppointmentInfoByDoctor(String doctorAddress);
    public JSONArray getAll(String doctorAddress);
//    public void startConsultation(String patientAddress);
    public void fillInAppointment(Integer appointmentId);
}
