package com.iobaf.domain.finance.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 凭证明细视图对象
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Data
public class VoucherDetailVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 凭证ID
     */
    private Long voucherId;

    /**
     * 科目ID
     */
    private Long accountId;

    /**
     * 科目编码
     */
    private String accountCode;

    /**
     * 科目名称
     */
    private String accountName;

    /**
     * 方向：1-借，2-贷
     */
    private Integer direction;

    /**
     * 方向名称
     */
    private String directionName;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 摘要
     */
    private String summary;
} 