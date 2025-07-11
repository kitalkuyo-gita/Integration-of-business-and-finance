package com.iobaf.domain.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单商品明细实体类
 * 记录订单中的商品信息，包括商品基本信息、价格、数量等
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ecommerce_order_items")
public class OrderItem {

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
     * 商品ID
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 商品编码
     */
    @TableField("product_code")
    private String productCode;

    /**
     * 商品名称
     */
    @TableField("product_name")
    private String productName;

    /**
     * 商品规格
     */
    @TableField("product_spec")
    private String productSpec;

    /**
     * 商品图片URL
     */
    @TableField("product_image")
    private String productImage;

    /**
     * 商品单价
     */
    @TableField("unit_price")
    private BigDecimal unitPrice;

    /**
     * 购买数量
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * 商品总价
     */
    @TableField("total_price")
    private BigDecimal totalPrice;

    /**
     * 优惠金额
     */
    @TableField("discount_amount")
    private BigDecimal discountAmount;

    /**
     * 实付金额
     */
    @TableField("paid_amount")
    private BigDecimal paidAmount;

    /**
     * 批次号（用于保质期管理）
     */
    @TableField("batch_no")
    private String batchNo;

    /**
     * 生产日期
     */
    @TableField("production_date")
    private LocalDateTime productionDate;

    /**
     * 保质期（天数）
     */
    @TableField("shelf_life_days")
    private Integer shelfLifeDays;

    /**
     * 过期日期
     */
    @TableField("expiry_date")
    private LocalDateTime expiryDate;

    /**
     * 是否赠品（0-否，1-是）
     */
    @TableField("is_gift")
    private Integer isGift;

    /**
     * 赠品来源订单ID
     */
    @TableField("gift_source_order_id")
    private Long giftSourceOrderId;

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