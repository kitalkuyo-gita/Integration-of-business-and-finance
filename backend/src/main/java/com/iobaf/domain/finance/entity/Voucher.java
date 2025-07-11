package com.iobaf.domain.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会计凭证实体类
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fin_voucher")
public class Voucher {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 凭证号
     */
    @TableField("voucher_no")
    private String voucherNo;

    /**
     * 凭证日期
     */
    @TableField("voucher_date")
    private LocalDate voucherDate;

    /**
     * 摘要
     */
    @TableField("summary")
    private String summary;

    /**
     * 总金额
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;

    /**
     * 凭证类型：1-收款，2-付款，3-转账，4-其他
     */
    @TableField("voucher_type")
    private Integer voucherType;

    /**
     * 状态：1-草稿，2-已审核，3-已过账
     */
    @TableField("status")
    private Integer status;

    /**
     * 制单人ID
     */
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 审核人ID
     */
    @TableField("audit_user_id")
    private Long auditUserId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 审核时间
     */
    @TableField("audit_time")
    private LocalDateTime auditTime;
} 