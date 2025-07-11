package com.iobaf.domain.promotion.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.promotion.entity.PromotionAllocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 促销费用分摊数据访问层
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Mapper
public interface PromotionAllocationRepository extends BaseMapper<PromotionAllocation> {

    /**
     * 分页查询分摊记录列表
     * 
     * @param page 分页参数
     * @param promotionId 促销活动ID
     * @param channelType 渠道类型
     * @param allocationStatus 分摊状态
     * @return 分摊记录分页数据
     */
    IPage<PromotionAllocation> selectAllocationPage(Page<PromotionAllocation> page,
                                                   @Param("promotionId") Long promotionId,
                                                   @Param("channelType") String channelType,
                                                   @Param("allocationStatus") String allocationStatus);

    /**
     * 根据促销活动ID查询分摊记录
     * 
     * @param promotionId 促销活动ID
     * @return 分摊记录列表
     */
    List<PromotionAllocation> selectByPromotionId(@Param("promotionId") Long promotionId);

    /**
     * 根据渠道类型查询分摊记录
     * 
     * @param channelType 渠道类型
     * @return 分摊记录列表
     */
    List<PromotionAllocation> selectByChannelType(@Param("channelType") String channelType);

    /**
     * 根据时间范围查询分摊记录
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分摊记录列表
     */
    List<PromotionAllocation> selectByTimeRange(@Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);

    /**
     * 批量插入分摊记录
     * 
     * @param allocations 分摊记录列表
     * @return 插入数量
     */
    int batchInsert(@Param("allocations") List<PromotionAllocation> allocations);

    /**
     * 批量更新分摊记录
     * 
     * @param allocations 分摊记录列表
     * @return 更新数量
     */
    int batchUpdate(@Param("allocations") List<PromotionAllocation> allocations);

    /**
     * 统计分摊记录数量
     * 
     * @param allocationStatus 分摊状态
     * @param promotionId 促销活动ID
     * @return 记录数量
     */
    int countByStatusAndPromotion(@Param("allocationStatus") String allocationStatus, 
                                 @Param("promotionId") Long promotionId);
} 