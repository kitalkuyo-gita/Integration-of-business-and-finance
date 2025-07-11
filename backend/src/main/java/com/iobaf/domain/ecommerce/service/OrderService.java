package com.iobaf.domain.ecommerce.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.ecommerce.entity.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 电商订单业务服务接口
 * 提供订单相关的业务逻辑处理
 * 
 * @author iobaf
 * @since 2024-01-01
 */
public interface OrderService {

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
    IPage<Order> getOrderPage(Page<Order> page,
                             String platformType,
                             String channelType,
                             String orderStatus,
                             String customerName,
                             LocalDateTime startTime,
                             LocalDateTime endTime);

    /**
     * 根据ID查询订单详情
     * 
     * @param orderId 订单ID
     * @return 订单详情
     */
    Order getOrderById(Long orderId);

    /**
     * 创建订单
     * 
     * @param order 订单信息
     * @return 创建结果
     */
    boolean createOrder(Order order);

    /**
     * 更新订单信息
     * 
     * @param order 订单信息
     * @return 更新结果
     */
    boolean updateOrder(Order order);

    /**
     * 删除订单
     * 
     * @param orderId 订单ID
     * @return 删除结果
     */
    boolean deleteOrder(Long orderId);

    /**
     * 更新订单状态
     * 
     * @param orderId 订单ID
     * @param orderStatus 订单状态
     * @param updatedBy 更新人ID
     * @return 更新结果
     */
    boolean updateOrderStatus(Long orderId, String orderStatus, Long updatedBy);

    /**
     * 更新支付状态
     * 
     * @param orderId 订单ID
     * @param paymentStatus 支付状态
     * @param paymentTime 支付时间
     * @param updatedBy 更新人ID
     * @return 更新结果
     */
    boolean updatePaymentStatus(Long orderId, String paymentStatus, LocalDateTime paymentTime, Long updatedBy);

    /**
     * 同步外部平台订单
     * 
     * @param platformType 平台类型
     * @param externalOrderData 外部订单数据
     * @return 同步结果
     */
    boolean syncExternalOrder(String platformType, Map<String, Object> externalOrderData);

    /**
     * 获取订单统计数据
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param platformType 平台类型
     * @param channelType 渠道类型
     * @return 统计数据
     */
    Map<String, Object> getOrderStatistics(LocalDateTime startTime,
                                          LocalDateTime endTime,
                                          String platformType,
                                          String channelType);

    /**
     * 获取待发货订单列表
     * 
     * @return 待发货订单列表
     */
    List<Order> getPendingShippingOrders();

    /**
     * 获取即将过期商品订单
     * 
     * @param days 提前预警天数
     * @return 即将过期商品订单列表
     */
    List<Order> getExpiringProductOrders(Integer days);

    /**
     * 批量更新订单状态
     * 
     * @param orderIds 订单ID列表
     * @param orderStatus 订单状态
     * @param updatedBy 更新人ID
     * @return 更新结果
     */
    boolean batchUpdateOrderStatus(List<Long> orderIds, String orderStatus, Long updatedBy);

    /**
     * 生成订单编号
     * 
     * @param platformType 平台类型
     * @return 订单编号
     */
    String generateOrderNo(String platformType);
} 