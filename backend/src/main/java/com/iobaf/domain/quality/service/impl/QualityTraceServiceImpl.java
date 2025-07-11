package com.iobaf.domain.quality.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.quality.entity.QualityTrace;
import com.iobaf.domain.quality.repository.QualityTraceRepository;
import com.iobaf.domain.quality.service.QualityTraceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 质量追溯服务实现类
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Slf4j
@Service
public class QualityTraceServiceImpl implements QualityTraceService {

    @Autowired
    private QualityTraceRepository qualityTraceRepository;

    @Override
    public IPage<QualityTrace> getQualityTracePage(Page<QualityTrace> page,
                                                 String productCode,
                                                 String batchNo,
                                                 String traceStatus) {
        log.info("查询质量追溯记录列表，参数：productCode={}, batchNo={}, traceStatus={}", 
                productCode, batchNo, traceStatus);
        
        try {
            return qualityTraceRepository.selectQualityTracePage(page, productCode, batchNo, traceStatus);
        } catch (Exception e) {
            log.error("查询质量追溯记录列表失败", e);
            throw new RuntimeException("查询质量追溯记录列表失败", e);
        }
    }

    @Override
    public QualityTrace getQualityTraceById(Long traceId) {
        log.info("根据ID查询质量追溯记录详情，traceId={}", traceId);
        
        try {
            return qualityTraceRepository.selectById(traceId);
        } catch (Exception e) {
            log.error("查询质量追溯记录详情失败，traceId={}", traceId, e);
            throw new RuntimeException("查询质量追溯记录详情失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createQualityTrace(QualityTrace trace) {
        log.info("创建质量追溯记录，记录信息：{}", trace);
        
        try {
            // 设置创建时间
            trace.setCreatedAt(LocalDateTime.now());
            
            // 生成二维码
            if (trace.getQrCode() == null) {
                trace.setQrCode(generateQrCode(trace.getProductCode(), trace.getBatchNo()));
            }
            
            int result = qualityTraceRepository.insert(trace);
            
            if (result > 0) {
                log.info("质量追溯记录创建成功，traceId={}", trace.getId());
                return true;
            } else {
                log.error("质量追溯记录创建失败");
                return false;
            }
        } catch (Exception e) {
            log.error("创建质量追溯记录失败", e);
            throw new RuntimeException("创建质量追溯记录失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateQualityTrace(QualityTrace trace) {
        log.info("更新质量追溯记录信息，traceId={}", trace.getId());
        
        try {
            // 设置更新时间
            trace.setUpdatedAt(LocalDateTime.now());
            
            int result = qualityTraceRepository.updateById(trace);
            
            if (result > 0) {
                log.info("质量追溯记录更新成功，traceId={}", trace.getId());
                return true;
            } else {
                log.error("质量追溯记录更新失败，traceId={}", trace.getId());
                return false;
            }
        } catch (Exception e) {
            log.error("更新质量追溯记录失败，traceId={}", trace.getId(), e);
            throw new RuntimeException("更新质量追溯记录失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteQualityTrace(Long traceId) {
        log.info("删除质量追溯记录，traceId={}", traceId);
        
        try {
            int result = qualityTraceRepository.deleteById(traceId);
            
            if (result > 0) {
                log.info("质量追溯记录删除成功，traceId={}", traceId);
                return true;
            } else {
                log.error("质量追溯记录删除失败，traceId={}", traceId);
                return false;
            }
        } catch (Exception e) {
            log.error("删除质量追溯记录失败，traceId={}", traceId, e);
            throw new RuntimeException("删除质量追溯记录失败", e);
        }
    }

    @Override
    public List<QualityTrace> getQualityTracesByProductCode(String productCode) {
        log.info("根据产品编码查询追溯记录，productCode={}", productCode);
        
        try {
            return qualityTraceRepository.selectByProductCode(productCode);
        } catch (Exception e) {
            log.error("查询产品追溯记录失败，productCode={}", productCode, e);
            throw new RuntimeException("查询产品追溯记录失败", e);
        }
    }

    @Override
    public List<QualityTrace> getQualityTracesByBatchNo(String batchNo) {
        log.info("根据批次号查询追溯记录，batchNo={}", batchNo);
        
        try {
            return qualityTraceRepository.selectByBatchNo(batchNo);
        } catch (Exception e) {
            log.error("查询批次追溯记录失败，batchNo={}", batchNo, e);
            throw new RuntimeException("查询批次追溯记录失败", e);
        }
    }

    @Override
    public QualityTrace getQualityTraceByQrCode(String qrCode) {
        log.info("根据二维码查询追溯记录，qrCode={}", qrCode);
        
        try {
            return qualityTraceRepository.selectByQrCode(qrCode);
        } catch (Exception e) {
            log.error("查询二维码追溯记录失败，qrCode={}", qrCode, e);
            throw new RuntimeException("查询二维码追溯记录失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchCreateQualityTraces(List<QualityTrace> traces) {
        log.info("批量创建质量追溯记录，记录数量={}", traces.size());
        
        try {
            // 设置创建时间和二维码
            traces.forEach(trace -> {
                trace.setCreatedAt(LocalDateTime.now());
                if (trace.getQrCode() == null) {
                    trace.setQrCode(generateQrCode(trace.getProductCode(), trace.getBatchNo()));
                }
            });
            
            int result = qualityTraceRepository.batchInsert(traces);
            log.info("批量创建质量追溯记录成功，创建数量={}", result);
            return result;
        } catch (Exception e) {
            log.error("批量创建质量追溯记录失败", e);
            throw new RuntimeException("批量创建质量追溯记录失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateQualityTraces(List<QualityTrace> traces) {
        log.info("批量更新质量追溯记录，记录数量={}", traces.size());
        
        try {
            // 设置更新时间
            traces.forEach(trace -> trace.setUpdatedAt(LocalDateTime.now()));
            
            int result = qualityTraceRepository.batchUpdate(traces);
            log.info("批量更新质量追溯记录成功，更新数量={}", result);
            return result;
        } catch (Exception e) {
            log.error("批量更新质量追溯记录失败", e);
            throw new RuntimeException("批量更新质量追溯记录失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteQualityTraces(List<Long> traceIds) {
        log.info("批量删除质量追溯记录，记录ID数量={}", traceIds.size());
        
        try {
            int result = 0;
            for (Long traceId : traceIds) {
                if (qualityTraceRepository.deleteById(traceId) > 0) {
                    result++;
                }
            }
            log.info("批量删除质量追溯记录成功，删除数量={}", result);
            return result;
        } catch (Exception e) {
            log.error("批量删除质量追溯记录失败", e);
            throw new RuntimeException("批量删除质量追溯记录失败", e);
        }
    }

    @Override
    public String generateQrCode(String productCode, String batchNo) {
        log.info("生成产品二维码，productCode={}, batchNo={}", productCode, batchNo);
        
        try {
            // 生成唯一的二维码内容
            String qrContent = String.format("PRODUCT:%s:BATCH:%s:UUID:%s", 
                    productCode, batchNo, UUID.randomUUID().toString());
            
            log.info("二维码生成完成，内容={}", qrContent);
            return qrContent;
        } catch (Exception e) {
            log.error("生成二维码失败", e);
            throw new RuntimeException("生成二维码失败", e);
        }
    }

    @Override
    public Map<String, Object> getProductLifecycleTrace(String productCode, String batchNo) {
        log.info("获取产品全生命周期追溯信息，productCode={}, batchNo={}", productCode, batchNo);
        
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("productCode", productCode);
            result.put("batchNo", batchNo);
            
            // 获取产品追溯记录
            List<QualityTrace> traces = getQualityTracesByProductCode(productCode);
            result.put("traces", traces);
            
            // 计算追溯统计信息
            long totalTraces = traces.size();
            long completedTraces = traces.stream()
                    .filter(trace -> "COMPLETED".equals(trace.getTraceStatus()))
                    .count();
            long pendingTraces = traces.stream()
                    .filter(trace -> "PENDING".equals(trace.getTraceStatus()))
                    .count();
            
            result.put("totalTraces", totalTraces);
            result.put("completedTraces", completedTraces);
            result.put("pendingTraces", pendingTraces);
            result.put("completionRate", totalTraces > 0 ? (double) completedTraces / totalTraces : 0.0);
            
            log.info("产品全生命周期追溯信息获取完成");
            return result;
        } catch (Exception e) {
            log.error("获取产品全生命周期追溯信息失败", e);
            throw new RuntimeException("获取产品全生命周期追溯信息失败", e);
        }
    }

    @Override
    public Map<String, Object> getQualityTraceStatistics() {
        log.info("获取质量追溯统计信息");
        
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 统计各状态记录数量
            int completedCount = qualityTraceRepository.countByStatusAndProduct("COMPLETED", null);
            int pendingCount = qualityTraceRepository.countByStatusAndProduct("PENDING", null);
            int failedCount = qualityTraceRepository.countByStatusAndProduct("FAILED", null);
            
            statistics.put("completedCount", completedCount);
            statistics.put("pendingCount", pendingCount);
            statistics.put("failedCount", failedCount);
            statistics.put("totalCount", completedCount + pendingCount + failedCount);
            
            log.info("质量追溯统计信息获取完成");
            return statistics;
        } catch (Exception e) {
            log.error("获取质量追溯统计信息失败", e);
            throw new RuntimeException("获取质量追溯统计信息失败", e);
        }
    }
} 