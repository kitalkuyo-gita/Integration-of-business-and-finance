package com.iobaf.domain.quality.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.quality.entity.QualityTrace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 质量追溯数据访问层
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Mapper
public interface QualityTraceRepository extends BaseMapper<QualityTrace> {

    /**
     * 分页查询质量追溯记录列表
     * 
     * @param page 分页参数
     * @param productCode 产品编码
     * @param batchNo 批次号
     * @param traceStatus 追溯状态
     * @return 质量追溯记录分页数据
     */
    IPage<QualityTrace> selectQualityTracePage(Page<QualityTrace> page,
                                              @Param("productCode") String productCode,
                                              @Param("batchNo") String batchNo,
                                              @Param("traceStatus") String traceStatus);

    /**
     * 根据产品编码查询追溯记录
     * 
     * @param productCode 产品编码
     * @return 追溯记录列表
     */
    List<QualityTrace> selectByProductCode(@Param("productCode") String productCode);

    /**
     * 根据批次号查询追溯记录
     * 
     * @param batchNo 批次号
     * @return 追溯记录列表
     */
    List<QualityTrace> selectByBatchNo(@Param("batchNo") String batchNo);

    /**
     * 根据二维码查询追溯记录
     * 
     * @param qrCode 二维码
     * @return 追溯记录
     */
    QualityTrace selectByQrCode(@Param("qrCode") String qrCode);

    /**
     * 批量插入质量追溯记录
     * 
     * @param traces 质量追溯记录列表
     * @return 插入数量
     */
    int batchInsert(@Param("traces") List<QualityTrace> traces);

    /**
     * 批量更新质量追溯记录
     * 
     * @param traces 质量追溯记录列表
     * @return 更新数量
     */
    int batchUpdate(@Param("traces") List<QualityTrace> traces);

    /**
     * 统计质量追溯记录数量
     * 
     * @param traceStatus 追溯状态
     * @param productCode 产品编码
     * @return 记录数量
     */
    int countByStatusAndProduct(@Param("traceStatus") String traceStatus, 
                               @Param("productCode") String productCode);
} 