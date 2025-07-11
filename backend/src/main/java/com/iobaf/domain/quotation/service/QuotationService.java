package com.iobaf.domain.quotation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.quotation.entity.QuotationRequest;
import com.iobaf.domain.quotation.entity.QuotationResult;

import java.util.List;
import java.util.Map;

/**
 * 定制化报价服务接口
 * 
 * @author iobaf
 * @since 2024-01-01
 */
public interface QuotationService {

    /**
     * 分页查询报价请求列表
     * 
     * @param page 分页参数
     * @param customerName 客户名称
     * @param productCode 产品编码
     * @param quotationStatus 报价状态
     * @return 报价请求分页数据
     */
    IPage<QuotationRequest> getQuotationRequestPage(Page<QuotationRequest> page,
                                                   String customerName,
                                                   String productCode,
                                                   String quotationStatus);

    /**
     * 根据ID查询报价请求详情
     * 
     * @param requestId 请求ID
     * @return 报价请求详情
     */
    QuotationRequest getQuotationRequestById(Long requestId);

    /**
     * 创建报价请求
     * 
     * @param request 报价请求信息
     * @return 创建结果
     */
    boolean createQuotationRequest(QuotationRequest request);

    /**
     * 更新报价请求
     * 
     * @param request 报价请求信息
     * @return 更新结果
     */
    boolean updateQuotationRequest(QuotationRequest request);

    /**
     * 删除报价请求
     * 
     * @param requestId 请求ID
     * @return 删除结果
     */
    boolean deleteQuotationRequest(Long requestId);

    /**
     * 计算报价
     * 
     * @param requestId 请求ID
     * @return 报价结果
     */
    QuotationResult calculateQuotation(Long requestId);

    /**
     * 分页查询报价结果列表
     * 
     * @param page 分页参数
     * @param requestId 请求ID
     * @param quotationStatus 报价状态
     * @return 报价结果分页数据
     */
    IPage<QuotationResult> getQuotationResultPage(Page<QuotationResult> page,
                                                 Long requestId,
                                                 String quotationStatus);

    /**
     * 根据ID查询报价结果详情
     * 
     * @param resultId 结果ID
     * @return 报价结果详情
     */
    QuotationResult getQuotationResultById(Long resultId);

    /**
     * 获取成本分析数据
     * 
     * @param requestId 请求ID
     * @return 成本分析数据
     */
    Map<String, Object> getCostAnalysis(Long requestId);

    /**
     * 获取风险评估数据
     * 
     * @param requestId 请求ID
     * @return 风险评估数据
     */
    Map<String, Object> getRiskAssessment(Long requestId);

    /**
     * 快速报价
     * 
     * @param productCode 产品编码
     * @param quantity 数量
     * @param materialCost 材料成本
     * @param processComplexity 工艺复杂度
     * @return 快速报价结果
     */
    Map<String, Object> quickQuote(String productCode, 
                                  Integer quantity, 
                                  Double materialCost, 
                                  String processComplexity);

    /**
     * 批量报价
     * 
     * @param requestIds 请求ID列表
     * @return 批量报价结果
     */
    boolean batchQuote(List<Long> requestIds);

    /**
     * 获取报价历史对比
     * 
     * @param productCode 产品编码
     * @param customerId 客户ID
     * @return 历史对比数据
     */
    Map<String, Object> getQuotationHistoryComparison(String productCode, Long customerId);
} 