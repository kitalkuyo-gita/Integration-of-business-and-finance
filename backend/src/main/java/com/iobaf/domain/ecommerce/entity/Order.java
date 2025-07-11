package com.iobaf.domain.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 电商订单实体类
 * 支持多渠道电商平台订单的统一管理
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ecommerce_orders")
public class Order {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号（系统生成）
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 外部订单号（电商平台订单号）
     */
    @TableField("external_order_no")
    private String externalOrderNo;

    /**
     * 电商平台类型（TAOBAO-淘宝, JD-京东, PINDUODUO-拼多多, DOUYIN-抖音）
     */
    @TableField("platform_type")
    private String platformType;

    /**
     * 渠道类型（ONLINE-线上, LIVE-直播, WHOLESALE-批发）
     */
    @TableField("channel_type")
    private String channelType;

    /**
     * 客户ID
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 客户姓名
     */
    @TableField("customer_name")
    private String customerName;

    /**
     * 客户手机号
     */
    @TableField("customer_phone")
    private String customerPhone;

    /**
     * 订单总金额
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;

    /**
     * 实付金额
     */
    @TableField("paid_amount")
    private BigDecimal paidAmount;

    /**
     * 优惠金额
     */
    @TableField("discount_amount")
    private BigDecimal discountAmount;

    /**
     * 运费
     */
    @TableField("freight_amount")
    private BigDecimal freightAmount;

    /**
     * 订单状态（PENDING-待付款, PAID-已付款, SHIPPED-已发货, DELIVERED-已送达, COMPLETED-已完成, CANCELLED-已取消）
     */
    @TableField("order_status")
    private String orderStatus;

    /**
     * 支付状态（UNPAID-未支付, PAID-已支付, REFUNDED-已退款）
     */
    @TableField("payment_status")
    private String paymentStatus;

    /**
     * 支付方式（ALIPAY-支付宝, WECHAT-微信, BANK-银行转账）
     */
    @TableField("payment_method")
    private String paymentMethod;

    /**
     * 支付时间
     */
    @TableField("payment_time")
    private LocalDateTime paymentTime;

    /**
     * 发货时间
     */
    @TableField("shipping_time")
    private LocalDateTime shippingTime;

    /**
     * 送达时间
     */
    @TableField("delivery_time")
    private LocalDateTime deliveryTime;

    /**
     * 收货地址
     */
    @TableField("shipping_address")
    private String shippingAddress;

    /**
     * 收货人姓名
     */
    @TableField("receiver_name")
    private String receiverName;

    /**
     * 收货人电话
     */
    @TableField("receiver_phone")
    private String receiverPhone;

    /**
     * 物流公司
     */
    @TableField("logistics_company")
    private String logisticsCompany;

    /**
     * 物流单号
     */
    @TableField("tracking_number")
    private String trackingNumber;

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

    /**
     * 订单商品列表（非数据库字段）
     */
    @TableField(exist = false)
    private List<OrderItem> orderItems;

    /**
     * 促销活动信息（非数据库字段）
     */
    @TableField(exist = false)
    private List<PromotionInfo> promotions;
} 