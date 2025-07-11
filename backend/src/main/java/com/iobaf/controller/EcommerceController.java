package com.iobaf.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.common.response.Result;
import com.iobaf.domain.ecommerce.entity.Order;
import com.iobaf.domain.ecommerce.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 电商订单管理控制器
 * 提供电商订单的增删改查、状态更新、数据同步等功能
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/ecommerce")
public class EcommerceController {

    @Autowired
    private OrderService orderService;

    /**
     * 分页查询订单列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param platformType 平台类型
     * @param channelType 渠道类型
     * @param orderStatus 订单状态
     * @param customerName 客户姓名
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 订单分页数据
     */
    @GetMapping("/orders")
    public Result<IPage<Order>> getOrderPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String platformType,
            @RequestParam(required = false) String channelType,
            @RequestParam(required = false) String orderStatus,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        
        log.info("查询订单列表，参数：current={}, size={}, platformType={}, channelType={}, orderStatus={}, customerName={}, startTime={}, endTime={}",
                current, size, platformType, channelType, orderStatus, customerName, startTime, endTime);
        
        try {
            Page<Order> page = new Page<>(current, size);
            IPage<Order> result = orderService.getOrderPage(page, platformType, channelType, orderStatus, 
                    customerName, startTime, endTime);
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询订单列表失败", e);
            return Result.error("查询订单列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询订单详情
     * 
     * @param orderId 订单ID
     * @return 订单详情
     */
    @GetMapping("/orders/{orderId}")
    public Result<Order> getOrderById(@PathVariable Long orderId) {
        log.info("查询订单详情，orderId={}", orderId);
        
        try {
            Order order = orderService.getOrderById(orderId);
            if (order != null) {
                return Result.success(order);
            } else {
                return Result.error("订单不存在");
            }
        } catch (Exception e) {
            log.error("查询订单详情失败，orderId={}", orderId, e);
            return Result.error("查询订单详情失败：" + e.getMessage());
        }
    }

    /**
     * 创建订单
     * 
     * @param order 订单信息
     * @return 创建结果
     */
    @PostMapping("/orders")
    public Result<Boolean> createOrder(@RequestBody Order order) {
        log.info("创建订单，订单信息：{}", order);
        
        try {
            boolean result = orderService.createOrder(order);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("创建订单失败");
            }
        } catch (Exception e) {
            log.error("创建订单失败", e);
            return Result.error("创建订单失败：" + e.getMessage());
        }
    }

    /**
     * 更新订单信息
     * 
     * @param orderId 订单ID
     * @param order 订单信息
     * @return 更新结果
     */
    @PutMapping("/orders/{orderId}")
    public Result<Boolean> updateOrder(@PathVariable Long orderId, @RequestBody Order order) {
        log.info("更新订单信息，orderId={}", orderId);
        
        try {
            order.setId(orderId);
            boolean result = orderService.updateOrder(order);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("更新订单失败");
            }
        } catch (Exception e) {
            log.error("更新订单失败，orderId={}", orderId, e);
            return Result.error("更新订单失败：" + e.getMessage());
        }
    }

