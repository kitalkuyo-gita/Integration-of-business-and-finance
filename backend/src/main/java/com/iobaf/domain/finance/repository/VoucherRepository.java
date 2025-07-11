package com.iobaf.domain.finance.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.finance.entity.Voucher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 凭证数据访问层
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Mapper
public interface VoucherRepository extends BaseMapper<Voucher> {

    /**
     * 分页查询凭证列表
     * 
     * @param page 分页参数
     * @param voucherNo 凭证号（模糊查询）
     * @param voucherType 凭证类型
     * @param status 状态
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分页结果
     */
    IPage<Voucher> selectVoucherPage(Page<Voucher> page, 
                                    @Param("voucherNo") String voucherNo,
                                    @Param("voucherType") Integer voucherType,
                                    @Param("status") Integer status,
                                    @Param("startDate") String startDate,
                                    @Param("endDate") String endDate);
} 