package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import org.apache.poi.util.PackageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 添加md5验证
         password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }
    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult select(EmployeePageQueryDTO employeePageQueryDTO) {
         PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee>page=employeeMapper.pageQuery(employeePageQueryDTO);
         long total = page.getTotal();
         List<Employee> records = page.getResult();
         return new PageResult(total,records);
    }
    /**
     * 更换员工密码
     * @param passwordEditDTO
     */
    @Override
    public Result editPassword(PasswordEditDTO passwordEditDTO) {
        Long empId = BaseContext.getCurrentId();
        passwordEditDTO.setEmpId(empId);
        Employee employee = employeeMapper.select(empId);
        passwordEditDTO.getMd5();
        if (employee.getPassword().equals(passwordEditDTO.getOldPassword())){
         employeeMapper.updatePassword(passwordEditDTO);
         return Result.success("更换密码成功");
        }else {
        return Result.error("密码错误");
    }
    }
    /**
     * 新增员工
     * @param employee
     */
    @Override
    public void save(Employee employee) {
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long createUser = BaseContext.getCurrentId();
        employee.setCreateUser(createUser);
        employee.setUpdateUser(createUser);
        employee.setPassword(PasswordConstant.DEFAULT_PASSWORD);
        employee.setStatus(StatusConstant.ENABLE);
        employeeMapper.insert(employee);
    }

    /**
     * 用户状态变化
     * @param status
     */
    public void statusChange(Integer status,Long id) {
        Employee employee = new Employee();
        employee.setStatus(status);
        employee.setId(id);
        employeeMapper.update(employee);
    }

    /**
     * 编辑用户信息
     * @param employeeDTO
     * @return
     */
    public void editUserInfo(EmployeeDTO employeeDTO) {
         Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employeeMapper.update(employee);
    }
}
