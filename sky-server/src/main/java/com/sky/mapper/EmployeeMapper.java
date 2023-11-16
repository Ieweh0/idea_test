package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 查询用户
     * @param employeePageQueryDTO
     * @return
     */

    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);


    /**
     * 增加用户
     * @param employee
     */
    @Insert("insert into employee( id,username,name,password,phone,sex,id_number,status,update_time,create_time,create_user,update_user)" +
            "values " +
            "( #{id},#{username}, #{name}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{updateTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Employee employee);

    /**
     * 更换密码
     * @param passwordEditDTO
     */
    //TODO 更新密码
    void updatePassword(PasswordEditDTO passwordEditDTO);

    /**
     * 根据ID查询用户
     * @param empId
     * @return
     */
    @Select("select * from employee where id=#{empId}")
    Employee select(long empId);

    /**
     * 更新用户状态
     * @param employee
     */
    void update(Employee employee);

}
