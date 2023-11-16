package com.sky.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.DigestUtils;

import java.io.Serializable;

@Data
public class PasswordEditDTO implements Serializable {

    //员工id
    @ApiModelProperty("员工id")
    private Long empId;

    //旧密码
    @ApiModelProperty("旧密码")
    private String oldPassword;

    //新密码
    @ApiModelProperty("新密码")
    private String newPassword;

    public void getMd5(){
        oldPassword= DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        newPassword= DigestUtils.md5DigestAsHex(newPassword.getBytes());
    }


}
