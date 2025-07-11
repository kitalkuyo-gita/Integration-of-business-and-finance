package com.iobaf.domain.customer.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.customer.entity.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 客户数据访问层
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Mapper
public interface CustomerRepository extends BaseMapper<Customer> {

    /**
     * 分页查询客户列表
     * 
     * @param page 分页参数
     * @param customerName 客户名称（模糊查询）
     * @param customerLevel 客户等级
     * @param status 状态
     * @return 分页结果
     */
    IPage<Customer> selectCustomerPage(Page<Customer> page, 
                                     @Param("customerName") String customerName,
                                     @Param("customerLevel") Integer customerLevel,
                                     @Param("status") Integer status);
} 