package org.example.medical;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple4;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple5;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class MedicalDataSharingPlatform extends Contract {
    public static final String[] BINARY_ARRAY = {"6080604052600160005534801561001557600080fd5b50610c76806100256000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806302110d251461005c5780633d1c4d1c1461015b57806368cdafb3146102dc575b600080fd5b34801561006857600080fd5b50610159600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929080359060200190929190505050610431565b005b34801561016757600080fd5b506102da600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506105ee565b005b3480156102e857600080fd5b50610343600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061078d565b604051808060200180602001848152602001838103835286818151815260200191508051906020019080838360005b8381101561038d578082015181840152602081019050610372565b50505050905090810190601f1680156103ba5780820380516001836020036101000a031916815260200191505b50838103825285818151815260200191508051906020019080838360005b838110156103f35780820151818401526020810190506103d8565b50505050905090810190601f1680156104205780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b60c0604051908101604052808481526020018381526020018281526020013373ffffffffffffffffffffffffffffffffffffffff1681526020016020604051908101604052806000815250815260200160006040519080825280602002602001820160405280156104b15781602001602082028038833980820191505090505b508152506001856040518082805190602001908083835b6020831015156104ed57805182526020820191506020810190506020830392506104c8565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020600082015181600001908051906020019061053c929190610ad4565b506020820151816001019080519060200190610559929190610ad4565b506040820151816002015560608201518160030160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060808201518160040190805190602001906105c7929190610ad4565b5060a08201518160050190805190602001906105e4929190610b54565b5090505050505050565b6105f6610ba1565b60c06040519081016040528087815260200160005481526020018681526020018581526020018481526020018381525090508060026000805481526020019081526020016000206000820151816000019080519060200190610659929190610ad4565b50602082015181600101556040820151816002019080519060200190610680929190610ad4565b50606082015181600301908051906020019061069d929190610ad4565b5060808201518160040190805190602001906106ba929190610ad4565b5060a08201518160050190805190602001906106d7929190610ad4565b509050506001866040518082805190602001908083835b60208310151561071357805182526020820191506020810190506020830392506106ee565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020600501600054908060018154018082558091505090600182039060005260206000200160009091929091909150555060016000808282540192505081905550505050505050565b606080600061079a610bd8565b6001856040518082805190602001908083835b6020831015156107d257805182526020820191506020810190506020830392506107ad565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060c06040519081016040529081600082018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108aa5780601f1061087f576101008083540402835291602001916108aa565b820191906000526020600020905b81548152906001019060200180831161088d57829003601f168201915b50505050508152602001600182018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561094c5780601f106109215761010080835404028352916020019161094c565b820191906000526020600020905b81548152906001019060200180831161092f57829003601f168201915b50505050508152602001600282015481526020016003820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001600482018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a4e5780601f10610a2357610100808354040283529160200191610a4e565b820191906000526020600020905b815481529060010190602001808311610a3157829003601f168201915b5050505050815260200160058201805480602002602001604051908101604052809291908181526020018280548015610aa657602002820191906000526020600020905b815481526020019060010190808311610a92575b5050505050815250509050806000015181602001518260400151829250819150935093509350509193909250565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610b1557805160ff1916838001178555610b43565b82800160010185558215610b43579182015b82811115610b42578251825591602001919060010190610b27565b5b509050610b509190610c25565b5090565b828054828255906000526020600020908101928215610b90579160200282015b82811115610b8f578251825591602001919060010190610b74565b5b509050610b9d9190610c25565b5090565b60c0604051908101604052806060815260200160008152602001606081526020016060815260200160608152602001606081525090565b60c060405190810160405280606081526020016060815260200160008152602001600073ffffffffffffffffffffffffffffffffffffffff16815260200160608152602001606081525090565b610c4791905b80821115610c43576000816000905550600101610c2b565b5090565b905600a165627a7a72305820318b6d19877a151d9fd0e3159188ca879f3dc2661d93e1287cc29dea552fb0c90029"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"6080604052600160005534801561001557600080fd5b50610c76806100256000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806302110d251461005c5780633d1c4d1c1461015b57806368cdafb3146102dc575b600080fd5b34801561006857600080fd5b50610159600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929080359060200190929190505050610431565b005b34801561016757600080fd5b506102da600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506105ee565b005b3480156102e857600080fd5b50610343600480360381019080803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061078d565b604051808060200180602001848152602001838103835286818151815260200191508051906020019080838360005b8381101561038d578082015181840152602081019050610372565b50505050905090810190601f1680156103ba5780820380516001836020036101000a031916815260200191505b50838103825285818151815260200191508051906020019080838360005b838110156103f35780820151818401526020810190506103d8565b50505050905090810190601f1680156104205780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b60c0604051908101604052808481526020018381526020018281526020013373ffffffffffffffffffffffffffffffffffffffff1681526020016020604051908101604052806000815250815260200160006040519080825280602002602001820160405280156104b15781602001602082028038833980820191505090505b508152506001856040518082805190602001908083835b6020831015156104ed57805182526020820191506020810190506020830392506104c8565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020600082015181600001908051906020019061053c929190610ad4565b506020820151816001019080519060200190610559929190610ad4565b506040820151816002015560608201518160030160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060808201518160040190805190602001906105c7929190610ad4565b5060a08201518160050190805190602001906105e4929190610b54565b5090505050505050565b6105f6610ba1565b60c06040519081016040528087815260200160005481526020018681526020018581526020018481526020018381525090508060026000805481526020019081526020016000206000820151816000019080519060200190610659929190610ad4565b50602082015181600101556040820151816002019080519060200190610680929190610ad4565b50606082015181600301908051906020019061069d929190610ad4565b5060808201518160040190805190602001906106ba929190610ad4565b5060a08201518160050190805190602001906106d7929190610ad4565b509050506001866040518082805190602001908083835b60208310151561071357805182526020820191506020810190506020830392506106ee565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020600501600054908060018154018082558091505090600182039060005260206000200160009091929091909150555060016000808282540192505081905550505050505050565b606080600061079a610bd8565b6001856040518082805190602001908083835b6020831015156107d257805182526020820191506020810190506020830392506107ad565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060c06040519081016040529081600082018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108aa5780601f1061087f576101008083540402835291602001916108aa565b820191906000526020600020905b81548152906001019060200180831161088d57829003601f168201915b50505050508152602001600182018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561094c5780601f106109215761010080835404028352916020019161094c565b820191906000526020600020905b81548152906001019060200180831161092f57829003601f168201915b50505050508152602001600282015481526020016003820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001600482018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a4e5780601f10610a2357610100808354040283529160200191610a4e565b820191906000526020600020905b815481529060010190602001808311610a3157829003601f168201915b5050505050815260200160058201805480602002602001604051908101604052809291908181526020018280548015610aa657602002820191906000526020600020905b815481526020019060010190808311610a92575b5050505050815250509050806000015181602001518260400151829250819150935093509350509193909250565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610b1557805160ff1916838001178555610b43565b82800160010185558215610b43579182015b82811115610b42578251825591602001919060010190610b27565b5b509050610b509190610c25565b5090565b828054828255906000526020600020908101928215610b90579160200282015b82811115610b8f578251825591602001919060010190610b74565b5b509050610b9d9190610c25565b5090565b60c0604051908101604052806060815260200160008152602001606081526020016060815260200160608152602001606081525090565b60c060405190810160405280606081526020016060815260200160008152602001600073ffffffffffffffffffffffffffffffffffffffff16815260200160608152602001606081525090565b610c4791905b80821115610c43576000816000905550600101610c2b565b5090565b905600a165627a7a72305820318b6d19877a151d9fd0e3159188ca879f3dc2661d93e1287cc29dea552fb0c90029"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"methodSignatureAsString\":\"register(string,string,string,uint256)\",\"name\":\"register\",\"type\":\"function\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"nonpayable\",\"inputs\":[{\"name\":\"_id\",\"type\":\"string\",\"internalType\":\"\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_name\",\"type\":\"string\",\"internalType\":\"\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_gender\",\"type\":\"string\",\"internalType\":\"\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_age\",\"type\":\"uint256\",\"internalType\":\"\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"}],\"outputs\":[]},{\"methodSignatureAsString\":\"makeAppointment(string,string,string,string,string)\",\"name\":\"makeAppointment\",\"type\":\"function\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"nonpayable\",\"inputs\":[{\"name\":\"_id\",\"type\":\"string\",\"internalType\":\"\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_department\",\"type\":\"string\",\"internalType\":\"\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_doctor\",\"type\":\"string\",\"internalType\":\"\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"remark\",\"type\":\"string\",\"internalType\":\"\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"_createTime\",\"type\":\"string\",\"internalType\":\"\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}],\"outputs\":[]},{\"methodSignatureAsString\":\"getPatientInfo(string)\",\"name\":\"getPatientInfo\",\"type\":\"function\",\"constant\":true,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"view\",\"inputs\":[{\"name\":\"_id\",\"type\":\"string\",\"internalType\":\"\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}],\"outputs\":[{\"name\":\"\",\"type\":\"string\",\"internalType\":\"\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"\",\"type\":\"string\",\"internalType\":\"\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"\",\"type\":\"uint256\",\"internalType\":\"\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"}]}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_REGISTER = "register";

    public static final String FUNC_MAKEAPPOINTMENT = "makeAppointment";

    public static final String FUNC_GETPATIENTINFO = "getPatientInfo";

    protected MedicalDataSharingPlatform(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public TransactionReceipt register(String _id, String _name, String _gender, BigInteger _age) {
        final Function function = new Function(
                FUNC_REGISTER, 
                Arrays.<Type>asList(new Utf8String(_id),
                new Utf8String(_name),
                new Utf8String(_gender),
                new Uint256(_age)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] register(String _id, String _name, String _gender, BigInteger _age, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_REGISTER, 
                Arrays.<Type>asList(new Utf8String(_id),
                new Utf8String(_name),
                new Utf8String(_gender),
                new Uint256(_age)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRegister(String _id, String _name, String _gender, BigInteger _age) {
        final Function function = new Function(
                FUNC_REGISTER, 
                Arrays.<Type>asList(new Utf8String(_id),
                new Utf8String(_name),
                new Utf8String(_gender),
                new Uint256(_age)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple4<String, String, String, BigInteger> getRegisterInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_REGISTER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple4<String, String, String, BigInteger>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue(), 
                (BigInteger) results.get(3).getValue()
                );
    }

    public TransactionReceipt makeAppointment(String _id, String _department, String _doctor, String remark, String _createTime) {
        final Function function = new Function(
                FUNC_MAKEAPPOINTMENT, 
                Arrays.<Type>asList(new Utf8String(_id),
                new Utf8String(_department),
                new Utf8String(_doctor),
                new Utf8String(remark),
                new Utf8String(_createTime)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] makeAppointment(String _id, String _department, String _doctor, String remark, String _createTime, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_MAKEAPPOINTMENT, 
                Arrays.<Type>asList(new Utf8String(_id),
                new Utf8String(_department),
                new Utf8String(_doctor),
                new Utf8String(remark),
                new Utf8String(_createTime)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForMakeAppointment(String _id, String _department, String _doctor, String remark, String _createTime) {
        final Function function = new Function(
                FUNC_MAKEAPPOINTMENT, 
                Arrays.<Type>asList(new Utf8String(_id),
                new Utf8String(_department),
                new Utf8String(_doctor),
                new Utf8String(remark),
                new Utf8String(_createTime)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple5<String, String, String, String, String> getMakeAppointmentInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_MAKEAPPOINTMENT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple5<String, String, String, String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue(), 
                (String) results.get(3).getValue(), 
                (String) results.get(4).getValue()
                );
    }

    public Tuple3<String, String, BigInteger> getPatientInfo(String _id) throws ContractException {
        final Function function = new Function(FUNC_GETPATIENTINFO, 
                Arrays.<Type>asList(new Utf8String(_id)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple3<String, String, BigInteger>(
                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (BigInteger) results.get(2).getValue());
    }

    public static MedicalDataSharingPlatform load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new MedicalDataSharingPlatform(contractAddress, client, credential);
    }

    public static MedicalDataSharingPlatform deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(MedicalDataSharingPlatform.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }
}
