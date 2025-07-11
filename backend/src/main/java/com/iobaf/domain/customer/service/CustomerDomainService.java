package com.iobaf.domain.customer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.customer.entity.Customer;
import com.iobaf.domain.customer.repository.CustomerRepository;
import com.iobaf.domain.customer.vo.CustomerVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户领域服务
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Service
public class CustomerDomainService {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * 分页查询客户列表
     * 
     * @param page 分页参数
     * @param customerName 客户名称
     * @param customerLevel 客户等级
     * @param status 状态
     * @return 客户列表
     */
    public IPage<CustomerVO> getCustomerPage(Page<Customer> page, 
                                           String customerName, 
                                           Integer customerLevel, 
                                           Integer status) {
        // 查询客户数据
        IPage<Customer> customerPage = customerRepository.selectCustomerPage(page, customerName, customerLevel, status);
        
        // 转换为VO对象
        List<CustomerVO> customerVOList = customerPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        // 创建新的分页结果
        Page<CustomerVO> resultPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        resultPage.setRecords(customerVOList);
        
        return resultPage;
    }

    /**
     * 根据ID查询客户详情
     * 
     * @param id 客户ID
     * @return 客户详情
     */
    public CustomerVO getCustomerById(Long id) {
        Customer customer = customerRepository.selectById(id);
        if (customer == null) {
            return null;
        }
        return convertToVO(customer);
    }

    /**
     * 创建客户
     * 
     * @param customer 客户信息
     * @return 是否成功
     */
    @Transactional
    public boolean createCustomer(Customer customer) {
        // 设置默认状态
        if (customer.getStatus() == null) {
            customer.setStatus(1);
        }
        if (customer.getCustomerLevel() == null) {
            customer.setCustomerLevel(1);
        }
        
        return customerRepository.insert(customer) > 0;
    }

    /**
     * 更新客户
     * 
     * @param customer 客户信息
     * @return 是否成功
     */
    @Transactional
    public boolean updateCustomer(Customer customer) {
        return customerRepository.updateById(customer) > 0;
    }

    /**
     * 删除客户
     * 
     * @param id 客户ID
     * @return 是否成功
     */
    @Transactional
    public boolean deleteCustomer(Long id) {
        return customerRepository.deleteById(id) > 0;
    }

    /**
     * 将实体对象转换为VO对象
     * 
     * @param customer 客户实体
     * @return 客户VO
     */
    private CustomerVO convertToVO(Customer customer) {
        CustomerVO vo = new CustomerVO();
        BeanUtils.copyProperties(customer, vo);
        
        // 设置等级名称
        switch (customer.getCustomerLevel()) {
            case 1:
                vo.setCustomerLevelName("普通");
                break;
            case 2:
                vo.setCustomerLevelName("重要");
                break;
            case 3:
                vo.setCustomerLevelName("VIP");
                break;
            default:
                vo.setCustomerLevelName("未知");
        }
        
        // 设置状态名称
        vo.setStatusName(customer.getStatus() == 1 ? "启用" : "禁用");
        
        return vo;
    }
} 