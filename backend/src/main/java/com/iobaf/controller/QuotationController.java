package com.iobaf.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.common.response.Result;
import com.iobaf.domain.quotation.entity.QuotationRequest;
import com.iobaf.domain.quotation.entity.QuotationResult;
import com.iobaf.domain.quotation.service.QuotationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 定制化订单报价引擎控制器
 * 提供动态报价、成本计算、风险评估等功能
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/quotation")
public class QuotationController {

    @Autowired
    private QuotationService quotationService;

    /**
     * 分页查询报价请求列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param customerName 客户名称
     * @param productCode 产品编码
     * @param quotationStatus 报价状态
     * @return 报价请求分页数据
     */
    @GetMapping("/requests")
    public Result<IPage<QuotationRequest>> getQuotationRequestPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String quotationStatus) {
        
        log.info("查询报价请求列表，参数：current={}, size={}, customerName={}, productCode={}, quotationStatus={}",
                current, size, customerName, productCode, quotationStatus);
        
        try {
            Page<QuotationRequest> page = new Page<>(current, size);
            IPage<QuotationRequest> result = quotationService.getQuotationRequestPage(page, customerName, productCode, quotationStatus);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询报价请求列表失败", e);
            return Result.error("查询报价请求列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询报价请求详情
     * 
     * @param requestId 请求ID
     * @return 报价请求详情
     */
    @GetMapping("/requests/{requestId}")
    public Result<QuotationRequest> getQuotationRequestById(@PathVariable Long requestId) {
        log.info("查询报价请求详情，requestId={}", requestId);
        
        try {
            QuotationRequest request = quotationService.getQuotationRequestById(requestId);
            if (request != null) {
                return Result.success(request);
            } else {
                return Result.error("报价请求不存在");
            }
        } catch (Exception e) {
            log.error("查询报价请求详情失败，requestId={}", requestId, e);
            return Result.error("查询报价请求详情失败：" + e.getMessage());
        }
    }

    /**
     * 创建报价请求
     * 
     * @param request 报价请求信息
     * @return 创建结果
     */
    @PostMapping("/requests")
    public Result<Boolean> createQuotationRequest(@RequestBody QuotationRequest request) {
        log.info("创建报价请求，请求信息：{}", request);
        
        try {
            boolean result = quotationService.createQuotationRequest(request);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("创建报价请求失败");
            }
        } catch (Exception e) {
            log.error("创建报价请求失败", e);
            return Result.error("创建报价请求失败：" + e.getMessage());
        }
    }

    /**
     * 更新报价请求
     * 
     * @param requestId 请求ID
     * @param request 报价请求信息
     * @return 更新结果
     */
    @PutMapping("/requests/{requestId}")
    public Result<Boolean> updateQuotationRequest(@PathVariable Long requestId, @RequestBody QuotationRequest request) {
        log.info("更新报价请求，requestId={}", requestId);
        
        try {
            request.setId(requestId);
            boolean result = quotationService.updateQuotationRequest(request);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("更新报价请求失败");
            }
        } catch (Exception e) {
            log.error("更新报价请求失败，requestId={}", requestId, e);
            return Result.error("更新报价请求失败：" + e.getMessage());
        }
    }

    /**
     * 删除报价请求
     * 
     * @param requestId 请求ID
     * @return 删除结果
     */
    @DeleteMapping("/requests/{requestId}")
    public Result<Boolean> deleteQuotationRequest(@PathVariable Long requestId) {
        log.info("删除报价请求，requestId={}", requestId);
        
        try {
            boolean result = quotationService.deleteQuotationRequest(requestId);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("删除报价请求失败");
            }
        } catch (Exception e) {
            log.error("删除报价请求失败，requestId={}", requestId, e);
            return Result.error("删除报价请求失败：" + e.getMessage());
        }
    }

    /**
     * 执行报价计算
     * 
     * @param requestId 请求ID
     * @return 报价结果
     */
    @PostMapping("/calculate/{requestId}")
    public Result<QuotationResult> calculateQuotation(@PathVariable Long requestId) {
        log.info("执行报价计算，requestId={}", requestId);
        
        try {
            QuotationResult result = quotationService.calculateQuotation(requestId);
            if (result != null) {
                return Result.success(result);
            } else {
                return Result.error("报价计算失败");
            }
        } catch (Exception e) {
            log.error("报价计算失败，requestId={}", requestId, e);
            return Result.error("报价计算失败：" + e.getMessage());
        }
    }

    /**
     * 获取报价结果列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param requestId 请求ID
     * @param quotationStatus 报价状态
     * @return 报价结果分页数据
     */
    @GetMapping("/results")
    public Result<IPage<QuotationResult>> getQuotationResultPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long requestId,
            @RequestParam(required = false) String quotationStatus) {
        
        log.info("查询报价结果列表，参数：current={}, size={}, requestId={}, quotationStatus={}",
                current, size, requestId, quotationStatus);
        
        try {
            Page<QuotationResult> page = new Page<>(current, size);
            IPage<QuotationResult> result = quotationService.getQuotationResultPage(page, requestId, quotationStatus);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询报价结果列表失败", e);
            return Result.error("查询报价结果列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询报价结果详情
     * 
     * @param resultId 结果ID
     * @return 报价结果详情
     */
    @GetMapping("/results/{resultId}")
    public Result<QuotationResult> getQuotationResultById(@PathVariable Long resultId) {
        log.info("查询报价结果详情，resultId={}", resultId);
        
        try {
            QuotationResult result = quotationService.getQuotationResultById(resultId);
            if (result != null) {
                return Result.success(result);
            } else {
                return Result.error("报价结果不存在");
            }
        } catch (Exception e) {
            log.error("查询报价结果详情失败，resultId={}", resultId, e);
            return Result.error("查询报价结果详情失败：" + e.getMessage());
        }
    }

    /**
     * 获取成本分析数据
     * 
     * @param requestId 请求ID
     * @return 成本分析数据
     */
    @GetMapping("/cost-analysis/{requestId}")
    public Result<Map<String, Object>> getCostAnalysis(@PathVariable Long requestId) {
        log.info("获取成本分析数据，requestId={}", requestId);
        
        try {
            Map<String, Object> costData = quotationService.getCostAnalysis(requestId);
            return Result.success(costData);
        } catch (Exception e) {
            log.error("获取成本分析数据失败，requestId={}", requestId, e);
            return Result.error("获取成本分析数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取风险评估数据
     * 
     * @param requestId 请求ID
     * @return 风险评估数据
     */
    @GetMapping("/risk-assessment/{requestId}")
    public Result<Map<String, Object>> getRiskAssessment(@PathVariable Long requestId) {
        log.info("获取风险评估数据，requestId={}", requestId);
        
        try {
            Map<String, Object> riskData = quotationService.getRiskAssessment(requestId);
            return Result.success(riskData);
        } catch (Exception e) {
            log.error("获取风险评估数据失败，requestId={}", requestId, e);
            return Result.error("获取风险评估数据失败：" + e.getMessage());
        }
    }

    /**
     * 快速报价（简化版）
     * 
     * @param productCode 产品编码
     * @param quantity 数量
     * @param materialCost 材料成本
     * @param processComplexity 工艺复杂度
     * @return 快速报价结果
     */
    @PostMapping("/quick-quote")
    public Result<Map<String, Object>> quickQuote(@RequestParam String productCode,
                                                @RequestParam Integer quantity,
                                                @RequestParam BigDecimal materialCost,
                                                @RequestParam String processComplexity) {
        log.info("快速报价，productCode={}, quantity={}, materialCost={}, processComplexity={}", 
                productCode, quantity, materialCost, processComplexity);
        
        try {
            Map<String, Object> quoteResult = quotationService.quickQuote(productCode, quantity, materialCost, processComplexity);
            return Result.success(quoteResult);
        } catch (Exception e) {
            log.error("快速报价失败，productCode={}", productCode, e);
            return Result.error("快速报价失败：" + e.getMessage());
        }
    }

    /**
     * 批量报价
     * 
     * @param requestIds 请求ID列表
     * @return 批量报价结果
     */
    @PostMapping("/batch-quote")
    public Result<Boolean> batchQuote(@RequestParam List<Long> requestIds) {
        log.info("批量报价，requestIds={}", requestIds);
        
        try {
            boolean result = quotationService.batchQuote(requestIds);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("批量报价失败");
            }
        } catch (Exception e) {
            log.error("批量报价失败", e);
            return Result.error("批量报价失败：" + e.getMessage());
        }
    }

    /**
     * 获取报价历史对比
     * 
     * @param productCode 产品编码
     * @param customerId 客户ID
     * @return 报价历史对比数据
     */
    @GetMapping("/history-comparison")
    public Result<Map<String, Object>> getQuotationHistoryComparison(@RequestParam String productCode,
                                                                   @RequestParam Long customerId) {
        log.info("获取报价历史对比，productCode={}, customerId={}", productCode, customerId);
        
        try {
            Map<String, Object> historyData = quotationService.getQuotationHistoryComparison(productCode, customerId);
            return Result.success(historyData);
        } catch (Exception e) {
            log.error("获取报价历史对比失败，productCode={}", productCode, e);
            return Result.error("获取报价历史对比失败：" + e.getMessage());
        }
    }
} 