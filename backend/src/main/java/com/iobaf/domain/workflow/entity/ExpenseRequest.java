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
 * 费用报销申请实体类
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("expense_requests")
public class ExpenseRequest {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 申请编号
     */
    private String requestNo;

    /**
     * 申请人ID
     */
    private Long employeeId;

    /**
     * 费用类型：TRAVEL（差旅费）、MEAL（餐饮费）、OFFICE（办公费）、TRANSPORT（交通费）、OTHER（其他）
     */
    private String expenseType;

    /**
     * 费用金额
     */
    private BigDecimal amount;

    /**
     * 费用描述
     */
    private String description;

    /**
     * 费用发生日期
     */
    private LocalDate expenseDate;

    /**
     * 申请状态：DRAFT（草稿）、SUBMITTED（已提交）、MANAGER_APPROVED（经理已审批）、FINANCE_REVIEWED（财务已审核）、CEO_APPROVED（CEO已审批）、PAID（已付款）、REJECTED（已拒绝）
     */
    private String status;

    /**
     * 审批状态：PENDING（待审批）、APPROVED（已通过）、REJECTED（已拒绝）
     */
    private String approvalStatus;

    /**
     * 部门经理ID
     */
    private Long managerId;

    /**
     * 财务审核人ID
     */
    private Long financeId;

    /**
     * CEO审批人ID
     */
    private Long ceoId;

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