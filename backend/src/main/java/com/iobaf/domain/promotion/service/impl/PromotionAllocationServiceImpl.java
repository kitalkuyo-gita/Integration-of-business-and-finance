package com.iobaf.domain.promotion.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.promotion.entity.PromotionAllocation;
import com.iobaf.domain.promotion.repository.PromotionAllocationRepository;
import com.iobaf.domain.promotion.service.PromotionAllocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 促销费用分摊服务实现类
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Slf4j
@Service
public class PromotionAllocationServiceImpl implements PromotionAllocationService {

    @Autowired
    private PromotionAllocationRepository promotionAllocationRepository;

    @Override
    public IPage<PromotionAllocation> getAllocationPage(Page<PromotionAllocation> page,
                                                       Long promotionId,
                                                       String channelType,
                                                       String allocationStatus) {
        log.info("查询分摊记录列表，参数：promotionId={}, channelType={}, allocationStatus={}", 
                promotionId, channelType, allocationStatus);
        
        try {
            return promotionAllocationRepository.selectAllocationPage(page, promotionId, channelType, allocationStatus);
        } catch (Exception e) {
            log.error("查询分摊记录列表失败", e);
            throw new RuntimeException("查询分摊记录列表失败", e);
        }
    }

    @Override
    public PromotionAllocation getAllocationById(Long allocationId) {
        log.info("根据ID查询分摊记录详情，allocationId={}", allocationId);
        
        try {
            return promotionAllocationRepository.selectById(allocationId);
        } catch (Exception e) {
            log.error("查询分摊记录详情失败，allocationId={}", allocationId, e);
            throw new RuntimeException("查询分摊记录详情失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createAllocation(PromotionAllocation allocation) {
        log.info("创建分摊记录，记录信息：{}", allocation);
        
        try {
            // 设置创建时间
            allocation.setCreatedAt(LocalDateTime.now());
            
            int result = promotionAllocationRepository.insert(allocation);
            
            if (result > 0) {
                log.info("分摊记录创建成功，allocationId={}", allocation.getId());
                return true;
            } else {
                log.error("分摊记录创建失败");
                return false;
            }
        } catch (Exception e) {
            log.error("创建分摊记录失败", e);
            throw new RuntimeException("创建分摊记录失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAllocation(PromotionAllocation allocation) {
        log.info("更新分摊记录信息，allocationId={}", allocation.getId());
        
        try {
            // 设置更新时间
            allocation.setUpdatedAt(LocalDateTime.now());
            
            int result = promotionAllocationRepository.updateById(allocation);
            
            if (result > 0) {
                log.info("分摊记录更新成功，allocationId={}", allocation.getId());
                return true;
            } else {
                log.error("分摊记录更新失败，allocationId={}", allocation.getId());
                return false;
            }
        } catch (Exception e) {
            log.error("更新分摊记录失败，allocationId={}", allocation.getId(), e);
            throw new RuntimeException("更新分摊记录失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAllocation(Long allocationId) {
        log.info("删除分摊记录，allocationId={}", allocationId);
        
        try {
            int result = promotionAllocationRepository.deleteById(allocationId);
            
            if (result > 0) {
                log.info("分摊记录删除成功，allocationId={}", allocationId);
                return true;
            } else {
                log.error("分摊记录删除失败，allocationId={}", allocationId);
                return false;
            }
        } catch (Exception e) {
            log.error("删除分摊记录失败，allocationId={}", allocationId, e);
            throw new RuntimeException("删除分摊记录失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateAllocation(Long promotionId, LocalDateTime startTime, LocalDateTime endTime) {
        log.info("执行费用分摊计算，promotionId={}, startTime={}, endTime={}", promotionId, startTime, endTime);
        
        try {
            // 模拟分摊计算逻辑
            // 实际项目中需要根据具体的业务规则进行计算
            
            // 创建分摊记录
            PromotionAllocation allocation = new PromotionAllocation();
            allocation.setPromotionId(promotionId);
            allocation.setChannelType("ONLINE");
            allocation.setAllocationAmount(10000.0);
            allocation.setAllocationRatio(0.3);
            allocation.setAllocationStatus("COMPLETED");
            allocation.setStartTime(startTime);
            allocation.setEndTime(endTime);
            allocation.setCreatedAt(LocalDateTime.now());
            
            int result = promotionAllocationRepository.insert(allocation);
            
            if (result > 0) {
                log.info("费用分摊计算完成，allocationId={}", allocation.getId());
                return true;
            } else {
                log.error("费用分摊计算失败");
                return false;
            }
        } catch (Exception e) {
            log.error("执行费用分摊计算失败", e);
            throw new RuntimeException("执行费用分摊计算失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchCalculateAllocation(List<Long> promotionIds, LocalDateTime startTime, LocalDateTime endTime) {
        log.info("批量执行费用分摊计算，promotionIds={}, startTime={}, endTime={}", promotionIds, startTime, endTime);
        
        try {
            int successCount = 0;
            for (Long promotionId : promotionIds) {
                try {
                    if (calculateAllocation(promotionId, startTime, endTime)) {
                        successCount++;
                    }
                } catch (Exception e) {
                    log.error("批量分摊计算失败，promotionId={}", promotionId, e);
                }
            }
            
            log.info("批量费用分摊计算完成，成功数量={}", successCount);
            return successCount > 0;
        } catch (Exception e) {
            log.error("批量执行费用分摊计算失败", e);
            throw new RuntimeException("批量执行费用分摊计算失败", e);
        }
    }

    @Override
    public Map<String, Object> getRoiAnalysis(LocalDateTime startTime, LocalDateTime endTime, String channelType) {
        log.info("获取ROI分析数据，startTime={}, endTime={}, channelType={}", startTime, endTime, channelType);
        
        try {
            Map<String, Object> analysis = new HashMap<>();
            analysis.put("startTime", startTime);
            analysis.put("endTime", endTime);
            analysis.put("channelType", channelType);
            
            // 模拟ROI分析数据
            analysis.put("totalInvestment", 50000.0);
            analysis.put("totalRevenue", 150000.0);
            analysis.put("roi", 2.0);
            analysis.put("conversionRate", 0.15);
            analysis.put("customerAcquisitionCost", 100.0);
            analysis.put("customerLifetimeValue", 800.0);
            
            log.info("ROI分析数据获取完成");
            return analysis;
        } catch (Exception e) {
            log.error("获取ROI分析数据失败", e);
            throw new RuntimeException("获取ROI分析数据失败", e);
        }
    }

    @Override
    public Map<String, Object> getChannelComparison(LocalDateTime startTime, LocalDateTime endTime) {
        log.info("获取渠道对比数据，startTime={}, endTime={}", startTime, endTime);
        
        try {
            Map<String, Object> comparison = new HashMap<>();
            comparison.put("startTime", startTime);
            comparison.put("endTime", endTime);
            
            // 模拟渠道对比数据
            Map<String, Object> onlineChannel = new HashMap<>();
            onlineChannel.put("investment", 30000.0);
            onlineChannel.put("revenue", 90000.0);
            onlineChannel.put("roi", 2.0);
            onlineChannel.put("conversionRate", 0.12);
            
            Map<String, Object> offlineChannel = new HashMap<>();
            offlineChannel.put("investment", 20000.0);
            offlineChannel.put("revenue", 60000.0);
            offlineChannel.put("roi", 2.0);
            offlineChannel.put("conversionRate", 0.18);
            
            comparison.put("onlineChannel", onlineChannel);
            comparison.put("offlineChannel", offlineChannel);
            
            log.info("渠道对比数据获取完成");
            return comparison;
        } catch (Exception e) {
            log.error("获取渠道对比数据失败", e);
            throw new RuntimeException("获取渠道对比数据失败", e);
        }
    }

    @Override
    public Map<String, Object> getAllocationStatistics() {
        log.info("获取分摊统计信息");
        
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 统计各状态记录数量
            int completedCount = promotionAllocationRepository.countByStatusAndPromotion("COMPLETED", null);
            int pendingCount = promotionAllocationRepository.countByStatusAndPromotion("PENDING", null);
            int failedCount = promotionAllocationRepository.countByStatusAndPromotion("FAILED", null);
            
            statistics.put("completedCount", completedCount);
            statistics.put("pendingCount", pendingCount);
            statistics.put("failedCount", failedCount);
            statistics.put("totalCount", completedCount + pendingCount + failedCount);
            
            log.info("分摊统计信息获取完成");
            return statistics;
        } catch (Exception e) {
            log.error("获取分摊统计信息失败", e);
            throw new RuntimeException("获取分摊统计信息失败", e);
        }
    }
} 