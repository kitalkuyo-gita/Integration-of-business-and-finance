package com.iobaf.domain.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 凭证明细实体类
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fin_voucher_detail")
public class VoucherDetail {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 凭证ID
     */
    @TableField("voucher_id")
    private Long voucherId;

    /**
     * 科目ID
     */
    @TableField("account_id")
    private Long accountId;

    /**
     * 方向：1-借，2-贷
     */
    @TableField("direction")
    private Integer direction;

    /**
     * 金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 摘要
     */
    @TableField("summary")
    private String summary;
} 