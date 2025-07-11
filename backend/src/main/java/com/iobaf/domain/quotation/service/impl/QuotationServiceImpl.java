package com.iobaf.domain.quotation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.quotation.entity.QuotationRequest;
import com.iobaf.domain.quotation.entity.QuotationResult;
import com.iobaf.domain.quotation.repository.QuotationRequestRepository;
import com.iobaf.domain.quotation.repository.QuotationResultRepository;
import com.iobaf.domain.quotation.service.QuotationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定制化报价服务实现类
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Slf4j
@Service
public class QuotationServiceImpl implements QuotationService {

    @Autowired
    private QuotationRequestRepository quotationRequestRepository;

    @Autowired
    private QuotationResultRepository quotationResultRepository;

    @Override
    public IPage<QuotationRequest> getQuotationRequestPage(Page<QuotationRequest> page,
                                                         String customerName,
                                                         String productCode,
                                                         String quotationStatus) {
        log.info("查询报价请求列表，参数：customerName={}, productCode={}, quotationStatus={}", 
                customerName, productCode, quotationStatus);
        
        try {
            return quotationRequestRepository.selectQuotationRequestPage(page, customerName, productCode, quotationStatus);
        } catch (Exception e) {
            log.error("查询报价请求列表失败", e);
            throw new RuntimeException("查询报价请求列表失败", e);
        }
    }

