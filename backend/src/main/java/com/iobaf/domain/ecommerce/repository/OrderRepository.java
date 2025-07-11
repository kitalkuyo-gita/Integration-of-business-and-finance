package com.iobaf.domain.ecommerce.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.ecommerce.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 电商订单数据访问层接口
 * 提供订单数据的增删改查操作
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Mapper
public interface OrderRepository extends BaseMapper<Order> {

    /**
     * 分页查询订单列表
     * 
     * @param page 分页参数
     * @param platformType 平台类型
     * @param channelType 渠道类型
     * @param orderStatus 订单状态
     * @param customerName 客户姓名
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单分页数据
     */
    IPage<Order> selectOrderPage(Page<Order> page,
                                @Param("platformType") String platformType,
                                @Param("channelType") String channelType,
                                @Param("orderStatus") String orderStatus,
                                @Param("customerName") String customerName,
                                @Param("startTime") LocalDateTime startTime,
                                @Param("endTime") LocalDateTime endTime);

    /**
     * 根据外部订单号查询订单
     * 
     * @param externalOrderNo 外部订单号
     * @param platformType 平台类型
     * @return 订单信息
     */
    Order selectByExternalOrderNo(@Param("externalOrderNo") String externalOrderNo,
                                 @Param("platformType") String platformType);

    /**
     * 查询指定时间范围内的订单统计
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param platformType 平台类型
     * @param channelType 渠道类型
     * @return 订单统计数据
     */
    List<Order> selectOrderStatistics(@Param("startTime") LocalDateTime startTime,
                                    @Param("endTime") LocalDateTime endTime,
                                    @Param("platformType") String platformType,
                                    @Param("channelType") String channelType);

    /**
     * 查询待发货订单列表
     * 
     * @return 待发货订单列表
     */
    List<Order> selectPendingShippingOrders();

    /**
     * 查询即将过期的商品订单
     * 
     * @param days 提前预警天数
     * @return 即将过期商品订单列表
     */
    List<Order> selectExpiringProductOrders(@Param("days") Integer days);

    /**
     * 更新订单状态
     * 
     * @param orderId 订单ID
     * @param orderStatus 订单状态
     * @param updatedBy 更新人ID
     * @return 更新结果
     */
    int updateOrderStatus(@Param("orderId") Long orderId,
                         @Param("orderStatus") String orderStatus,
                         @Param("updatedBy") Long updatedBy);

    /**
     * 更新支付状态
     * 
     * @param orderId 订单ID
     * @param paymentStatus 支付状态
     * @param paymentTime 支付时间
     * @param updatedBy 更新人ID
     * @return 更新结果
     */
    int updatePaymentStatus(@Param("orderId") Long orderId,
                           @Param("paymentStatus") String paymentStatus,
                           @Param("paymentTime") LocalDateTime paymentTime,
                           @Param("updatedBy") Long updatedBy);
} 