package com.iobaf.domain.promotion.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.promotion.entity.PromotionRule;
import com.iobaf.domain.promotion.repository.PromotionRuleRepository;
import com.iobaf.domain.promotion.service.PromotionRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 促销费用分摊规则服务实现类
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Slf4j
@Service
public class PromotionRuleServiceImpl implements PromotionRuleService {

    @Autowired
    private PromotionRuleRepository promotionRuleRepository;

    @Override
    public IPage<PromotionRule> getPromotionRulePage(Page<PromotionRule> page,
                                                    String ruleName,
                                                    String ruleType,
                                                    Integer status) {
        log.info("查询促销规则列表，参数：ruleName={}, ruleType={}, status={}", ruleName, ruleType, status);
        
        try {
            return promotionRuleRepository.selectPromotionRulePage(page, ruleName, ruleType, status);
        } catch (Exception e) {
            log.error("查询促销规则列表失败", e);
            throw new RuntimeException("查询促销规则列表失败", e);
        }
    }

    @Override
    public PromotionRule getPromotionRuleById(Long ruleId) {
        log.info("根据ID查询促销规则详情，ruleId={}", ruleId);
        
        try {
            return promotionRuleRepository.selectById(ruleId);
        } catch (Exception e) {
            log.error("查询促销规则详情失败，ruleId={}", ruleId, e);
            throw new RuntimeException("查询促销规则详情失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createPromotionRule(PromotionRule rule) {
        log.info("创建促销规则，规则信息：{}", rule);
        
        try {
            // 设置创建时间
            rule.setCreatedAt(LocalDateTime.now());
            
            // 设置默认状态
            if (rule.getStatus() == null) {
                rule.setStatus(1);
            }
            
            int result = promotionRuleRepository.insert(rule);
            
            if (result > 0) {
                log.info("促销规则创建成功，ruleId={}", rule.getId());
                return true;
            } else {
                log.error("促销规则创建失败");
                return false;
            }
        } catch (Exception e) {
            log.error("创建促销规则失败", e);
            throw new RuntimeException("创建促销规则失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePromotionRule(PromotionRule rule) {
        log.info("更新促销规则信息，ruleId={}", rule.getId());
        
        try {
            // 设置更新时间
            rule.setUpdatedAt(LocalDateTime.now());
            
            int result = promotionRuleRepository.updateById(rule);
            
            if (result > 0) {
                log.info("促销规则更新成功，ruleId={}", rule.getId());
                return true;
            } else {
                log.error("促销规则更新失败，ruleId={}", rule.getId());
                return false;
            }
        } catch (Exception e) {
            log.error("更新促销规则失败，ruleId={}", rule.getId(), e);
            throw new RuntimeException("更新促销规则失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePromotionRule(Long ruleId) {
        log.info("删除促销规则，ruleId={}", ruleId);
        
        try {
            int result = promotionRuleRepository.deleteById(ruleId);
            
            if (result > 0) {
                log.info("促销规则删除成功，ruleId={}", ruleId);
                return true;
            } else {
                log.error("促销规则删除失败，ruleId={}", ruleId);
                return false;
            }
        } catch (Exception e) {
            log.error("删除促销规则失败，ruleId={}", ruleId, e);
            throw new RuntimeException("删除促销规则失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enablePromotionRule(Long ruleId) {
        log.info("启用促销规则，ruleId={}", ruleId);
        
        try {
            PromotionRule rule = new PromotionRule();
            rule.setId(ruleId);
            rule.setStatus(1);
            rule.setUpdatedAt(LocalDateTime.now());
            
            int result = promotionRuleRepository.updateById(rule);
            
            if (result > 0) {
                log.info("促销规则启用成功，ruleId={}", ruleId);
                return true;
            } else {
                log.error("促销规则启用失败，ruleId={}", ruleId);
                return false;
            }
        } catch (Exception e) {
            log.error("启用促销规则失败，ruleId={}", ruleId, e);
            throw new RuntimeException("启用促销规则失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disablePromotionRule(Long ruleId) {
        log.info("禁用促销规则，ruleId={}", ruleId);
        
        try {
            PromotionRule rule = new PromotionRule();
            rule.setId(ruleId);
            rule.setStatus(0);
            rule.setUpdatedAt(LocalDateTime.now());
            
            int result = promotionRuleRepository.updateById(rule);
            
            if (result > 0) {
                log.info("促销规则禁用成功，ruleId={}", ruleId);
                return true;
            } else {
                log.error("促销规则禁用失败，ruleId={}", ruleId);
                return false;
            }
        } catch (Exception e) {
            log.error("禁用促销规则失败，ruleId={}", ruleId, e);
            throw new RuntimeException("禁用促销规则失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchEnablePromotionRules(List<Long> ruleIds) {
        log.info("批量启用促销规则，ruleIds={}", ruleIds);
        
        try {
            int result = promotionRuleRepository.batchUpdateStatus(ruleIds, 1);
            log.info("批量启用促销规则成功，启用数量={}", result);
            return result;
        } catch (Exception e) {
            log.error("批量启用促销规则失败", e);
            throw new RuntimeException("批量启用促销规则失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDisablePromotionRules(List<Long> ruleIds) {
        log.info("批量禁用促销规则，ruleIds={}", ruleIds);
        
        try {
            int result = promotionRuleRepository.batchUpdateStatus(ruleIds, 0);
            log.info("批量禁用促销规则成功，禁用数量={}", result);
            return result;
        } catch (Exception e) {
            log.error("批量禁用促销规则失败", e);
            throw new RuntimeException("批量禁用促销规则失败", e);
        }
    }

    @Override
    public List<PromotionRule> getActiveRulesByType(String ruleType) {
        log.info("根据规则类型查询有效规则，ruleType={}", ruleType);
        
        try {
            return promotionRuleRepository.selectActiveRulesByType(ruleType);
        } catch (Exception e) {
            log.error("查询有效规则失败，ruleType={}", ruleType, e);
            throw new RuntimeException("查询有效规则失败", e);
        }
    }

    @Override
    public Map<String, Object> calculatePromotionAllocation(Long orderId, 
                                                          Double totalAmount, 
                                                          String ruleType) {
        log.info("计算促销费用分摊，orderId={}, totalAmount={}, ruleType={}", orderId, totalAmount, ruleType);
        
        try {
            // 获取有效的分摊规则
            List<PromotionRule> activeRules = getActiveRulesByType(ruleType);
            
            Map<String, Object> result = new HashMap<>();
            result.put("orderId", orderId);
            result.put("totalAmount", totalAmount);
            result.put("ruleType", ruleType);
            result.put("allocationRules", activeRules);
            
            // 计算分摊金额
            double allocatedAmount = 0.0;
            for (PromotionRule rule : activeRules) {
                double ruleAmount = totalAmount * (rule.getAllocationRatio() / 100.0);
                allocatedAmount += ruleAmount;
            }
            
            result.put("allocatedAmount", allocatedAmount);
            result.put("remainingAmount", totalAmount - allocatedAmount);
            
            log.info("促销费用分摊计算完成，分摊金额={}", allocatedAmount);
            return result;
        } catch (Exception e) {
            log.error("计算促销费用分摊失败", e);
            throw new RuntimeException("计算促销费用分摊失败", e);
        }
    }

    @Override
    public Map<String, Object> getPromotionRuleStatistics() {
        log.info("获取促销规则统计信息");
        
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 统计各类型规则数量
            int salesRuleCount = promotionRuleRepository.countByTypeAndStatus("SALES", 1);
            int quantityRuleCount = promotionRuleRepository.countByTypeAndStatus("QUANTITY", 1);
            int marketRuleCount = promotionRuleRepository.countByTypeAndStatus("MARKET", 1);
            
            statistics.put("salesRuleCount", salesRuleCount);
            statistics.put("quantityRuleCount", quantityRuleCount);
            statistics.put("marketRuleCount", marketRuleCount);
            statistics.put("totalActiveRules", salesRuleCount + quantityRuleCount + marketRuleCount);
            
            log.info("促销规则统计信息获取完成");
            return statistics;
        } catch (Exception e) {
            log.error("获取促销规则统计信息失败", e);
            throw new RuntimeException("获取促销规则统计信息失败", e);
        }
    }
} 