    /**
     * 删除订单
     * 
     * @param orderId 订单ID
     * @return 删除结果
     */
    @DeleteMapping("/orders/{orderId}")
    public Result<Boolean> deleteOrder(@PathVariable Long orderId) {
        log.info("删除订单，orderId={}", orderId);
        
        try {
            boolean result = orderService.deleteOrder(orderId);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("删除订单失败");
            }
        } catch (Exception e) {
            log.error("删除订单失败，orderId={}", orderId, e);
            return Result.error("删除订单失败：" + e.getMessage());
        }
    }

    /**
     * 更新订单状态
     * 
     * @param orderId 订单ID
     * @param orderStatus 订单状态
     * @return 更新结果
     */
    @PutMapping("/orders/{orderId}/status")
    public Result<Boolean> updateOrderStatus(@PathVariable Long orderId, 
                                           @RequestParam String orderStatus,
                                           @RequestParam Long updatedBy) {
        log.info("更新订单状态，orderId={}, orderStatus={}, updatedBy={}", orderId, orderStatus, updatedBy);
        
        try {
            boolean result = orderService.updateOrderStatus(orderId, orderStatus, updatedBy);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("更新订单状态失败");
            }
        } catch (Exception e) {
            log.error("更新订单状态失败，orderId={}", orderId, e);
            return Result.error("更新订单状态失败：" + e.getMessage());
        }
    }

    /**
     * 更新支付状态
     * 
     * @param orderId 订单ID
     * @param paymentStatus 支付状态
     * @param paymentTime 支付时间
     * @return 更新结果
     */
    @PutMapping("/orders/{orderId}/payment")
    public Result<Boolean> updatePaymentStatus(@PathVariable Long orderId,
                                             @RequestParam String paymentStatus,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime paymentTime,
                                             @RequestParam Long updatedBy) {
        log.info("更新支付状态，orderId={}, paymentStatus={}, paymentTime={}, updatedBy={}", 
                orderId, paymentStatus, paymentTime, updatedBy);
        
        try {
            boolean result = orderService.updatePaymentStatus(orderId, paymentStatus, paymentTime, updatedBy);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("更新支付状态失败");
            }
        } catch (Exception e) {
            log.error("更新支付状态失败，orderId={}", orderId, e);
            return Result.error("更新支付状态失败：" + e.getMessage());
        }
    }

    /**
     * 同步外部平台订单
     * 
     * @param platformType 平台类型
     * @param orderData 订单数据
     * @return 同步结果
     */
    @PostMapping("/orders/sync/{platformType}")
    public Result<Boolean> syncExternalOrder(@PathVariable String platformType,
                                           @RequestBody Map<String, Object> orderData) {
        log.info("同步外部平台订单，platformType={}", platformType);
        
        try {
            boolean result = orderService.syncExternalOrder(platformType, orderData);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("同步外部订单失败");
            }
        } catch (Exception e) {
            log.error("同步外部订单失败，platformType={}", platformType, e);
            return Result.error("同步外部订单失败：" + e.getMessage());
        }
    }

    /**
     * 获取订单统计数据
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param platformType 平台类型
     * @param channelType 渠道类型
     * @return 统计数据
     */
    @GetMapping("/orders/statistics")
    public Result<Map<String, Object>> getOrderStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) String platformType,
            @RequestParam(required = false) String channelType) {
        
        log.info("获取订单统计数据，startTime={}, endTime={}, platformType={}, channelType={}", 
                startTime, endTime, platformType, channelType);
        
        try {
            Map<String, Object> statistics = orderService.getOrderStatistics(startTime, endTime, platformType, channelType);
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取订单统计数据失败", e);
            return Result.error("获取订单统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取待发货订单列表
     * 
     * @return 待发货订单列表
     */
    @GetMapping("/orders/pending-shipping")
    public Result<List<Order>> getPendingShippingOrders() {
        log.info("获取待发货订单列表");
        
        try {
            List<Order> orders = orderService.getPendingShippingOrders();
            return Result.success(orders);
        } catch (Exception e) {
            log.error("获取待发货订单列表失败", e);
            return Result.error("获取待发货订单列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取即将过期商品订单
     * 
     * @param days 提前预警天数
     * @return 即将过期商品订单列表
     */
    @GetMapping("/orders/expiring")
    public Result<List<Order>> getExpiringProductOrders(@RequestParam(defaultValue = "30") Integer days) {
        log.info("获取即将过期商品订单，预警天数={}", days);
        
        try {
            List<Order> orders = orderService.getExpiringProductOrders(days);
            return Result.success(orders);
        } catch (Exception e) {
            log.error("获取即将过期商品订单失败", e);
            return Result.error("获取即将过期商品订单失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新订单状态
     * 
     * @param orderIds 订单ID列表
     * @param orderStatus 订单状态
     * @param updatedBy 更新人ID
     * @return 更新结果
     */
    @PutMapping("/orders/batch-status")
    public Result<Boolean> batchUpdateOrderStatus(@RequestParam List<Long> orderIds,
                                                @RequestParam String orderStatus,
                                                @RequestParam Long updatedBy) {
        log.info("批量更新订单状态，orderIds={}, orderStatus={}, updatedBy={}", orderIds, orderStatus, updatedBy);
        
        try {
            boolean result = orderService.batchUpdateOrderStatus(orderIds, orderStatus, updatedBy);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("批量更新订单状态失败");
            }
        } catch (Exception e) {
            log.error("批量更新订单状态失败", e);
            return Result.error("批量更新订单状态失败：" + e.getMessage());
        }
    }
} 