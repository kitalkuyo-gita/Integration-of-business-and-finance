package com.iobaf.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.common.response.Result;
import com.iobaf.domain.promotion.entity.PromotionRule;
import com.iobaf.domain.promotion.entity.PromotionAllocation;
import com.iobaf.domain.promotion.service.PromotionRuleService;
import com.iobaf.domain.promotion.service.PromotionAllocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 促销费用分摊引擎控制器
 * 提供促销规则管理、费用分摊计算、ROI分析等功能
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/promotion")
public class PromotionController {

    @Autowired
    private PromotionRuleService promotionRuleService;

    @Autowired
    private PromotionAllocationService promotionAllocationService;

    /**
     * 分页查询促销规则列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param ruleType 规则类型
     * @param channelType 渠道类型
     * @param ruleStatus 规则状态
     * @return 促销规则分页数据
     */
    @GetMapping("/rules")
    public Result<IPage<PromotionRule>> getPromotionRulePage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String ruleType,
            @RequestParam(required = false) String channelType,
            @RequestParam(required = false) String ruleStatus) {
        
        log.info("查询促销规则列表，参数：current={}, size={}, ruleType={}, channelType={}, ruleStatus={}",
                current, size, ruleType, channelType, ruleStatus);
        
        try {
            Page<PromotionRule> page = new Page<>(current, size);
            IPage<PromotionRule> result = promotionRuleService.getPromotionRulePage(page, ruleType, channelType, ruleStatus);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询促销规则列表失败", e);
            return Result.error("查询促销规则列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询促销规则详情
     * 
     * @param ruleId 规则ID
     * @return 促销规则详情
     */
    @GetMapping("/rules/{ruleId}")
    public Result<PromotionRule> getPromotionRuleById(@PathVariable Long ruleId) {
        log.info("查询促销规则详情，ruleId={}", ruleId);
        
        try {
            PromotionRule rule = promotionRuleService.getPromotionRuleById(ruleId);
            if (rule != null) {
                return Result.success(rule);
            } else {
                return Result.error("促销规则不存在");
            }
        } catch (Exception e) {
            log.error("查询促销规则详情失败，ruleId={}", ruleId, e);
            return Result.error("查询促销规则详情失败：" + e.getMessage());
        }
    }

    /**
     * 创建促销规则
     * 
     * @param rule 促销规则信息
     * @return 创建结果
     */
    @PostMapping("/rules")
    public Result<Boolean> createPromotionRule(@RequestBody PromotionRule rule) {
        log.info("创建促销规则，规则信息：{}", rule);
        
        try {
            boolean result = promotionRuleService.createPromotionRule(rule);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("创建促销规则失败");
            }
        } catch (Exception e) {
            log.error("创建促销规则失败", e);
            return Result.error("创建促销规则失败：" + e.getMessage());
        }
    }

    /**
     * 更新促销规则
     * 
     * @param ruleId 规则ID
     * @param rule 促销规则信息
     * @return 更新结果
     */
    @PutMapping("/rules/{ruleId}")
    public Result<Boolean> updatePromotionRule(@PathVariable Long ruleId, @RequestBody PromotionRule rule) {
        log.info("更新促销规则，ruleId={}", ruleId);
        
        try {
            rule.setId(ruleId);
            boolean result = promotionRuleService.updatePromotionRule(rule);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("更新促销规则失败");
            }
        } catch (Exception e) {
            log.error("更新促销规则失败，ruleId={}", ruleId, e);
            return Result.error("更新促销规则失败：" + e.getMessage());
        }
    }

    /**
     * 删除促销规则
     * 
     * @param ruleId 规则ID
     * @return 删除结果
     */
    @DeleteMapping("/rules/{ruleId}")
    public Result<Boolean> deletePromotionRule(@PathVariable Long ruleId) {
        log.info("删除促销规则，ruleId={}", ruleId);
        
        try {
            boolean result = promotionRuleService.deletePromotionRule(ruleId);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("删除促销规则失败");
            }
        } catch (Exception e) {
            log.error("删除促销规则失败，ruleId={}", ruleId, e);
            return Result.error("删除促销规则失败：" + e.getMessage());
        }
    }

    /**
     * 执行费用分摊计算
     * 
     * @param promotionId 促销活动ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 分摊结果
     */
    @PostMapping("/allocation/calculate")
    public Result<Boolean> calculateAllocation(@RequestParam Long promotionId,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        log.info("执行费用分摊计算，promotionId={}, startTime={}, endTime={}", promotionId, startTime, endTime);
        
        try {
            boolean result = promotionAllocationService.calculateAllocation(promotionId, startTime, endTime);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("费用分摊计算失败");
            }
        } catch (Exception e) {
            log.error("费用分摊计算失败，promotionId={}", promotionId, e);
            return Result.error("费用分摊计算失败：" + e.getMessage());
        }
    }

    /**
     * 查询分摊记录列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param promotionId 促销活动ID
     * @param channelType 渠道类型
     * @param allocationStatus 分摊状态
     * @return 分摊记录分页数据
     */
    @GetMapping("/allocation")
    public Result<IPage<PromotionAllocation>> getAllocationPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long promotionId,
            @RequestParam(required = false) String channelType,
            @RequestParam(required = false) String allocationStatus) {
        
        log.info("查询分摊记录列表，参数：current={}, size={}, promotionId={}, channelType={}, allocationStatus={}",
                current, size, promotionId, channelType, allocationStatus);
        
        try {
            Page<PromotionAllocation> page = new Page<>(current, size);
            IPage<PromotionAllocation> result = promotionAllocationService.getAllocationPage(page, promotionId, channelType, allocationStatus);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询分摊记录列表失败", e);
            return Result.error("查询分摊记录列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取促销ROI分析数据
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param channelType 渠道类型
     * @return ROI分析数据
     */
    @GetMapping("/roi-analysis")
    public Result<Map<String, Object>> getRoiAnalysis(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) String channelType) {
        
        log.info("获取促销ROI分析数据，startTime={}, endTime={}, channelType={}", startTime, endTime, channelType);
        
        try {
            Map<String, Object> roiData = promotionAllocationService.getRoiAnalysis(startTime, endTime, channelType);
            return Result.success(roiData);
        } catch (Exception e) {
            log.error("获取促销ROI分析数据失败", e);
            return Result.error("获取促销ROI分析数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取渠道对比分析数据
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 渠道对比分析数据
     */
    @GetMapping("/channel-comparison")
    public Result<Map<String, Object>> getChannelComparison(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        
        log.info("获取渠道对比分析数据，startTime={}, endTime={}", startTime, endTime);
        
        try {
            Map<String, Object> comparisonData = promotionAllocationService.getChannelComparison(startTime, endTime);
            return Result.success(comparisonData);
        } catch (Exception e) {
            log.error("获取渠道对比分析数据失败", e);
            return Result.error("获取渠道对比分析数据失败：" + e.getMessage());
        }
    }

    /**
     * 批量执行费用分摊
     * 
     * @param promotionIds 促销活动ID列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 批量分摊结果
     */
    @PostMapping("/allocation/batch")
    public Result<Boolean> batchCalculateAllocation(@RequestParam List<Long> promotionIds,
                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        log.info("批量执行费用分摊，promotionIds={}, startTime={}, endTime={}", promotionIds, startTime, endTime);
        
        try {
            boolean result = promotionAllocationService.batchCalculateAllocation(promotionIds, startTime, endTime);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("批量费用分摊失败");
            }
        } catch (Exception e) {
            log.error("批量费用分摊失败", e);
            return Result.error("批量费用分摊失败：" + e.getMessage());
        }
    }
} 