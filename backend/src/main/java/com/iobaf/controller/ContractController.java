package com.iobaf.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.common.response.Result;
import com.iobaf.domain.contract.entity.Contract;
import com.iobaf.domain.contract.service.ContractDomainService;
import com.iobaf.domain.contract.vo.ContractVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 合同管理控制器
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/contracts")
public class ContractController {

    @Autowired
    private ContractDomainService contractDomainService;

    /**
     * 分页查询合同列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param contractName 合同名称
     * @param contractType 合同类型
     * @param status 状态
     * @param customerId 客户ID
     * @return 合同列表
     */
    @GetMapping
    public Result<IPage<ContractVO>> getContractList(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String contractName,
            @RequestParam(required = false) Integer contractType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long customerId) {
        
        Page<Contract> page = new Page<>(current, size);
        IPage<ContractVO> result = contractDomainService.getContractPage(page, contractName, contractType, status, customerId);
        
        return Result.success(result);
    }

    /**
     * 根据ID查询合同详情
     * 
     * @param id 合同ID
     * @return 合同详情
     */
    @GetMapping("/{id}")
    public Result<ContractVO> getContractById(@PathVariable Long id) {
        ContractVO contract = contractDomainService.getContractById(id);
        if (contract == null) {
            return Result.error("合同不存在");
        }
        return Result.success(contract);
    }

    /**
     * 创建合同
     * 
     * @param contract 合同信息
     * @return 操作结果
     */
    @PostMapping
    public Result<String> createContract(@RequestBody Contract contract) {
        boolean success = contractDomainService.createContract(contract);
        if (success) {
            return Result.success("合同创建成功");
        } else {
            return Result.error("合同创建失败");
        }
    }

    /**
     * 更新合同
     * 
     * @param id 合同ID
     * @param contract 合同信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public Result<String> updateContract(@PathVariable Long id, @RequestBody Contract contract) {
        contract.setId(id);
        boolean success = contractDomainService.updateContract(contract);
        if (success) {
            return Result.success("合同更新成功");
        } else {
            return Result.error("合同更新失败");
        }
    }

    /**
     * 删除合同
     * 
     * @param id 合同ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteContract(@PathVariable Long id) {
        boolean success = contractDomainService.deleteContract(id);
        if (success) {
            return Result.success("合同删除成功");
        } else {
            return Result.error("合同删除失败");
        }
    }
} 