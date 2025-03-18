package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author admin
 * @since 2024-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Patient implements Serializable {

    private static final long serialVersionUID=1L;
//添加配置使其能够传递值

    @JsonProperty
    private String username;
    @JsonProperty
    private String gender;
    @JsonProperty
    private Integer age;
    @JsonProperty
    @TableField("accountAddress")
    private String accountAddress;
    @JsonProperty
    private String password;

    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getGender(){
        return gender;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public Integer getAge(){
        return age;
    }
    public void setAge(Integer age){
        this.age = age;
    }
    public String getAccountAddress(){
        return accountAddress;
    }
    public void setAccountAddress(String accountAddress){
        this.accountAddress = accountAddress;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }


}
