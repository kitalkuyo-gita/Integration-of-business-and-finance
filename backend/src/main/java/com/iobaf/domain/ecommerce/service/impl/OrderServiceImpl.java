package com.iobaf.domain.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.ecommerce.entity.Order;
import com.iobaf.domain.ecommerce.repository.OrderRepository;
import com.iobaf.domain.ecommerce.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 电商订单业务服务实现类
 * 实现订单相关的业务逻辑处理
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // 订单编号计数器，用于生成唯一订单号
    private static final AtomicInteger orderCounter = new AtomicInteger(1);
    private static final DateTimeFormatter ORDER_NO_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    public IPage<Order> getOrderPage(Page<Order> page,
                                   String platformType,
                                   String channelType,
                                   String orderStatus,
                                   String customerName,
                                   LocalDateTime startTime,
                                   LocalDateTime endTime) {
        log.info("查询订单列表，参数：platformType={}, channelType={}, orderStatus={}, customerName={}, startTime={}, endTime={}",
                platformType, channelType, orderStatus, customerName, startTime, endTime);
        
        try {
            return orderRepository.selectOrderPage(page, platformType, channelType, orderStatus, 
                    customerName, startTime, endTime);
        } catch (Exception e) {
            log.error("查询订单列表失败", e);
            throw new RuntimeException("查询订单列表失败", e);
        }
    }

    @Override
    public Order getOrderById(Long orderId) {
        log.info("根据ID查询订单详情，orderId={}", orderId);
        
        try {
            return orderRepository.selectById(orderId);
        } catch (Exception e) {
            log.error("查询订单详情失败，orderId={}", orderId, e);
            throw new RuntimeException("查询订单详情失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrder(Order order) {
        log.info("创建订单，订单信息：{}", order);
        
        try {
            // 生成订单编号
            order.setOrderNo(generateOrderNo(order.getPlatformType()));
            
            // 设置创建时间
            order.setCreatedAt(LocalDateTime.now());
            
            // 保存订单
            int result = orderRepository.insert(order);
            
            if (result > 0) {
                log.info("订单创建成功，orderId={}", order.getId());
                return true;
            } else {
                log.error("订单创建失败");
                return false;
            }
        } catch (Exception e) {
            log.error("创建订单失败", e);
            throw new RuntimeException("创建订单失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrder(Order order) {
        log.info("更新订单信息，orderId={}", order.getId());
        
        try {
            // 设置更新时间
            order.setUpdatedAt(LocalDateTime.now());
            
            int result = orderRepository.updateById(order);
            
            if (result > 0) {
                log.info("订单更新成功，orderId={}", order.getId());
                return true;
            } else {
                log.error("订单更新失败，orderId={}", order.getId());
                return false;
            }
        } catch (Exception e) {
            log.error("更新订单失败，orderId={}", order.getId(), e);
            throw new RuntimeException("更新订单失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrder(Long orderId) {
        log.info("删除订单，orderId={}", orderId);
        
        try {
            int result = orderRepository.deleteById(orderId);
            
            if (result > 0) {
                log.info("订单删除成功，orderId={}", orderId);
                return true;
            } else {
                log.error("订单删除失败，orderId={}", orderId);
                return false;
            }
        } catch (Exception e) {
            log.error("删除订单失败，orderId={}", orderId, e);
            throw new RuntimeException("删除订单失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(Long orderId, String orderStatus, Long updatedBy) {
        log.info("更新订单状态，orderId={}, orderStatus={}, updatedBy={}", orderId, orderStatus, updatedBy);
        
        try {
            int result = orderRepository.updateOrderStatus(orderId, orderStatus, updatedBy);
            
            if (result > 0) {
                log.info("订单状态更新成功，orderId={}, orderStatus={}", orderId, orderStatus);
                return true;
            } else {
                log.error("订单状态更新失败，orderId={}", orderId);
                return false;
            }
        } catch (Exception e) {
            log.error("更新订单状态失败，orderId={}", orderId, e);
            throw new RuntimeException("更新订单状态失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePaymentStatus(Long orderId, String paymentStatus, LocalDateTime paymentTime, Long updatedBy) {
        log.info("更新支付状态，orderId={}, paymentStatus={}, paymentTime={}, updatedBy={}", 
                orderId, paymentStatus, paymentTime, updatedBy);
        
        try {
            int result = orderRepository.updatePaymentStatus(orderId, paymentStatus, paymentTime, updatedBy);
            
            if (result > 0) {
                log.info("支付状态更新成功，orderId={}, paymentStatus={}", orderId, paymentStatus);
                return true;
            } else {
                log.error("支付状态更新失败，orderId={}", orderId);
                return false;
            }
        } catch (Exception e) {
            log.error("更新支付状态失败，orderId={}", orderId, e);
            throw new RuntimeException("更新支付状态失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean syncExternalOrder(String platformType, Map<String, Object> externalOrderData) {
        log.info("同步外部平台订单，platformType={}", platformType);
        
        try {
            // 解析外部订单数据
            String externalOrderNo = (String) externalOrderData.get("orderNo");
            
            // 检查订单是否已存在
            Order existingOrder = orderRepository.selectByExternalOrderNo(externalOrderNo, platformType);
            if (existingOrder != null) {
                log.info("订单已存在，跳过同步，externalOrderNo={}", externalOrderNo);
                return true;
            }
            
            // 创建新订单
            Order order = new Order();
            order.setExternalOrderNo(externalOrderNo);
            order.setPlatformType(platformType);
            order.setChannelType((String) externalOrderData.get("channelType"));
            order.setCustomerName((String) externalOrderData.get("customerName"));
            order.setCustomerPhone((String) externalOrderData.get("customerPhone"));
            order.setTotalAmount(new java.math.BigDecimal(externalOrderData.get("totalAmount").toString()));
            order.setPaidAmount(new java.math.BigDecimal(externalOrderData.get("paidAmount").toString()));
            order.setOrderStatus((String) externalOrderData.get("orderStatus"));
            order.setPaymentStatus((String) externalOrderData.get("paymentStatus"));
            order.setShippingAddress((String) externalOrderData.get("shippingAddress"));
            order.setReceiverName((String) externalOrderData.get("receiverName"));
            order.setReceiverPhone((String) externalOrderData.get("receiverPhone"));
            
            // 保存订单
            boolean result = createOrder(order);
            
            if (result) {
                log.info("外部订单同步成功，externalOrderNo={}", externalOrderNo);
                return true;
            } else {
                log.error("外部订单同步失败，externalOrderNo={}", externalOrderNo);
                return false;
            }
        } catch (Exception e) {
            log.error("同步外部订单失败，platformType={}", platformType, e);
            throw new RuntimeException("同步外部订单失败", e);
        }
    }

    @Override
    public Map<String, Object> getOrderStatistics(LocalDateTime startTime,
                                                LocalDateTime endTime,
                                                String platformType,
                                                String channelType) {
        log.info("获取订单统计数据，startTime={}, endTime={}, platformType={}, channelType={}", 
                startTime, endTime, platformType, channelType);
        
        try {
            List<Order> orders = orderRepository.selectOrderStatistics(startTime, endTime, platformType, channelType);
            
            // 计算统计数据
            Map<String, Object> statistics = new java.util.HashMap<>();
            statistics.put("totalOrders", orders.size());
            statistics.put("totalAmount", orders.stream()
                    .mapToDouble(order -> order.getTotalAmount().doubleValue())
                    .sum());
            statistics.put("paidAmount", orders.stream()
                    .mapToDouble(order -> order.getPaidAmount().doubleValue())
                    .sum());
            
            return statistics;
        } catch (Exception e) {
            log.error("获取订单统计数据失败", e);
            throw new RuntimeException("获取订单统计数据失败", e);
        }
    }

    @Override
    public List<Order> getPendingShippingOrders() {
        log.info("获取待发货订单列表");
        
        try {
            return orderRepository.selectPendingShippingOrders();
        } catch (Exception e) {
            log.error("获取待发货订单列表失败", e);
            throw new RuntimeException("获取待发货订单列表失败", e);
        }
    }

    @Override
    public List<Order> getExpiringProductOrders(Integer days) {
        log.info("获取即将过期商品订单，预警天数={}", days);
        
        try {
            return orderRepository.selectExpiringProductOrders(days);
        } catch (Exception e) {
            log.error("获取即将过期商品订单失败", e);
            throw new RuntimeException("获取即将过期商品订单失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateOrderStatus(List<Long> orderIds, String orderStatus, Long updatedBy) {
        log.info("批量更新订单状态，orderIds={}, orderStatus={}, updatedBy={}", orderIds, orderStatus, updatedBy);
        
        try {
            boolean allSuccess = true;
            for (Long orderId : orderIds) {
                boolean result = updateOrderStatus(orderId, orderStatus, updatedBy);
                if (!result) {
                    allSuccess = false;
                    log.error("批量更新订单状态失败，orderId={}", orderId);
                }
            }
            
            if (allSuccess) {
                log.info("批量更新订单状态成功，orderIds={}", orderIds);
            }
            
            return allSuccess;
        } catch (Exception e) {
            log.error("批量更新订单状态失败", e);
            throw new RuntimeException("批量更新订单状态失败", e);
        }
    }

    @Override
    public String generateOrderNo(String platformType) {
        // 生成订单编号：平台前缀 + 时间戳 + 序号
        String prefix = getPlatformPrefix(platformType);
        String timestamp = LocalDateTime.now().format(ORDER_NO_FORMATTER);
        String sequence = String.format("%04d", orderCounter.getAndIncrement());
        
        String orderNo = prefix + timestamp + sequence;
        log.info("生成订单编号：{}", orderNo);
        
        return orderNo;
    }

    /**
     * 获取平台前缀
     * 
     * @param platformType 平台类型
     * @return 平台前缀
     */
    private String getPlatformPrefix(String platformType) {
        switch (platformType) {
            case "TAOBAO":
                return "TB";
            case "JD":
                return "JD";
            case "PINDUODUO":
                return "PDD";
            case "DOUYIN":
                return "DY";
            default:
                return "EC";
        }
    }
} 