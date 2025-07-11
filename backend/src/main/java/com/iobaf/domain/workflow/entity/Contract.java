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
 * 合同实体类
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("contracts")
public class Contract {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 销售机会ID
     */
    private Long opportunityId;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 合同类型：SALES（销售合同）、SERVICE（服务合同）、MAINTENANCE（维护合同）
     */
    private String contractType;

    /**
     * 合同金额
     */
    private BigDecimal amount;

    /**
     * 币种
     */
    private String currency;

    /**
     * 合同开始日期
     */
    private LocalDate startDate;

    /**
     * 合同结束日期
     */
    private LocalDate endDate;

    /**
     * 付款条件
     */
    private String paymentTerms;

    /**
     * 合同状态：DRAFT（草稿）、PENDING_APPROVAL（待审批）、APPROVED（已审批）、SIGNED（已签署）、EXECUTING（执行中）、COMPLETED（已完成）、TERMINATED（已终止）
     */
    private String status;

    /**
     * 审批状态：PENDING（待审批）、APPROVED（已通过）、REJECTED（已拒绝）
     */
    private String approvalStatus;

    /**
     * 负责人ID
     */
    private Long ownerId;

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