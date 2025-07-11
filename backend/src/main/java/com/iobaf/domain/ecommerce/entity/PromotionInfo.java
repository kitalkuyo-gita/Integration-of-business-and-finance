package com.iobaf.domain.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 促销活动信息实体类
 * 记录订单相关的促销活动信息，用于费用分摊和ROI分析
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ecommerce_promotion_info")
public class PromotionInfo {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    @TableField("order_id")
    private Long orderId;

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
     * 促销活动类型（DISCOUNT-折扣, COUPON-优惠券, GIFT-赠品, FLASH_SALE-限时抢购）
     */
    @TableField("promotion_type")
    private String promotionType;

    /**
     * 促销活动渠道（ONLINE-线上, LIVE-直播, WHOLESALE-批发, OFFLINE-线下）
     */
    @TableField("promotion_channel")
    private String promotionChannel;

    /**
     * 促销活动开始时间
     */
    @TableField("promotion_start_time")
    private LocalDateTime promotionStartTime;

    /**
     * 促销活动结束时间
     */
    @TableField("promotion_end_time")
    private LocalDateTime promotionEndTime;

    /**
     * 促销活动预算
     */
    @TableField("promotion_budget")
    private BigDecimal promotionBudget;

    /**
     * 促销活动实际支出
     */
    @TableField("promotion_actual_cost")
    private BigDecimal promotionActualCost;

    /**
     * 促销活动带来的销售额
     */
    @TableField("promotion_sales_amount")
    private BigDecimal promotionSalesAmount;

    /**
     * 促销活动带来的订单数量
     */
    @TableField("promotion_order_count")
    private Integer promotionOrderCount;

    /**
     * 促销活动ROI
     */
    @TableField("promotion_roi")
    private BigDecimal promotionRoi;

    /**
     * 促销活动转化率
     */
    @TableField("promotion_conversion_rate")
    private BigDecimal promotionConversionRate;

    /**
     * 促销活动客单价
     */
    @TableField("promotion_avg_order_value")
    private BigDecimal promotionAvgOrderValue;

    /**
     * 促销活动状态（ACTIVE-进行中, PAUSED-暂停, ENDED-已结束）
     */
    @TableField("promotion_status")
    private String promotionStatus;

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
     * 是否删除（0-未删除，1-已删除）
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
} 