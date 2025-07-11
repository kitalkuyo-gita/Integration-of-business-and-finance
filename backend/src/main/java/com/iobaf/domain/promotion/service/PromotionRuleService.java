package com.iobaf.domain.promotion.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.promotion.entity.PromotionRule;

import java.util.List;
import java.util.Map;

/**
 * 促销费用分摊规则服务接口
 * 
 * @author iobaf
 * @since 2024-01-01
 */
public interface PromotionRuleService {

    /**
     * 分页查询促销规则列表
     * 
     * @param page 分页参数
     * @param ruleName 规则名称
     * @param ruleType 规则类型
     * @param status 状态
     * @return 促销规则分页数据
     */
    IPage<PromotionRule> getPromotionRulePage(Page<PromotionRule> page,
                                             String ruleName,
                                             String ruleType,
                                             Integer status);

    /**
     * 根据ID查询促销规则详情
     * 
     * @param ruleId 规则ID
     * @return 促销规则详情
     */
    PromotionRule getPromotionRuleById(Long ruleId);

    /**
     * 创建促销规则
     * 
     * @param rule 促销规则信息
     * @return 创建结果
     */
    boolean createPromotionRule(PromotionRule rule);

    /**
     * 更新促销规则
     * 
     * @param rule 促销规则信息
     * @return 更新结果
     */
    boolean updatePromotionRule(PromotionRule rule);

    /**
     * 删除促销规则
     * 
     * @param ruleId 规则ID
     * @return 删除结果
     */
    boolean deletePromotionRule(Long ruleId);

    /**
     * 启用促销规则
     * 
     * @param ruleId 规则ID
     * @return 启用结果
     */
    boolean enablePromotionRule(Long ruleId);

    /**
     * 禁用促销规则
     * 
     * @param ruleId 规则ID
     * @return 禁用结果
     */
    boolean disablePromotionRule(Long ruleId);

    /**
     * 批量启用促销规则
     * 
     * @param ruleIds 规则ID列表
     * @return 启用数量
     */
    int batchEnablePromotionRules(List<Long> ruleIds);

    /**
     * 批量禁用促销规则
     * 
     * @param ruleIds 规则ID列表
     * @return 禁用数量
     */
    int batchDisablePromotionRules(List<Long> ruleIds);

    /**
     * 根据规则类型查询有效规则
     * 
     * @param ruleType 规则类型
     * @return 有效规则列表
     */
    List<PromotionRule> getActiveRulesByType(String ruleType);

    /**
     * 计算促销费用分摊
     * 
     * @param orderId 订单ID
     * @param totalAmount 总金额
     * @param ruleType 规则类型
     * @return 分摊结果
     */
    Map<String, Object> calculatePromotionAllocation(Long orderId, 
                                                   Double totalAmount, 
                                                   String ruleType);

    /**
     * 获取促销规则统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getPromotionRuleStatistics();
} 