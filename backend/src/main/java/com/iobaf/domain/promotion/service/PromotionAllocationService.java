package com.iobaf.domain.promotion.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.promotion.entity.PromotionAllocation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 促销费用分摊服务接口
 * 
 * @author iobaf
 * @since 2024-01-01
 */
public interface PromotionAllocationService {

    /**
     * 分页查询分摊记录列表
     * 
     * @param page 分页参数
     * @param promotionId 促销活动ID
     * @param channelType 渠道类型
     * @param allocationStatus 分摊状态
     * @return 分摊记录分页数据
     */
    IPage<PromotionAllocation> getAllocationPage(Page<PromotionAllocation> page,
                                                Long promotionId,
                                                String channelType,
                                                String allocationStatus);

    /**
     * 根据ID查询分摊记录详情
     * 
     * @param allocationId 分摊记录ID
     * @return 分摊记录详情
     */
    PromotionAllocation getAllocationById(Long allocationId);

    /**
     * 创建分摊记录
     * 
     * @param allocation 分摊记录信息
     * @return 创建结果
     */
    boolean createAllocation(PromotionAllocation allocation);

    /**
     * 更新分摊记录
     * 
     * @param allocation 分摊记录信息
     * @return 更新结果
     */
    boolean updateAllocation(PromotionAllocation allocation);

    /**
     * 删除分摊记录
     * 
     * @param allocationId 分摊记录ID
     * @return 删除结果
     */
    boolean deleteAllocation(Long allocationId);

    /**
     * 执行费用分摊计算
     * 
     * @param promotionId 促销活动ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 计算结果
     */
    boolean calculateAllocation(Long promotionId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 批量执行费用分摊计算
     * 
     * @param promotionIds 促销活动ID列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 计算结果
     */
    boolean batchCalculateAllocation(List<Long> promotionIds, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取ROI分析数据
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param channelType 渠道类型
     * @return ROI分析数据
     */
    Map<String, Object> getRoiAnalysis(LocalDateTime startTime, LocalDateTime endTime, String channelType);

    /**
     * 获取渠道对比数据
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 渠道对比数据
     */
    Map<String, Object> getChannelComparison(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取分摊统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getAllocationStatistics();
} 