package com.iobaf.domain.workflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 发票实体类
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("invoices")
public class Invoice {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 发票编号
     */
    private String invoiceNo;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 发票类型：SALES（销售发票）、SERVICE（服务发票）
     */
    private String invoiceType;

    /**
     * 发票金额
     */
    private BigDecimal amount;

    /**
     * 已收款金额
     */
    private BigDecimal receivedAmount;

    /**
     * 发票日期
     */
    private LocalDate invoiceDate;

    /**
     * 到期日期
     */
    private LocalDate dueDate;

    /**
     * 发票状态：DRAFT（草稿）、ISSUED（已开具）、PAID（已付款）、OVERDUE（已逾期）、CANCELLED（已作废）
     */
    private String status;

    /**
     * 付款状态：UNPAID（未付款）、PARTIAL（部分付款）、PAID（已付款）
     */
    private String paymentStatus;

    /**
     * 创建人ID
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 备注
     */
    private String remarks;
} 