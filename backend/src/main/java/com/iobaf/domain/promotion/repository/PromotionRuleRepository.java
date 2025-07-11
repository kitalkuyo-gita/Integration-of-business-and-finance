package com.iobaf.domain.promotion.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.promotion.entity.PromotionRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 促销费用分摊规则数据访问层
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Mapper
public interface PromotionRuleRepository extends BaseMapper<PromotionRule> {

    /**
     * 分页查询促销规则列表
     * 
     * @param page 分页参数
     * @param ruleName 规则名称
     * @param ruleType 规则类型
     * @param status 状态
     * @return 促销规则分页数据
     */
    IPage<PromotionRule> selectPromotionRulePage(Page<PromotionRule> page,
                                                @Param("ruleName") String ruleName,
                                                @Param("ruleType") String ruleType,
                                                @Param("status") Integer status);

    /**
     * 根据规则类型查询有效规则
     * 
     * @param ruleType 规则类型
     * @return 有效规则列表
     */
    List<PromotionRule> selectActiveRulesByType(@Param("ruleType") String ruleType);

    /**
     * 根据规则名称查询规则
     * 
     * @param ruleName 规则名称
     * @return 规则列表
     */
    List<PromotionRule> selectByRuleName(@Param("ruleName") String ruleName);

    /**
     * 批量更新规则状态
     * 
     * @param ruleIds 规则ID列表
     * @param status 状态
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("ruleIds") List<Long> ruleIds, @Param("status") Integer status);

    /**
     * 统计规则数量
     * 
     * @param ruleType 规则类型
     * @param status 状态
     * @return 规则数量
     */
    int countByTypeAndStatus(@Param("ruleType") String ruleType, @Param("status") Integer status);
} 