package com.itheima.reggie.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeHandler {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee,HttpSession session){

        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername,employee.getUsername());
        Employee employeeServiceOne = employeeService.getOne(wrapper);

//        若没有查到，则登录失败
        if (employeeServiceOne == null) {
            return R.error("登录失败");
        }
//       比对密码
        if (!employeeServiceOne.getPassword().equals(password)) {
            return R.error("密码错误");
        }
//        检验是否账号禁用
        if (employeeServiceOne.getStatus() == 0) {
            return R.error("账号已禁用");
        }
        session.setAttribute("employee",employeeServiceOne.getId());
        return R.success(employeeServiceOne);
    }

    /**
     * 退出
     * @param session
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpSession session){
        session.removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 员工分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageR(int page,int pageSize,String name){
        Page<Employee> employeePage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(name),Employee::getName,name);
        wrapper.orderByAsc(Employee::getCreateTime);
        employeeService.page(employeePage, wrapper);
        return R.success(employeePage);
    }

    /**
     * 员工添加
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Employee employee){
        String s = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(s);
        employeeService.save(employee);
        return R.success("添加成功");
    }

    /**
     * 回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> back(@PathVariable long id){
        if (!StringUtils.hasLength(String.valueOf(id))){
            return R.error("未知错误1");
        }
        Employee byId = employeeService.getById(id);
        if (byId == null){
            return R.error("未知错误2");
        }
        return R.success(byId);
    }

    /**
     * 修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> put(@RequestBody Employee employee){
        employeeService.updateById(employee);
        return R.success("修改成功");
    }
}
