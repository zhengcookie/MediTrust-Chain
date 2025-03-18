package org.example.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.Form.LoginForm;
import org.example.Form.RegisterForm;
import org.example.VO.ResultVO;
import org.example.entity.Doctor;
import org.example.mapper.DoctorMapper;
import org.example.service.DoctorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.utils.WeBASEUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author admin
 * @since 2024-12-03
 */
@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements DoctorService {

        @Autowired
        WeBASEUtils weBASEUtils;

        private static final String OWNER_ADDRESS = "0x7835965bac8e4ca91c73af6afe704767dd7c70e7";

        private static final String contraceAddress="0x56d37a5f5bbffe00ff2d4cd9ebf52a0f38d494e2";
        public static final String ABI = org.example.utils.IOUtil.readResourceAsString("abi/MedicalPlatform.abi");
        @Autowired

        private DoctorMapper doctorMapper;
        @Override
        public ResultVO login(LoginForm loginForm) {

//        动态查询
//        判断用户是否存在
//        判断密码是否存在
            QueryWrapper<Doctor> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", loginForm.getUsername());
            Doctor doctor = doctorMapper.selectOne(queryWrapper);
//      生成状态码
            ResultVO resultVO = new ResultVO();
            if (doctor == null) {
                resultVO.setCode(-1);
            }else{
                if (!doctor.getPassword().equals(loginForm.getPassword())) {
                    resultVO.setCode(-2);
                }else{
                    resultVO.setCode(0);
                    resultVO.setMsg(doctor);
                }
            }
            return resultVO;
        }
        @Override

        public ResultVO register(@RequestParam RegisterForm registerForm) {
            ResultVO resultVO = new ResultVO();
            String funcName = "registerDoctor";

            // 动态查询，判断用户是否存在
            QueryWrapper<Doctor> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", registerForm.getUsername());
            Doctor doctor = doctorMapper.selectOne(queryWrapper);
            System.out.println("doctor信息: " + doctor);

            if (doctor != null) {
                resultVO.setCode(-1);
                resultVO.setMsg("用户名已被占用！");
            } else {
                // 创建新用户对象并设置属性
                Doctor newDoctor = new Doctor();
                newDoctor.setUsername(registerForm.getUsername());
                newDoctor.setGender(registerForm.getGender());
                newDoctor.setAge(registerForm.getAge());
                newDoctor.setAccountAddress(registerForm.getAccountAddress());
                newDoctor.setPassword(registerForm.getPassword());

                // 构建 funcParam 参数
                List<Object> funcParam = new ArrayList<>();
                funcParam.add(newDoctor.getUsername());
                funcParam.add(newDoctor.getGender());
                funcParam.add(newDoctor.getAccountAddress());
                funcParam.add(newDoctor.getAge());



                // 进行业务逻辑处理
                boolean funcResult = callBusinessFunction(funcParam,funcName);

                if (!funcResult) {
                    resultVO.setCode(-2);
                    resultVO.setMsg("业务注册失败！");
                    return resultVO;
                }

                // 保存用户信息到数据库
                try {
                    int rows = doctorMapper.insert(newDoctor);
                    if (rows > 0) {
                        resultVO.setCode(0);
                        resultVO.setMsg("注册成功");
                    } else {
                        resultVO.setCode(-2);
                        resultVO.setMsg("注册失败！");
                    }
                } catch (Exception e) {
                    resultVO.setCode(-2);
                    resultVO.setMsg("数据库插入异常：" + e.getMessage());
                    e.printStackTrace();
                }
            }
            return resultVO;
        }

    @Override
    public JSONArray getDoctorInfo(String address) {
        String funcName = "getDoctorInfo";
        List<Object> funcParm = new ArrayList<>();
        funcParm.add(address);
        String funcResult = weBASEUtils.funcPost(contraceAddress, ABI, OWNER_ADDRESS, funcName, funcParm);
        JSONArray response = JSONUtil.parseArray(funcResult);
        return response;
    }

    @Override
    public JSONArray getPatientsInfoByDoctor(String doctorAddress) {
        String funcName = "getPatientsInfoByDoctor";
        List<Object> funcParam = new ArrayList<>();
        funcParam.add(doctorAddress);
        String funcResult = weBASEUtils.funcPost(contraceAddress, ABI, OWNER_ADDRESS, funcName, funcParam);
        JSONArray responseStr = JSONUtil.parseArray(funcResult);
        return responseStr;
    }
    @Override
    public JSONArray getAppointmentInfoByDoctor(String doctorAddress){
            String funcName = "getAppointmentsByDoctor";
            List<Object> funcParm = new ArrayList<>();
            funcParm.add(doctorAddress);
            String funcResult = weBASEUtils.funcPost(contraceAddress,ABI,OWNER_ADDRESS,funcName,funcParm);
            JSONArray responseStr = JSONUtil.parseArray(funcResult);
            return responseStr;
    }

    @Override
    public JSONArray getAll(String doctorAddress) {
        String funcName = "getAll";
        List<Object> funcParam = new ArrayList<>();
        funcParam.add(doctorAddress);
        String funcResult = weBASEUtils.funcPost(contraceAddress,ABI,OWNER_ADDRESS,funcName,funcParam);
        JSONArray responseStr = JSONUtil.parseArray(funcResult);
        return responseStr;
    }



    @Override
    public void fillInAppointment(Integer appointmentId) {
        System.out.println("appointmentId: " + appointmentId);
        String funcName = "fillInAppointment";
        List<Object> funcParam = new ArrayList<>();
        funcParam.add(appointmentId);
        String funcResult = weBASEUtils.funcPost(contraceAddress,ABI,OWNER_ADDRESS,funcName,funcParam);
        System.out.println(funcResult);
    }

    private boolean callBusinessFunction(List<Object> funcParam,String funcName) {
        try {
            System.out.println("funParam: " + funcParam);
            String _result = weBASEUtils.funcPost(contraceAddress,ABI, OWNER_ADDRESS, funcName, funcParam);
            System.out.println(_result);
            JSONObject _resultJson = JSONUtil.parseObj(_result);
            if (!_resultJson.containsKey("statusOK") || !_resultJson.getBool("statusOK")) {
                return false;
            }

            return true;
        } catch (Exception e) {
            System.out.println("调用业务函数失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
