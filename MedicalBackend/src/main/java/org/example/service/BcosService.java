package org.example.service;

import cn.hutool.core.lang.Dict;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author binge
 * @Description
 * @date 2023年05月15日 下午 5:06
 */
@Service
public class BcosService {
    private final Client client;

    //TODO：任务3-3-4 请补充代码
    public BcosService(){
//        调用区块链平台
        BcosSDK bcosSDK = BcosSDK.build("D:\\比赛\\区块链\\MedicalBackend\\src\\main\\resources\\config-example.toml");
//        获取连接数
        client = bcosSDK.getClient(Integer.valueOf(1));
    }
    public final Client getClient(){
        return client;
    }


}