    @Override
    public QuotationRequest getQuotationRequestById(Long requestId) {
        log.info("根据ID查询报价请求详情，requestId={}", requestId);
        
        try {
            return quotationRequestRepository.selectById(requestId);
        } catch (Exception e) {
            log.error("查询报价请求详情失败，requestId={}", requestId, e);
            throw new RuntimeException("查询报价请求详情失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createQuotationRequest(QuotationRequest request) {
        log.info("创建报价请求，请求信息：{}", request);
        
        try {
            // 设置创建时间
            request.setCreatedAt(LocalDateTime.now());
            
            // 设置默认状态
            if (request.getQuotationStatus() == null) {
                request.setQuotationStatus("PENDING");
            }
            
            int result = quotationRequestRepository.insert(request);
            
            if (result > 0) {
                log.info("报价请求创建成功，requestId={}", request.getId());
                return true;
            } else {
                log.error("报价请求创建失败");
                return false;
            }
        } catch (Exception e) {
            log.error("创建报价请求失败", e);
            throw new RuntimeException("创建报价请求失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQuotationRequest(QuotationRequest request) {
        log.info("更新报价请求信息，requestId={}", request.getId());
        
        try {
            // 设置更新时间
            request.setUpdatedAt(LocalDateTime.now());
            
            int result = quotationRequestRepository.updateById(request);
            
            if (result > 0) {
                log.info("报价请求更新成功，requestId={}", request.getId());
                return true;
            } else {
                log.error("报价请求更新失败，requestId={}", request.getId());
                return false;
            }
        } catch (Exception e) {
            log.error("更新报价请求失败，requestId={}", request.getId(), e);
            throw new RuntimeException("更新报价请求失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteQuotationRequest(Long requestId) {
        log.info("删除报价请求，requestId={}", requestId);
        
        try {
            int result = quotationRequestRepository.deleteById(requestId);
            
            if (result > 0) {
                log.info("报价请求删除成功，requestId={}", requestId);
                return true;
            } else {
                log.error("报价请求删除失败，requestId={}", requestId);
                return false;
            }
        } catch (Exception e) {
            log.error("删除报价请求失败，requestId={}", requestId, e);
            throw new RuntimeException("删除报价请求失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QuotationResult calculateQuotation(Long requestId) {
        log.info("计算报价，requestId={}", requestId);
        
        try {
            // 获取报价请求
            QuotationRequest request = getQuotationRequestById(requestId);
            if (request == null) {
                throw new RuntimeException("报价请求不存在");
            }
            
            // 计算报价
            QuotationResult result = new QuotationResult();
            result.setRequestId(requestId);
            result.setProductCode(request.getProductCode());
            result.setCustomerId(request.getCustomerId());
            result.setQuantity(request.getQuantity());
            
            // 计算材料成本
            double materialCost = request.getMaterialCost() * request.getQuantity();
            
            // 计算工艺成本
            double processCost = calculateProcessCost(request.getProcessComplexity(), request.getQuantity());
            
            // 计算管理成本
            double managementCost = (materialCost + processCost) * 0.1;
            
            // 计算利润
            double profit = (materialCost + processCost + managementCost) * 0.2;
            
            // 计算总价
            double totalPrice = materialCost + processCost + managementCost + profit;
            
            result.setMaterialCost(materialCost);
            result.setProcessCost(processCost);
            result.setManagementCost(managementCost);
            result.setProfit(profit);
            result.setTotalPrice(totalPrice);
            result.setQuotationStatus("COMPLETED");
            result.setCreatedAt(LocalDateTime.now());
            
            // 保存报价结果
            quotationResultRepository.insert(result);
            
            // 更新请求状态
            request.setQuotationStatus("COMPLETED");
            request.setUpdatedAt(LocalDateTime.now());
            quotationRequestRepository.updateById(request);
            
            log.info("报价计算完成，总价={}", totalPrice);
            return result;
        } catch (Exception e) {
            log.error("计算报价失败，requestId={}", requestId, e);
            throw new RuntimeException("计算报价失败", e);
        }
    }

    @Override
    public IPage<QuotationResult> getQuotationResultPage(Page<QuotationResult> page,
                                                        Long requestId,
                                                        String quotationStatus) {
        log.info("查询报价结果列表，参数：requestId={}, quotationStatus={}", requestId, quotationStatus);
        
        try {
            return quotationResultRepository.selectQuotationResultPage(page, requestId, quotationStatus);
        } catch (Exception e) {
            log.error("查询报价结果列表失败", e);
            throw new RuntimeException("查询报价结果列表失败", e);
        }
    }

    @Override
    public QuotationResult getQuotationResultById(Long resultId) {
        log.info("根据ID查询报价结果详情，resultId={}", resultId);
        
        try {
            return quotationResultRepository.selectById(resultId);
        } catch (Exception e) {
            log.error("查询报价结果详情失败，resultId={}", resultId, e);
            throw new RuntimeException("查询报价结果详情失败", e);
        }
    }

    @Override
    public Map<String, Object> getCostAnalysis(Long requestId) {
        log.info("获取成本分析数据，requestId={}", requestId);
        
        try {
            QuotationRequest request = getQuotationRequestById(requestId);
            if (request == null) {
                throw new RuntimeException("报价请求不存在");
            }
            
            Map<String, Object> analysis = new HashMap<>();
            analysis.put("requestId", requestId);
            analysis.put("productCode", request.getProductCode());
            analysis.put("quantity", request.getQuantity());
            
            // 成本分析
            double materialCost = request.getMaterialCost() * request.getQuantity();
            double processCost = calculateProcessCost(request.getProcessComplexity(), request.getQuantity());
            double managementCost = (materialCost + processCost) * 0.1;
            
            analysis.put("materialCost", materialCost);
            analysis.put("processCost", processCost);
            analysis.put("managementCost", managementCost);
            analysis.put("totalCost", materialCost + processCost + managementCost);
            
            // 成本占比
            double totalCost = materialCost + processCost + managementCost;
            analysis.put("materialCostRatio", materialCost / totalCost);
            analysis.put("processCostRatio", processCost / totalCost);
            analysis.put("managementCostRatio", managementCost / totalCost);
            
            log.info("成本分析数据获取完成");
            return analysis;
        } catch (Exception e) {
            log.error("获取成本分析数据失败", e);
            throw new RuntimeException("获取成本分析数据失败", e);
        }
    }

    @Override
    public Map<String, Object> getRiskAssessment(Long requestId) {
        log.info("获取风险评估数据，requestId={}", requestId);
        
        try {
            QuotationRequest request = getQuotationRequestById(requestId);
            if (request == null) {
                throw new RuntimeException("报价请求不存在");
            }
            
            Map<String, Object> assessment = new HashMap<>();
            assessment.put("requestId", requestId);
            
            // 风险评估
            double riskScore = 0.0;
            String riskLevel = "LOW";
            
            // 工艺复杂度风险
            if ("HIGH".equals(request.getProcessComplexity())) {
                riskScore += 0.4;
                riskLevel = "HIGH";
            } else if ("MEDIUM".equals(request.getProcessComplexity())) {
                riskScore += 0.2;
                riskLevel = "MEDIUM";
            }
            
            // 数量风险
            if (request.getQuantity() < 100) {
                riskScore += 0.3;
                riskLevel = "HIGH";
            } else if (request.getQuantity() < 1000) {
                riskScore += 0.1;
                riskLevel = "MEDIUM";
            }
            
            // 材料成本风险
            if (request.getMaterialCost() > 1000) {
                riskScore += 0.3;
                riskLevel = "HIGH";
            }
            
            assessment.put("riskScore", riskScore);
            assessment.put("riskLevel", riskLevel);
            assessment.put("riskFactors", List.of("工艺复杂度", "数量", "材料成本"));
            
            log.info("风险评估数据获取完成，风险等级={}", riskLevel);
            return assessment;
        } catch (Exception e) {
            log.error("获取风险评估数据失败", e);
            throw new RuntimeException("获取风险评估数据失败", e);
        }
    }

    @Override
    public Map<String, Object> quickQuote(String productCode, 
                                         Integer quantity, 
                                         Double materialCost, 
                                         String processComplexity) {
        log.info("快速报价，productCode={}, quantity={}, materialCost={}, processComplexity={}", 
                productCode, quantity, materialCost, processComplexity);
        
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("productCode", productCode);
            result.put("quantity", quantity);
            result.put("materialCost", materialCost);
            result.put("processComplexity", processComplexity);
            
            // 快速计算
            double totalMaterialCost = materialCost * quantity;
            double processCost = calculateProcessCost(processComplexity, quantity);
            double managementCost = (totalMaterialCost + processCost) * 0.1;
            double profit = (totalMaterialCost + processCost + managementCost) * 0.2;
            double totalPrice = totalMaterialCost + processCost + managementCost + profit;
            
            result.put("totalMaterialCost", totalMaterialCost);
            result.put("processCost", processCost);
            result.put("managementCost", managementCost);
            result.put("profit", profit);
            result.put("totalPrice", totalPrice);
            result.put("unitPrice", totalPrice / quantity);
            
            log.info("快速报价完成，总价={}", totalPrice);
            return result;
        } catch (Exception e) {
            log.error("快速报价失败", e);
            throw new RuntimeException("快速报价失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchQuote(List<Long> requestIds) {
        log.info("批量报价，请求ID数量={}", requestIds.size());
        
        try {
            int successCount = 0;
            for (Long requestId : requestIds) {
                try {
                    calculateQuotation(requestId);
                    successCount++;
                } catch (Exception e) {
                    log.error("批量报价失败，requestId={}", requestId, e);
                }
            }
            
            log.info("批量报价完成，成功数量={}", successCount);
            return successCount > 0;
        } catch (Exception e) {
            log.error("批量报价失败", e);
            throw new RuntimeException("批量报价失败", e);
        }
    }

    @Override
    public Map<String, Object> getQuotationHistoryComparison(String productCode, Long customerId) {
        log.info("获取报价历史对比，productCode={}, customerId={}", productCode, customerId);
        
        try {
            Map<String, Object> comparison = new HashMap<>();
            comparison.put("productCode", productCode);
            comparison.put("customerId", customerId);
            
            // 获取历史报价数据
            List<QuotationResult> historyResults = quotationResultRepository.selectByProductCode(productCode);
            
            if (!historyResults.isEmpty()) {
                // 计算统计信息
                double avgPrice = historyResults.stream()
                        .mapToDouble(QuotationResult::getTotalPrice)
                        .average()
                        .orElse(0.0);
                
                double minPrice = historyResults.stream()
                        .mapToDouble(QuotationResult::getTotalPrice)
                        .min()
                        .orElse(0.0);
                
                double maxPrice = historyResults.stream()
                        .mapToDouble(QuotationResult::getTotalPrice)
                        .max()
                        .orElse(0.0);
                
                comparison.put("avgPrice", avgPrice);
                comparison.put("minPrice", minPrice);
                comparison.put("maxPrice", maxPrice);
                comparison.put("historyCount", historyResults.size());
                comparison.put("historyData", historyResults);
            }
            
            log.info("报价历史对比数据获取完成");
            return comparison;
        } catch (Exception e) {
            log.error("获取报价历史对比失败", e);
            throw new RuntimeException("获取报价历史对比失败", e);
        }
    }

    /**
     * 计算工艺成本
     * 
     * @param processComplexity 工艺复杂度
     * @param quantity 数量
     * @return 工艺成本
     */
    private double calculateProcessCost(String processComplexity, Integer quantity) {
        double baseCost = 100.0; // 基础工艺成本
        
        switch (processComplexity) {
            case "HIGH":
                return baseCost * 3.0 * quantity;
            case "MEDIUM":
                return baseCost * 2.0 * quantity;
            case "LOW":
            default:
                return baseCost * 1.0 * quantity;
        }
    }
} 