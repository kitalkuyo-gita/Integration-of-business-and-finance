package com.iobaf.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.common.response.Result;
import com.iobaf.domain.quality.entity.QualityTrace;
import com.iobaf.domain.quality.service.QualityTraceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 质量追溯控制器
 * 提供质量追溯记录管理、全生命周期追溯、二维码生成等功能
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/quality")
public class QualityController {

    @Autowired
    private QualityTraceService qualityTraceService;

    /**
     * 分页查询质量追溯记录列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param productCode 产品编码
     * @param batchNo 批次号
     * @param traceStatus 追溯状态
     * @return 质量追溯记录分页数据
     */
    @GetMapping("/traces")
    public Result<IPage<QualityTrace>> getQualityTracePage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String batchNo,
            @RequestParam(required = false) String traceStatus) {
        
        log.info("查询质量追溯记录列表，参数：current={}, size={}, productCode={}, batchNo={}, traceStatus={}",
                current, size, productCode, batchNo, traceStatus);
        
        try {
            Page<QualityTrace> page = new Page<>(current, size);
            IPage<QualityTrace> result = qualityTraceService.getQualityTracePage(page, productCode, batchNo, traceStatus);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询质量追溯记录列表失败", e);
            return Result.error("查询质量追溯记录列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询质量追溯记录详情
     * 
     * @param traceId 追溯记录ID
     * @return 质量追溯记录详情
     */
    @GetMapping("/traces/{traceId}")
    public Result<QualityTrace> getQualityTraceById(@PathVariable Long traceId) {
        log.info("查询质量追溯记录详情，traceId={}", traceId);
        
        try {
            QualityTrace trace = qualityTraceService.getQualityTraceById(traceId);
            if (trace != null) {
                return Result.success(trace);
            } else {
                return Result.error("质量追溯记录不存在");
            }
        } catch (Exception e) {
            log.error("查询质量追溯记录详情失败，traceId={}", traceId, e);
            return Result.error("查询质量追溯记录详情失败：" + e.getMessage());
        }
    }

    /**
     * 创建质量追溯记录
     * 
     * @param trace 质量追溯记录信息
     * @return 创建结果
     */
    @PostMapping("/traces")
    public Result<Boolean> createQualityTrace(@RequestBody QualityTrace trace) {
        log.info("创建质量追溯记录，记录信息：{}", trace);
        
        try {
            boolean result = qualityTraceService.createQualityTrace(trace);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("创建质量追溯记录失败");
            }
        } catch (Exception e) {
            log.error("创建质量追溯记录失败", e);
            return Result.error("创建质量追溯记录失败：" + e.getMessage());
        }
    }

    /**
     * 更新质量追溯记录
     * 
     * @param traceId 追溯记录ID
     * @param trace 质量追溯记录信息
     * @return 更新结果
     */
    @PutMapping("/traces/{traceId}")
    public Result<Boolean> updateQualityTrace(@PathVariable Long traceId, @RequestBody QualityTrace trace) {
        log.info("更新质量追溯记录，traceId={}", traceId);
        
        try {
            trace.setId(traceId);
            boolean result = qualityTraceService.updateQualityTrace(trace);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("更新质量追溯记录失败");
            }
        } catch (Exception e) {
            log.error("更新质量追溯记录失败，traceId={}", traceId, e);
            return Result.error("更新质量追溯记录失败：" + e.getMessage());
        }
    }

    /**
     * 删除质量追溯记录
     * 
     * @param traceId 追溯记录ID
     * @return 删除结果
     */
    @DeleteMapping("/traces/{traceId}")
    public Result<Boolean> deleteQualityTrace(@PathVariable Long traceId) {
        log.info("删除质量追溯记录，traceId={}", traceId);
        
        try {
            boolean result = qualityTraceService.deleteQualityTrace(traceId);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("删除质量追溯记录失败");
            }
        } catch (Exception e) {
            log.error("删除质量追溯记录失败，traceId={}", traceId, e);
            return Result.error("删除质量追溯记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据产品编码查询追溯记录
     * 
     * @param productCode 产品编码
     * @return 追溯记录列表
     */
    @GetMapping("/traces/product/{productCode}")
    public Result<List<QualityTrace>> getQualityTracesByProductCode(@PathVariable String productCode) {
        log.info("查询产品追溯记录，productCode={}", productCode);
        
        try {
            List<QualityTrace> traces = qualityTraceService.getQualityTracesByProductCode(productCode);
            return Result.success(traces);
        } catch (Exception e) {
            log.error("查询产品追溯记录失败，productCode={}", productCode, e);
            return Result.error("查询产品追溯记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据批次号查询追溯记录
     * 
     * @param batchNo 批次号
     * @return 追溯记录列表
     */
    @GetMapping("/traces/batch/{batchNo}")
    public Result<List<QualityTrace>> getQualityTracesByBatchNo(@PathVariable String batchNo) {
        log.info("查询批次追溯记录，batchNo={}", batchNo);
        
        try {
            List<QualityTrace> traces = qualityTraceService.getQualityTracesByBatchNo(batchNo);
            return Result.success(traces);
        } catch (Exception e) {
            log.error("查询批次追溯记录失败，batchNo={}", batchNo, e);
            return Result.error("查询批次追溯记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据二维码查询追溯记录
     * 
     * @param qrCode 二维码
     * @return 追溯记录
     */
    @GetMapping("/traces/qr/{qrCode}")
    public Result<QualityTrace> getQualityTraceByQrCode(@PathVariable String qrCode) {
        log.info("查询二维码追溯记录，qrCode={}", qrCode);
        
        try {
            QualityTrace trace = qualityTraceService.getQualityTraceByQrCode(qrCode);
            if (trace != null) {
                return Result.success(trace);
            } else {
                return Result.error("二维码追溯记录不存在");
            }
        } catch (Exception e) {
            log.error("查询二维码追溯记录失败，qrCode={}", qrCode, e);
            return Result.error("查询二维码追溯记录失败：" + e.getMessage());
        }
    }

    /**
     * 批量创建质量追溯记录
     * 
     * @param traces 质量追溯记录列表
     * @return 创建数量
     */
    @PostMapping("/traces/batch")
    public Result<Integer> batchCreateQualityTraces(@RequestBody List<QualityTrace> traces) {
        log.info("批量创建质量追溯记录，记录数量={}", traces.size());
        
        try {
            int result = qualityTraceService.batchCreateQualityTraces(traces);
            return Result.success(result);
        } catch (Exception e) {
            log.error("批量创建质量追溯记录失败", e);
            return Result.error("批量创建质量追溯记录失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新质量追溯记录
     * 
     * @param traces 质量追溯记录列表
     * @return 更新数量
     */
    @PutMapping("/traces/batch")
    public Result<Integer> batchUpdateQualityTraces(@RequestBody List<QualityTrace> traces) {
        log.info("批量更新质量追溯记录，记录数量={}", traces.size());
        
        try {
            int result = qualityTraceService.batchUpdateQualityTraces(traces);
            return Result.success(result);
        } catch (Exception e) {
            log.error("批量更新质量追溯记录失败", e);
            return Result.error("批量更新质量追溯记录失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除质量追溯记录
     * 
     * @param traceIds 追溯记录ID列表
     * @return 删除数量
     */
    @DeleteMapping("/traces/batch")
    public Result<Integer> batchDeleteQualityTraces(@RequestBody List<Long> traceIds) {
        log.info("批量删除质量追溯记录，记录ID数量={}", traceIds.size());
        
        try {
            int result = qualityTraceService.batchDeleteQualityTraces(traceIds);
            return Result.success(result);
        } catch (Exception e) {
            log.error("批量删除质量追溯记录失败", e);
            return Result.error("批量删除质量追溯记录失败：" + e.getMessage());
        }
    }

    /**
     * 生成产品二维码
     * 
     * @param productCode 产品编码
     * @param batchNo 批次号
     * @return 二维码内容
     */
    @PostMapping("/qr-code/generate")
    public Result<String> generateQrCode(@RequestParam String productCode, @RequestParam String batchNo) {
        log.info("生成产品二维码，productCode={}, batchNo={}", productCode, batchNo);
        
        try {
            String qrCode = qualityTraceService.generateQrCode(productCode, batchNo);
            return Result.success(qrCode);
        } catch (Exception e) {
            log.error("生成产品二维码失败", e);
            return Result.error("生成产品二维码失败：" + e.getMessage());
        }
    }

    /**
     * 获取产品全生命周期追溯信息
     * 
     * @param productCode 产品编码
     * @param batchNo 批次号
     * @return 追溯信息
     */
    @GetMapping("/lifecycle-trace")
    public Result<Map<String, Object>> getProductLifecycleTrace(@RequestParam String productCode, 
                                                               @RequestParam String batchNo) {
        log.info("获取产品全生命周期追溯信息，productCode={}, batchNo={}", productCode, batchNo);
        
        try {
            Map<String, Object> result = qualityTraceService.getProductLifecycleTrace(productCode, batchNo);
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取产品全生命周期追溯信息失败", e);
            return Result.error("获取产品全生命周期追溯信息失败：" + e.getMessage());
        }
    }

    /**
     * 获取质量追溯统计信息
     * 
     * @return 统计信息
     */
    @GetMapping("/traces/statistics")
    public Result<Map<String, Object>> getQualityTraceStatistics() {
        log.info("获取质量追溯统计信息");
        
        try {
            Map<String, Object> statistics = qualityTraceService.getQualityTraceStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取质量追溯统计信息失败", e);
            return Result.error("获取质量追溯统计信息失败：" + e.getMessage());
        }
    }
} 