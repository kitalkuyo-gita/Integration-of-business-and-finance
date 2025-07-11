package com.iobaf.domain.finance.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iobaf.domain.finance.entity.VoucherDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 凭证明细数据访问层
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Mapper
public interface VoucherDetailRepository extends BaseMapper<VoucherDetail> {

    /**
     * 根据凭证ID查询明细列表
     * 
     * @param voucherId 凭证ID
     * @return 明细列表
     */
    List<VoucherDetail> selectByVoucherId(@Param("voucherId") Long voucherId);
} 