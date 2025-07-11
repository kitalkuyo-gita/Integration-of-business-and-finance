package com.iobaf.domain.workflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购申请实体类
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("purchase_requests")
public class PurchaseRequest {

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
     * 申请部门ID
     */
    private Long departmentId;

    /**
     * 申请人ID
     */
    private Long requesterId;

    /**
     * 物品名称
     */
    private String itemName;

    /**
     * 物品描述
     */
    private String itemDescription;

    /**
     * 规格型号
     */
    private String specification;

    /**
     * 申请数量
     */
    private Integer quantity;

    /**
     * 单位
     */
    private String unit;

    /**
     * 预估单价
     */
    private BigDecimal estimatedUnitPrice;

    /**
     * 预估总金额
     */
    private BigDecimal estimatedTotalAmount;

    /**
     * 需求日期
     */
    private LocalDateTime requiredDate;

    /**
     * 申请原因
     */
    private String reason;

    /**
     * 申请状态：DRAFT（草稿）、SUBMITTED（已提交）、APPROVED（已审批）、REJECTED（已拒绝）、PROCESSING（处理中）、COMPLETED（已完成）
     */
    private String status;

    /**
     * 审批状态：PENDING（待审批）、APPROVED（已通过）、REJECTED（已拒绝）
     */
    private String approvalStatus;

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