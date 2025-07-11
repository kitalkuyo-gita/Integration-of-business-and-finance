package com.iobaf.domain.promotion.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 促销费用分摊记录实体类
 * 记录每次促销费用的分摊结果
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("promotion_allocations")
public class PromotionAllocation {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分摊批次号
     */
    @TableField("allocation_batch_no")
    private String allocationBatchNo;

    /**
     * 促销活动ID
     */
    @TableField("promotion_id")
    private Long promotionId;

    /**
     * 促销活动名称
     */
    @TableField("promotion_name")
    private String promotionName;

    /**
     * 分摊规则ID
     */
    @TableField("rule_id")
    private Long ruleId;

    /**
     * 分摊规则名称
     */
    @TableField("rule_name")
    private String ruleName;

    /**
     * 渠道类型
     */
    @TableField("channel_type")
    private String channelType;

    /**
     * 产品类别
     */
    @TableField("product_category")
    private String productCategory;

    /**
     * 分摊基数金额
     */
    @TableField("base_amount")
    private BigDecimal baseAmount;

    /**
     * 分摊基数数量
     */
    @TableField("base_quantity")
    private Integer baseQuantity;

    /**
     * 分摊比例
     */
    @TableField("allocation_ratio")
    private BigDecimal allocationRatio;

    /**
     * 分摊金额
     */
    @TableField("allocated_amount")
    private BigDecimal allocatedAmount;

    /**
     * 分摊数量
     */
    @TableField("allocated_quantity")
    private Integer allocatedQuantity;

    /**
     * 分摊时间
     */
    @TableField("allocation_time")
    private LocalDateTime allocationTime;

    /**
     * 分摊状态（PENDING-待分摊, PROCESSING-分摊中, COMPLETED-已完成, FAILED-失败）
     */
    @TableField("allocation_status")
    private String allocationStatus;

    /**
     * 会计凭证ID
     */
    @TableField("voucher_id")
    private Long voucherId;

    /**
     * 备注信息
     */
    @TableField("remarks")
    private String remarks;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 创建人ID
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 更新人ID
     */
    @TableField("updated_by")
    private Long updatedBy;

    /**
     * 是否删除（0-未删除，1-已删除）
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
} 