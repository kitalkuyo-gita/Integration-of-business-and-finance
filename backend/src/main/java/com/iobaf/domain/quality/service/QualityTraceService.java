package com.iobaf.domain.quality.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.quality.entity.QualityTrace;

import java.util.List;
import java.util.Map;

/**
 * 质量追溯服务接口
 * 
 * @author iobaf
 * @since 2024-01-01
 */
public interface QualityTraceService {

    /**
     * 分页查询质量追溯记录列表
     * 
     * @param page 分页参数
     * @param productCode 产品编码
     * @param batchNo 批次号
     * @param traceStatus 追溯状态
     * @return 质量追溯记录分页数据
     */
    IPage<QualityTrace> getQualityTracePage(Page<QualityTrace> page,
                                           String productCode,
                                           String batchNo,
                                           String traceStatus);

    /**
     * 根据ID查询质量追溯记录详情
     * 
     * @param traceId 追溯记录ID
     * @return 质量追溯记录详情
     */
    QualityTrace getQualityTraceById(Long traceId);

    /**
     * 创建质量追溯记录
     * 
     * @param trace 质量追溯记录信息
     * @return 创建结果
     */
    boolean createQualityTrace(QualityTrace trace);

    /**
     * 更新质量追溯记录
     * 
     * @param trace 质量追溯记录信息
     * @return 更新结果
     */
    boolean updateQualityTrace(QualityTrace trace);

    /**
     * 删除质量追溯记录
     * 
     * @param traceId 追溯记录ID
     * @return 删除结果
     */
    boolean deleteQualityTrace(Long traceId);

    /**
     * 根据产品编码查询追溯记录
     * 
     * @param productCode 产品编码
     * @return 追溯记录列表
     */
    List<QualityTrace> getQualityTracesByProductCode(String productCode);

    /**
     * 根据批次号查询追溯记录
     * 
     * @param batchNo 批次号
     * @return 追溯记录列表
     */
    List<QualityTrace> getQualityTracesByBatchNo(String batchNo);

    /**
     * 根据二维码查询追溯记录
     * 
     * @param qrCode 二维码
     * @return 追溯记录
     */
    QualityTrace getQualityTraceByQrCode(String qrCode);

    /**
     * 批量创建质量追溯记录
     * 
     * @param traces 质量追溯记录列表
     * @return 创建数量
     */
    int batchCreateQualityTraces(List<QualityTrace> traces);

    /**
     * 批量更新质量追溯记录
     * 
     * @param traces 质量追溯记录列表
     * @return 更新数量
     */
    int batchUpdateQualityTraces(List<QualityTrace> traces);

    /**
     * 批量删除质量追溯记录
     * 
     * @param traceIds 追溯记录ID列表
     * @return 删除数量
     */
    int batchDeleteQualityTraces(List<Long> traceIds);

    /**
     * 生成产品二维码
     * 
     * @param productCode 产品编码
     * @param batchNo 批次号
     * @return 二维码内容
     */
    String generateQrCode(String productCode, String batchNo);

    /**
     * 获取产品全生命周期追溯信息
     * 
     * @param productCode 产品编码
     * @param batchNo 批次号
     * @return 追溯信息
     */
    Map<String, Object> getProductLifecycleTrace(String productCode, String batchNo);

    /**
     * 获取质量追溯统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getQualityTraceStatistics();
} 