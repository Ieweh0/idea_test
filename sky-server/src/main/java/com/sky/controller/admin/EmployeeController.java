package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@Api(tags = "员工管理接口文档")
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */

    @PostMapping("/login")
    @ApiOperation("用户登录接口")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }


    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("接收到分页数据：{}",employeePageQueryDTO);
        PageResult pageResult=employeeService.select(employeePageQueryDTO);
        return Result.success(pageResult);
    }



    @ApiOperation("新增员工")
    @PostMapping
    public Result addEmployee(@RequestBody Employee employee) {
        log.info("新增员工：{}"+ employee);
        employeeService.save(employee);
        return Result.success();
    }


    @ApiOperation("更换用户密码")
    @PutMapping("/editPassword")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
         Result result = employeeService.editPassword(passwordEditDTO);
        return result;
    }

    @ApiOperation("启用、禁用员工账号")
    @PostMapping("/status/{status}")
    public Result status(@PathVariable Integer status,Long id) {
        employeeService.statusChange(status,id);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("编辑员工信息")
    public Result editUserInformation(@RequestBody EmployeeDTO employeeDTO){
        log.info("接收到编辑员工信息：{}",employeeDTO);
         employeeService.editUserInfo(employeeDTO);
        return Result.success();
    }



    /**
     * 退出
     *
     * @return
     */
    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }


}
