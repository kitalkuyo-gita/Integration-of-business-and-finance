package com.iobaf.domain.finance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.finance.entity.Voucher;
import com.iobaf.domain.finance.entity.VoucherDetail;
import com.iobaf.domain.finance.repository.VoucherRepository;
import com.iobaf.domain.finance.repository.VoucherDetailRepository;
import com.iobaf.domain.finance.vo.VoucherVO;
import com.iobaf.domain.finance.vo.VoucherDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 财务领域服务
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Service
public class FinanceDomainService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private VoucherDetailRepository voucherDetailRepository;

    /**
     * 分页查询凭证列表
     * 
     * @param page 分页参数
     * @param voucherNo 凭证号
     * @param voucherType 凭证类型
     * @param status 状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 凭证列表
     */
    public IPage<VoucherVO> getVoucherPage(Page<Voucher> page, 
                                          String voucherNo, 
                                          Integer voucherType, 
                                          Integer status,
                                          String startDate,
                                          String endDate) {
        // 查询凭证数据
        IPage<Voucher> voucherPage = voucherRepository.selectVoucherPage(page, voucherNo, voucherType, status, startDate, endDate);
        
        // 转换为VO对象
        List<VoucherVO> voucherVOList = voucherPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        // 创建新的分页结果
        Page<VoucherVO> resultPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        resultPage.setRecords(voucherVOList);
        
        return resultPage;
    }

    /**
     * 根据ID查询凭证详情
     * 
     * @param id 凭证ID
     * @return 凭证详情
     */
    public VoucherVO getVoucherById(Long id) {
        Voucher voucher = voucherRepository.selectById(id);
        if (voucher == null) {
            return null;
        }
        return convertToVO(voucher);
    }

    /**
     * 创建凭证
     * 
     * @param voucherVO 凭证信息
     * @return 是否成功
     */
    @Transactional
    public boolean createVoucher(VoucherVO voucherVO) {
        Voucher voucher = new Voucher();
        BeanUtils.copyProperties(voucherVO, voucher);
        
        // 设置默认状态
        if (voucher.getStatus() == null) {
            voucher.setStatus(1);
        }
        
        // 保存凭证
        boolean success = voucherRepository.insert(voucher) > 0;
        if (!success) {
            return false;
        }
        
        // 保存明细
        if (voucherVO.getDetails() != null && !voucherVO.getDetails().isEmpty()) {
            for (VoucherDetailVO detailVO : voucherVO.getDetails()) {
                VoucherDetail detail = new VoucherDetail();
                BeanUtils.copyProperties(detailVO, detail);
                detail.setVoucherId(voucher.getId());
                voucherDetailRepository.insert(detail);
            }
        }
        
        return true;
    }

    /**
     * 更新凭证
     * 
     * @param voucherVO 凭证信息
     * @return 是否成功
     */
    @Transactional
    public boolean updateVoucher(VoucherVO voucherVO) {
        Voucher voucher = new Voucher();
        BeanUtils.copyProperties(voucherVO, voucher);
        
        // 更新凭证
        boolean success = voucherRepository.updateById(voucher) > 0;
        if (!success) {
            return false;
        }
        
        // 删除原有明细
        voucherDetailRepository.delete(
            voucherDetailRepository.selectList(null).stream()
                .filter(detail -> detail.getVoucherId().equals(voucher.getId()))
                .collect(Collectors.toList())
        );
        
        // 保存新明细
        if (voucherVO.getDetails() != null && !voucherVO.getDetails().isEmpty()) {
            for (VoucherDetailVO detailVO : voucherVO.getDetails()) {
                VoucherDetail detail = new VoucherDetail();
                BeanUtils.copyProperties(detailVO, detail);
                detail.setVoucherId(voucher.getId());
                voucherDetailRepository.insert(detail);
            }
        }
        
        return true;
    }

    /**
     * 删除凭证
     * 
     * @param id 凭证ID
     * @return 是否成功
     */
    @Transactional
    public boolean deleteVoucher(Long id) {
        // 删除明细
        voucherDetailRepository.delete(
            voucherDetailRepository.selectList(null).stream()
                .filter(detail -> detail.getVoucherId().equals(id))
                .collect(Collectors.toList())
        );
        
        // 删除凭证
        return voucherRepository.deleteById(id) > 0;
    }

    /**
     * 审核凭证
     * 
     * @param id 凭证ID
     * @param auditUserId 审核人ID
     * @return 是否成功
     */
    @Transactional
    public boolean auditVoucher(Long id, Long auditUserId) {
        Voucher voucher = voucherRepository.selectById(id);
        if (voucher == null) {
            return false;
        }
        
        voucher.setStatus(2);
        voucher.setAuditUserId(auditUserId);
        voucher.setAuditTime(java.time.LocalDateTime.now());
        
        return voucherRepository.updateById(voucher) > 0;
    }

    /**
     * 将实体对象转换为VO对象
     * 
     * @param voucher 凭证实体
     * @return 凭证VO
     */
    private VoucherVO convertToVO(Voucher voucher) {
        VoucherVO vo = new VoucherVO();
        BeanUtils.copyProperties(voucher, vo);
        
        // 设置凭证类型名称
        switch (voucher.getVoucherType()) {
            case 1:
                vo.setVoucherTypeName("收款");
                break;
            case 2:
                vo.setVoucherTypeName("付款");
                break;
            case 3:
                vo.setVoucherTypeName("转账");
                break;
            case 4:
                vo.setVoucherTypeName("其他");
                break;
            default:
                vo.setVoucherTypeName("未知");
        }
        
        // 设置状态名称
        switch (voucher.getStatus()) {
            case 1:
                vo.setStatusName("草稿");
                break;
            case 2:
                vo.setStatusName("已审核");
                break;
            case 3:
                vo.setStatusName("已过账");
                break;
            default:
                vo.setStatusName("未知");
        }
        
        // 查询明细
        List<VoucherDetail> details = voucherDetailRepository.selectByVoucherId(voucher.getId());
        List<VoucherDetailVO> detailVOs = details.stream()
                .map(this::convertDetailToVO)
                .collect(Collectors.toList());
        vo.setDetails(detailVOs);
        
        return vo;
    }

    /**
     * 将明细实体对象转换为VO对象
     * 
     * @param detail 明细实体
     * @return 明细VO
     */
    private VoucherDetailVO convertDetailToVO(VoucherDetail detail) {
        VoucherDetailVO vo = new VoucherDetailVO();
        BeanUtils.copyProperties(detail, vo);
        
        // 设置方向名称
        vo.setDirectionName(detail.getDirection() == 1 ? "借" : "贷");
        
        return vo;
    }
} 