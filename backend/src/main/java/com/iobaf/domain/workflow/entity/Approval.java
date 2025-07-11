package com.iobaf.domain.workflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 审批记录实体类
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("approvals")
public class Approval {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业务类型：CONTRACT（合同）、PURCHASE_REQUEST（采购申请）、EXPENSE_REQUEST（费用报销）
     */
    private String businessType;

    /**
     * 业务ID
     */
    private Long businessId;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 审批级别：1（一级审批）、2（二级审批）、3（三级审批）
     */
    private Integer approvalLevel;

    /**
     * 审批结果：APPROVED（通过）、REJECTED（拒绝）
     */
    private String result;

    /**
     * 审批意见
     */
    private String comments;

    /**
     * 审批时间
     */
    private LocalDateTime approvedAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
} 