package com.iobaf.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.common.response.Result;
import com.iobaf.domain.quality.entity.QualityTrace;
import com.iobaf.domain.quality.service.QualityTraceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 质量追溯控制器
 * 提供质量追溯、缺陷分析、成本关联等功能
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
     * @param traceCode 追溯码
     * @param productCode 产品编码
     * @param batchNo 批次号
     * @param inspectionResult 检测结果
     * @return 质量追溯记录分页数据
     */
    @GetMapping("/traces")
    public Result<IPage<QualityTrace>> getQualityTracePage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String traceCode,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String batchNo,
            @RequestParam(required = false) String inspectionResult) {
        
        log.info("查询质量追溯记录列表，参数：current={}, size={}, traceCode={}, productCode={}, batchNo={}, inspectionResult={}",
                current, size, traceCode, productCode, batchNo, inspectionResult);
        
        try {
            Page<QualityTrace> page = new Page<>(current, size);
            IPage<QualityTrace> result = qualityTraceService.getQualityTracePage(page, traceCode, productCode, batchNo, inspectionResult);
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
     * 根据追溯码查询质量追溯记录
     * 
     * @param traceCode 追溯码
     * @return 质量追溯记录列表
     */
    @GetMapping("/traces/by-code/{traceCode}")
    public Result<List<QualityTrace>> getQualityTraceByCode(@PathVariable String traceCode) {
        log.info("根据追溯码查询质量追溯记录，traceCode={}", traceCode);
        
        try {
            List<QualityTrace> traces = qualityTraceService.getQualityTraceByCode(traceCode);
            return Result.success(traces);
        } catch (Exception e) {
            log.error("根据追溯码查询质量追溯记录失败，traceCode={}", traceCode, e);
            return Result.error("根据追溯码查询质量追溯记录失败：" + e.getMessage());
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
     * 获取缺陷分析数据
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param defectType 缺陷类型
     * @param defectLevel 缺陷等级
     * @return 缺陷分析数据
     */
    @GetMapping("/defect-analysis")
    public Result<Map<String, Object>> getDefectAnalysis(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) String defectType,
            @RequestParam(required = false) String defectLevel) {
        
        log.info("获取缺陷分析数据，startTime={}, endTime={}, defectType={}, defectLevel={}", 
                startTime, endTime, defectType, defectLevel);
        
        try {
            Map<String, Object> defectData = qualityTraceService.getDefectAnalysis(startTime, endTime, defectType, defectLevel);
            return Result.success(defectData);
        } catch (Exception e) {
            log.error("获取缺陷分析数据失败", e);
            return Result.error("获取缺陷分析数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取质量成本分析
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param productCode 产品编码
     * @return 质量成本分析数据
     */
    @GetMapping("/cost-analysis")
    public Result<Map<String, Object>> getQualityCostAnalysis(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) String productCode) {
        
        log.info("获取质量成本分析，startTime={}, endTime={}, productCode={}", startTime, endTime, productCode);
        
        try {
            Map<String, Object> costData = qualityTraceService.getQualityCostAnalysis(startTime, endTime, productCode);
            return Result.success(costData);
        } catch (Exception e) {
            log.error("获取质量成本分析失败", e);
            return Result.error("获取质量成本分析失败：" + e.getMessage());
        }
    }

    /**
     * 获取产品全生命周期追溯
     * 
     * @param traceCode 追溯码
     * @return 全生命周期追溯数据
     */
    @GetMapping("/lifecycle/{traceCode}")
    public Result<Map<String, Object>> getProductLifecycle(@PathVariable String traceCode) {
        log.info("获取产品全生命周期追溯，traceCode={}", traceCode);
        
        try {
            Map<String, Object> lifecycleData = qualityTraceService.getProductLifecycle(traceCode);
            return Result.success(lifecycleData);
        } catch (Exception e) {
            log.error("获取产品全生命周期追溯失败，traceCode={}", traceCode, e);
            return Result.error("获取产品全生命周期追溯失败：" + e.getMessage());
        }
    }

    /**
     * 生成追溯码
     * 
     * @param productCode 产品编码
     * @param batchNo 批次号
     * @return 追溯码
     */
    @PostMapping("/generate-trace-code")
    public Result<String> generateTraceCode(@RequestParam String productCode, @RequestParam String batchNo) {
        log.info("生成追溯码，productCode={}, batchNo={}", productCode, batchNo);
        
        try {
            String traceCode = qualityTraceService.generateTraceCode(productCode, batchNo);
            return Result.success(traceCode);
        } catch (Exception e) {
            log.error("生成追溯码失败，productCode={}", productCode, e);
            return Result.error("生成追溯码失败：" + e.getMessage());
        }
    }

    /**
     * 批量处理缺陷产品
     * 
     * @param traceIds 追溯记录ID列表
     * @param treatmentMethod 处理方式
     * @param treatedBy 处理人ID
     * @return 处理结果
     */
    @PostMapping("/batch-treat-defects")
    public Result<Boolean> batchTreatDefects(@RequestParam List<Long> traceIds,
                                           @RequestParam String treatmentMethod,
                                           @RequestParam Long treatedBy) {
        log.info("批量处理缺陷产品，traceIds={}, treatmentMethod={}, treatedBy={}", traceIds, treatmentMethod, treatedBy);
        
        try {
            boolean result = qualityTraceService.batchTreatDefects(traceIds, treatmentMethod, treatedBy);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("批量处理缺陷产品失败");
            }
        } catch (Exception e) {
            log.error("批量处理缺陷产品失败", e);
            return Result.error("批量处理缺陷产品失败：" + e.getMessage());
        }
    }
} 