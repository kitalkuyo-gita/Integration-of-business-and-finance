package com.iobaf.domain.contract.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.contract.entity.Contract;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 合同数据访问层
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Mapper
public interface ContractRepository extends BaseMapper<Contract> {

    /**
     * 分页查询合同列表
     * 
     * @param page 分页参数
     * @param contractName 合同名称（模糊查询）
     * @param contractType 合同类型
     * @param status 状态
     * @param customerId 客户ID
     * @return 分页结果
     */
    IPage<Contract> selectContractPage(Page<Contract> page, 
                                     @Param("contractName") String contractName,
                                     @Param("contractType") Integer contractType,
                                     @Param("status") Integer status,
                                     @Param("customerId") Long customerId);
} 