package org.example.controller;

import cn.hutool.json.JSONObject;

import org.example.service.BcosService;
//import com.zkl.fisco.domain.FoodInfoItem;
import io.swagger.annotations.ApiOperation;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.response.BlockNumber;
import org.fisco.bcos.sdk.client.protocol.response.ConsensusStatus;
import org.fisco.bcos.sdk.client.protocol.response.TotalTransactionCount;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * @author binge
 * @Description
 * @date 2023年05月15日 下午 5:05
 */
@RestController
public class BcosController {
    @Autowired
    private BcosService bcosService;
    @GetMapping("/txSum")
    @ApiOperation(value = "获取交易总量")
    public  String getTotalTransactionCount() throws ContractException {
        Client client = bcosService.getClient();
        TotalTransactionCount transactionCount = client.getTotalTransactionCount();//获取交易总量
        if (transactionCount.hasError()){//判断是否获取失败
            throw new ContractException(transactionCount.getError().getMessage());//失败抛出异常
        }
        Integer integer = Integer.parseInt(transactionCount.getTotalTransactionCount().getTxSum().substring(2), 16);// 需要将其及进行转换，因为它是返回16进制的
        return  integer.toString();
    }

    @GetMapping("/blockByNumber")
    @ApiOperation(value = "获取区块高度、区块链交易总数、供应链查询次数、供应链总查询次数")
    public JSONObject getBlockByNumber() throws ContractException {
        Client client = bcosService.getClient();
// 使用jsonobject进行数据存储
        JSONObject json = new JSONObject();

//        获取区块高度
        BlockNumber blockNumber = client.getBlockNumber();
        if (blockNumber.hasError()){
            throw new ContractException(blockNumber.getError().getMessage());
        }
        json.set("blockNumber",blockNumber.getBlockNumber());
//        获取区块链交易总数
        String totalTransactionCount = getTotalTransactionCount();
        json.set("transactionCount",totalTransactionCount);
//        供应链查询次数??
        json.set("query",2);
//        供应链总查询次数??
        json.set("queryCount",10);
        return  json;
    }
    @ApiOperation(value = "获取区块链状态", notes = "获取区块链状态")
    @RequestMapping(value = "getSyncStatus", method = RequestMethod.GET)
    public ConsensusStatus getBlockchainStatus() throws UnsupportedEncodingException {
        Client client = bcosService.getClient();
        return client.getConsensusStatus();
    }

}