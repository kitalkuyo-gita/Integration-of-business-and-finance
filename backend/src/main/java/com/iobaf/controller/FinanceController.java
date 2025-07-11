package com.iobaf.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.common.response.Result;
import com.iobaf.domain.finance.entity.Voucher;
import com.iobaf.domain.finance.service.FinanceDomainService;
import com.iobaf.domain.finance.vo.VoucherVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 财务管理控制器
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    @Autowired
    private FinanceDomainService financeDomainService;

    /**
     * 分页查询凭证列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param voucherNo 凭证号
     * @param voucherType 凭证类型
     * @param status 状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 凭证列表
     */
    @GetMapping("/vouchers")
    public Result<IPage<VoucherVO>> getVoucherList(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String voucherNo,
            @RequestParam(required = false) Integer voucherType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        Page<Voucher> page = new Page<>(current, size);
        IPage<VoucherVO> result = financeDomainService.getVoucherPage(page, voucherNo, voucherType, status, startDate, endDate);
        
        return Result.success(result);
    }

    /**
     * 根据ID查询凭证详情
     * 
     * @param id 凭证ID
     * @return 凭证详情
     */
    @GetMapping("/vouchers/{id}")
    public Result<VoucherVO> getVoucherById(@PathVariable Long id) {
        VoucherVO voucher = financeDomainService.getVoucherById(id);
        if (voucher == null) {
            return Result.error("凭证不存在");
        }
        return Result.success(voucher);
    }

    /**
     * 创建凭证
     * 
     * @param voucherVO 凭证信息
     * @return 操作结果
     */
    @PostMapping("/vouchers")
    public Result<String> createVoucher(@RequestBody VoucherVO voucherVO) {
        boolean success = financeDomainService.createVoucher(voucherVO);
        if (success) {
            return Result.success("凭证创建成功");
        } else {
            return Result.error("凭证创建失败");
        }
    }

    /**
     * 更新凭证
     * 
     * @param id 凭证ID
     * @param voucherVO 凭证信息
     * @return 操作结果
     */
    @PutMapping("/vouchers/{id}")
    public Result<String> updateVoucher(@PathVariable Long id, @RequestBody VoucherVO voucherVO) {
        voucherVO.setId(id);
        boolean success = financeDomainService.updateVoucher(voucherVO);
        if (success) {
            return Result.success("凭证更新成功");
        } else {
            return Result.error("凭证更新失败");
        }
    }

    /**
     * 删除凭证
     * 
     * @param id 凭证ID
     * @return 操作结果
     */
    @DeleteMapping("/vouchers/{id}")
    public Result<String> deleteVoucher(@PathVariable Long id) {
        boolean success = financeDomainService.deleteVoucher(id);
        if (success) {
            return Result.success("凭证删除成功");
        } else {
            return Result.error("凭证删除失败");
        }
    }

    /**
     * 审核凭证
     * 
     * @param id 凭证ID
     * @param auditUserId 审核人ID
     * @return 操作结果
     */
    @PostMapping("/vouchers/{id}/audit")
    public Result<String> auditVoucher(@PathVariable Long id, @RequestParam Long auditUserId) {
        boolean success = financeDomainService.auditVoucher(id, auditUserId);
        if (success) {
            return Result.success("凭证审核成功");
        } else {
            return Result.error("凭证审核失败");
        }
    }
} 