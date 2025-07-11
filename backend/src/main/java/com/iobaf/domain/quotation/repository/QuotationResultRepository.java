package com.iobaf.domain.quotation.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.quotation.entity.QuotationResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报价结果数据访问层
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Mapper
public interface QuotationResultRepository extends BaseMapper<QuotationResult> {

    /**
     * 分页查询报价结果列表
     * 
     * @param page 分页参数
     * @param requestId 请求ID
     * @param quotationStatus 报价状态
     * @return 报价结果分页数据
     */
    IPage<QuotationResult> selectQuotationResultPage(Page<QuotationResult> page,
                                                    @Param("requestId") Long requestId,
                                                    @Param("quotationStatus") String quotationStatus);

    /**
     * 根据请求ID查询报价结果
     * 
     * @param requestId 请求ID
     * @return 报价结果列表
     */
    List<QuotationResult> selectByRequestId(@Param("requestId") Long requestId);

    /**
     * 根据产品编码查询报价结果
     * 
     * @param productCode 产品编码
     * @return 报价结果列表
     */
    List<QuotationResult> selectByProductCode(@Param("productCode") String productCode);

    /**
     * 根据客户ID查询报价结果
     * 
     * @param customerId 客户ID
     * @return 报价结果列表
     */
    List<QuotationResult> selectByCustomerId(@Param("customerId") Long customerId);

    /**
     * 批量插入报价结果
     * 
     * @param results 报价结果列表
     * @return 插入数量
     */
    int batchInsert(@Param("results") List<QuotationResult> results);

    /**
     * 批量更新报价结果
     * 
     * @param results 报价结果列表
     * @return 更新数量
     */
    int batchUpdate(@Param("results") List<QuotationResult> results);

    /**
     * 统计报价结果数量
     * 
     * @param quotationStatus 报价状态
     * @param requestId 请求ID
     * @return 结果数量
     */
    int countByStatusAndRequest(@Param("quotationStatus") String quotationStatus, 
                               @Param("requestId") Long requestId);
} 