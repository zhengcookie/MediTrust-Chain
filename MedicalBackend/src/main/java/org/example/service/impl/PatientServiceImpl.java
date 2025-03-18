package org.example.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.Form.AppointmentInfo;
import org.example.Form.LoginForm;
import org.example.Form.RegisterForm;
import org.example.VO.ResultVO;
import org.example.entity.Patient;
import org.example.mapper.PatientMapper;
import org.example.service.PatientService;
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
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {
    @Autowired
    WeBASEUtils weBASEUtils;

    private static final String OWNER_ADDRESS = "0x7835965bac8e4ca91c73af6afe704767dd7c70e7";

    private static final String contraceAddress="0x56d37a5f5bbffe00ff2d4cd9ebf52a0f38d494e2";
    public static final String ABI = org.example.utils.IOUtil.readResourceAsString("abi/MedicalPlatform.abi");
    @Autowired

    private PatientMapper patientMapper;
    @Override
    public ResultVO login(LoginForm loginForm) {

//        动态查询
//        判断用户是否存在
//        判断密码是否存在
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", loginForm.getUsername());
        Patient patient = patientMapper.selectOne(queryWrapper);
//      生成状态码
        ResultVO resultVO = new ResultVO();
        if (patient == null) {
            resultVO.setCode(-1);
        }else{
            if (!patient.getPassword().equals(loginForm.getPassword())) {
                resultVO.setCode(-2);
            }else{
                resultVO.setCode(0);
                resultVO.setMsg(patient);
            }
        }
        return resultVO;
    }
    @Override

    public ResultVO register(@RequestParam RegisterForm registerForm) {
        ResultVO resultVO = new ResultVO();
        String funcName = "registerPatient";

        // 动态查询，判断用户是否存在
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", registerForm.getUsername());
        Patient patient = patientMapper.selectOne(queryWrapper);
        System.out.println("patient信息: " + patient);

        if (patient != null) {
            resultVO.setCode(-1);
            resultVO.setMsg("用户名已被占用！");
        } else {
            // 创建新用户对象并设置属性
            Patient newPatient = new Patient();
            newPatient.setUsername(registerForm.getUsername());
            newPatient.setGender(registerForm.getGender());
            newPatient.setAge(registerForm.getAge());
            newPatient.setAccountAddress(registerForm.getAccountAddress());
            newPatient.setPassword(registerForm.getPassword());

            // 构建 funcParam 参数
            List<Object> funcParam = new ArrayList<>();
            funcParam.add(newPatient.getUsername());
            funcParam.add(newPatient.getGender());
            funcParam.add(newPatient.getAccountAddress());
            funcParam.add(newPatient.getAge());

            // 进行业务逻辑处理
            boolean funcResult = callBusinessFunction(funcParam,funcName);

            if (!funcResult) {
                resultVO.setCode(-2);
                resultVO.setMsg("业务注册失败！");
                return resultVO;
            }

            // 保存用户信息到数据库
            try {
                int rows = patientMapper.insert(newPatient);
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
    public ResultVO makeAppointment(AppointmentInfo appointmentInfo){
        ResultVO resultVO = new ResultVO();
        String funcName = "makeAppointment";
        List<Object> funcParm = new ArrayList<>();
        funcParm.add(appointmentInfo.getPatientName());
        funcParm.add(appointmentInfo.getDepartment());
        funcParm.add(appointmentInfo.getDoctorName());
        funcParm.add(appointmentInfo.getRemark());
        funcParm.add(appointmentInfo.getCreateTime());
        System.out.println("funcParm"+funcParm);
        boolean funcResult = callBusinessFunction(funcParm,funcName);

        if (!funcResult) {
            resultVO.setCode(-2);
            resultVO.setMsg(" 信息管理接口开发失败！");
        }else{
            resultVO.setCode(0);
            resultVO.setMsg(appointmentInfo);
        }

        return resultVO;

    }

    @Override
    public JSONArray getPatientAppointment(String patientAddress) {
        String funcName = "getPatientAppointment";
        List<Object> funcParm = new ArrayList<>();
        funcParm.add(patientAddress);
        String funcResult = weBASEUtils.funcPost(contraceAddress, ABI, OWNER_ADDRESS, funcName, funcParm);
        JSONArray responseStr = JSONUtil.parseArray(funcResult);
        return responseStr;
    }

    @Override
    public JSONArray getAppointmentByAll(String patientAddress) {
        JSONArray num_list = getPatientAppointment(patientAddress);
        System.out.println("num_list: " + num_list);

        // 获取第一个元素（假设返回的是一个数组或列表）
        JSONArray num_list2 = num_list.getJSONArray(0);
        JSONArray resList = new JSONArray();

        // 遍历 num_list2 来获取详细信息
        for (int i = 0; i < num_list2.size(); i++) {
            String appointmentId = num_list2.get(i).toString();
            String appointmentDetails = getAppointmentById(appointmentId).toString();

            // 这里添加到 JSONArray 而不是转换为字符串
            resList.add(appointmentDetails);
        }

        // 直接返回 JSON 数组
        return resList;
    }

    public JSONObject getAppointmentById(String id) {
        List<Object> funcParm = new ArrayList<>();
        funcParm.add(id);
        String responseStr = weBASEUtils.funcPost(contraceAddress, ABI, OWNER_ADDRESS, "getAppointmentById",funcParm);
        JSONArray List = JSONUtil.parseArray(responseStr);
        // 确保数据存在并且解析正确

        JSONObject _output = new JSONObject();
        _output.put("patientName", List.get(0));
        _output.put("appointmentId", List.get(1));
        _output.put("department", List.get(2));
        _output.put("doctorName", List.get(3));
        _output.put("remark", List.get(4));
        _output.put("createTime", List.get(5));
        _output.put("appointStatus",List.get(6));
        // 返回 JSON 字符串
        return _output;
        // 返回空字符串或者其他错误标识
    }



//    private JSONArray get_PatientAppointment_list(Integer id) {
//        List<Object> funcParm = new ArrayList<>();
//        funcParm.add(String.valueOf(id));
//        String responseStr = weBASEUtils.funcPost(contraceAddress,ABI,OWNER_ADDRESS,"getPatientAppointment",funcParm);
//        return JSONUtil.parseArray(responseStr);
//    }
//    没有调用合约

    @Override
    public JSONArray getPatientInfo(String address){
        String funcName = "getPatientInfo";
        List<Object> funcParm = new ArrayList<>();
        funcParm.add(address);
        String funcResult = weBASEUtils.funcPost(contraceAddress, ABI, OWNER_ADDRESS, funcName, funcParm);
        JSONArray response = JSONUtil.parseArray(funcResult);
        return response;
    }
//用于调用合约方法发送交易的协议
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
