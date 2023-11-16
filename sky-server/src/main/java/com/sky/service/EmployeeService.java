package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;

public interface EmployeeService {



    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult select(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 新增员工
     * @param employee
     */
    void save(Employee employee);

    /**
     * 更换员工密码
     * @param passwordEditDTO
     */
    Result editPassword(PasswordEditDTO passwordEditDTO);

    /**
     * 用户状态变化
     * @param status
     */
    void statusChange(Integer status,Long id);

    /**
     * 编辑用户信息
     * @param employeeDTO
     * @return
     */
    void editUserInfo(EmployeeDTO employeeDTO);
}
