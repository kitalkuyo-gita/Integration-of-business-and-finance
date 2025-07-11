package com.iobaf.domain.quotation.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.quotation.entity.QuotationRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报价请求数据访问层
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Mapper
public interface QuotationRequestRepository extends BaseMapper<QuotationRequest> {

    /**
     * 分页查询报价请求列表
     * 
     * @param page 分页参数
     * @param customerName 客户名称
     * @param productCode 产品编码
     * @param quotationStatus 报价状态
     * @return 报价请求分页数据
     */
    IPage<QuotationRequest> selectQuotationRequestPage(Page<QuotationRequest> page,
                                                      @Param("customerName") String customerName,
                                                      @Param("productCode") String productCode,
                                                      @Param("quotationStatus") String quotationStatus);

    /**
     * 根据客户ID查询报价请求
     * 
     * @param customerId 客户ID
     * @return 报价请求列表
     */
    List<QuotationRequest> selectByCustomerId(@Param("customerId") Long customerId);

    /**
     * 根据产品编码查询报价请求
     * 
     * @param productCode 产品编码
     * @return 报价请求列表
     */
    List<QuotationRequest> selectByProductCode(@Param("productCode") String productCode);

    /**
     * 根据报价状态查询报价请求
     * 
     * @param quotationStatus 报价状态
     * @return 报价请求列表
     */
    List<QuotationRequest> selectByQuotationStatus(@Param("quotationStatus") String quotationStatus);

    /**
     * 批量插入报价请求
     * 
     * @param requests 报价请求列表
     * @return 插入数量
     */
    int batchInsert(@Param("requests") List<QuotationRequest> requests);

    /**
     * 批量更新报价请求
     * 
     * @param requests 报价请求列表
     * @return 更新数量
     */
    int batchUpdate(@Param("requests") List<QuotationRequest> requests);

    /**
     * 统计报价请求数量
     * 
     * @param quotationStatus 报价状态
     * @param customerId 客户ID
     * @return 请求数量
     */
    int countByStatusAndCustomer(@Param("quotationStatus") String quotationStatus, 
                                @Param("customerId") Long customerId);
} 