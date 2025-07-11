package com.iobaf.domain.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.contract.entity.Contract;
import com.iobaf.domain.contract.repository.ContractRepository;
import com.iobaf.domain.contract.vo.ContractVO;
import com.iobaf.domain.customer.entity.Customer;
import com.iobaf.domain.customer.repository.CustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 合同领域服务
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Service
public class ContractDomainService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * 分页查询合同列表
     * 
     * @param page 分页参数
     * @param contractName 合同名称
     * @param contractType 合同类型
     * @param status 状态
     * @param customerId 客户ID
     * @return 合同列表
     */
    public IPage<ContractVO> getContractPage(Page<Contract> page, 
                                           String contractName, 
                                           Integer contractType, 
                                           Integer status,
                                           Long customerId) {
        // 查询合同数据
        IPage<Contract> contractPage = contractRepository.selectContractPage(page, contractName, contractType, status, customerId);
        
        // 获取所有客户ID
        List<Long> customerIds = contractPage.getRecords().stream()
                .map(Contract::getCustomerId)
                .distinct()
                .collect(Collectors.toList());
        
        // 批量查询客户信息
        Map<Long, Customer> customerMap = customerRepository.selectBatchIds(customerIds).stream()
                .collect(Collectors.toMap(Customer::getId, customer -> customer));
        
        // 转换为VO对象
        List<ContractVO> contractVOList = contractPage.getRecords().stream()
                .map(contract -> convertToVO(contract, customerMap))
                .collect(Collectors.toList());
        
        // 创建新的分页结果
        Page<ContractVO> resultPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        resultPage.setRecords(contractVOList);
        
        return resultPage;
    }

    /**
     * 根据ID查询合同详情
     * 
     * @param id 合同ID
     * @return 合同详情
     */
    public ContractVO getContractById(Long id) {
        Contract contract = contractRepository.selectById(id);
        if (contract == null) {
            return null;
        }
        
        // 查询客户信息
        Customer customer = customerRepository.selectById(contract.getCustomerId());
        Map<Long, Customer> customerMap = customer != null ? 
                Map.of(customer.getId(), customer) : Map.of();
        
        return convertToVO(contract, customerMap);
    }

    /**
     * 创建合同
     * 
     * @param contract 合同信息
     * @return 是否成功
     */
    @Transactional
    public boolean createContract(Contract contract) {
        // 设置默认状态
        if (contract.getStatus() == null) {
            contract.setStatus(1);
        }
        
        return contractRepository.insert(contract) > 0;
    }

    /**
     * 更新合同
     * 
     * @param contract 合同信息
     * @return 是否成功
     */
    @Transactional
    public boolean updateContract(Contract contract) {
        return contractRepository.updateById(contract) > 0;
    }

    /**
     * 删除合同
     * 
     * @param id 合同ID
     * @return 是否成功
     */
    @Transactional
    public boolean deleteContract(Long id) {
        return contractRepository.deleteById(id) > 0;
    }

    /**
     * 将实体对象转换为VO对象
     * 
     * @param contract 合同实体
     * @param customerMap 客户信息映射
     * @return 合同VO
     */
    private ContractVO convertToVO(Contract contract, Map<Long, Customer> customerMap) {
        ContractVO vo = new ContractVO();
        BeanUtils.copyProperties(contract, vo);
        
        // 设置客户名称
        Customer customer = customerMap.get(contract.getCustomerId());
        if (customer != null) {
            vo.setCustomerName(customer.getCustomerName());
        }
        
        // 设置合同类型名称
        switch (contract.getContractType()) {
            case 1:
                vo.setContractTypeName("销售合同");
                break;
            case 2:
                vo.setContractTypeName("采购合同");
                break;
            default:
                vo.setContractTypeName("未知");
        }
        
        // 设置状态名称
        switch (contract.getStatus()) {
            case 1:
                vo.setStatusName("草稿");
                break;
            case 2:
                vo.setStatusName("审批中");
                break;
            case 3:
                vo.setStatusName("已生效");
                break;
            case 4:
                vo.setStatusName("已完成");
                break;
            case 5:
                vo.setStatusName("已终止");
                break;
            default:
                vo.setStatusName("未知");
        }
        
        return vo;
    }
} 