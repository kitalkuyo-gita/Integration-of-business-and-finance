package com.iobaf.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.common.response.Result;
import com.iobaf.domain.customer.entity.Customer;
import com.iobaf.domain.customer.service.CustomerDomainService;
import com.iobaf.domain.customer.vo.CustomerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 客户管理控制器
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerDomainService customerDomainService;

    /**
     * 分页查询客户列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param customerName 客户名称
     * @param customerLevel 客户等级
     * @param status 状态
     * @return 客户列表
     */
    @GetMapping
    public Result<IPage<CustomerVO>> getCustomerList(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) Integer customerLevel,
            @RequestParam(required = false) Integer status) {
        
        Page<Customer> page = new Page<>(current, size);
        IPage<CustomerVO> result = customerDomainService.getCustomerPage(page, customerName, customerLevel, status);
        
        return Result.success(result);
    }

    /**
     * 根据ID查询客户详情
     * 
     * @param id 客户ID
     * @return 客户详情
     */
    @GetMapping("/{id}")
    public Result<CustomerVO> getCustomerById(@PathVariable Long id) {
        CustomerVO customer = customerDomainService.getCustomerById(id);
        if (customer == null) {
            return Result.error("客户不存在");
        }
        return Result.success(customer);
    }

    /**
     * 创建客户
     * 
     * @param customer 客户信息
     * @return 操作结果
     */
    @PostMapping
    public Result<String> createCustomer(@RequestBody Customer customer) {
        boolean success = customerDomainService.createCustomer(customer);
        if (success) {
            return Result.success("客户创建成功");
        } else {
            return Result.error("客户创建失败");
        }
    }

    /**
     * 更新客户
     * 
     * @param id 客户ID
     * @param customer 客户信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public Result<String> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        customer.setId(id);
        boolean success = customerDomainService.updateCustomer(customer);
        if (success) {
            return Result.success("客户更新成功");
        } else {
            return Result.error("客户更新失败");
        }
    }

    /**
     * 删除客户
     * 
     * @param id 客户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteCustomer(@PathVariable Long id) {
        boolean success = customerDomainService.deleteCustomer(id);
        if (success) {
            return Result.success("客户删除成功");
        } else {
            return Result.error("客户删除失败");
        }
    }
} 