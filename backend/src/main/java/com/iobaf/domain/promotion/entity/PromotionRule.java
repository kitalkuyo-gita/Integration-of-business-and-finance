package com.iobaf.domain.promotion.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 促销费用分摊规则实体类
 * 定义促销费用的分摊规则，支持多维度分摊
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("promotion_rules")
public class PromotionRule {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 规则名称
     */
    @TableField("rule_name")
    private String ruleName;

    /**
     * 规则编码
     */
    @TableField("rule_code")
    private String ruleCode;

    /**
     * 规则类型（SALES_AMOUNT-按销售额, SALES_QUANTITY-按销售量, MARKET_SHARE-按市场份额, CUSTOM-自定义）
     */
    @TableField("rule_type")
    private String ruleType;

    /**
     * 渠道类型（ONLINE-线上, LIVE-直播, WHOLESALE-批发, OFFLINE-线下）
     */
    @TableField("channel_type")
    private String channelType;

    /**
     * 产品类别
     */
    @TableField("product_category")
    private String productCategory;

    /**
     * 分摊比例（百分比，如：30.5表示30.5%）
     */
    @TableField("allocation_ratio")
    private BigDecimal allocationRatio;

    /**
     * 分摊基数（AMOUNT-金额, QUANTITY-数量, CUSTOM-自定义）
     */
    @TableField("allocation_base")
    private String allocationBase;

    /**
     * 自定义分摊公式
     */
    @TableField("custom_formula")
    private String customFormula;

    /**
     * 生效开始时间
     */
    @TableField("effective_start_time")
    private LocalDateTime effectiveStartTime;

    /**
     * 生效结束时间
     */
    @TableField("effective_end_time")
    private LocalDateTime effectiveEndTime;

    /**
     * 规则状态（ACTIVE-生效, INACTIVE-失效, PAUSED-暂停）
     */
    @TableField("rule_status")
    private String ruleStatus;

    /**
     * 优先级（数字越小优先级越高）
     */
    @TableField("priority")
    private Integer priority;

    /**
     * 规则描述
     */
    @TableField("description")
    private String description;

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