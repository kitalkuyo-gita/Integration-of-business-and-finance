package com.iobaf.domain.mes.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.mes.entity.ProcessParameter;
import com.iobaf.domain.mes.repository.ProcessParameterRepository;
import com.iobaf.domain.mes.service.ProcessParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MES工艺参数服务实现类
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Slf4j
@Service
public class ProcessParameterServiceImpl implements ProcessParameterService {

    @Autowired
    private ProcessParameterRepository processParameterRepository;

    @Override
    public IPage<ProcessParameter> getProcessParameterPage(Page<ProcessParameter> page,
                                                        String parameterName,
                                                        String parameterType,
                                                        Long equipmentId) {
        log.info("查询工艺参数列表，参数：parameterName={}, parameterType={}, equipmentId={}", 
                parameterName, parameterType, equipmentId);
        
        try {
            return processParameterRepository.selectProcessParameterPage(page, parameterName, parameterType, equipmentId);
        } catch (Exception e) {
            log.error("查询工艺参数列表失败", e);
            throw new RuntimeException("查询工艺参数列表失败", e);
        }
    }

    @Override
    public ProcessParameter getProcessParameterById(Long parameterId) {
        log.info("根据ID查询工艺参数详情，parameterId={}", parameterId);
        
        try {
            return processParameterRepository.selectById(parameterId);
        } catch (Exception e) {
            log.error("查询工艺参数详情失败，parameterId={}", parameterId, e);
            throw new RuntimeException("查询工艺参数详情失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createProcessParameter(ProcessParameter parameter) {
        log.info("创建工艺参数，参数信息：{}", parameter);
        
        try {
            // 设置创建时间
            parameter.setCreatedAt(LocalDateTime.now());
            
            int result = processParameterRepository.insert(parameter);
            
            if (result > 0) {
                log.info("工艺参数创建成功，parameterId={}", parameter.getId());
                return true;
            } else {
                log.error("工艺参数创建失败");
                return false;
            }
        } catch (Exception e) {
            log.error("创建工艺参数失败", e);
            throw new RuntimeException("创建工艺参数失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProcessParameter(ProcessParameter parameter) {
        log.info("更新工艺参数信息，parameterId={}", parameter.getId());
        
        try {
            // 设置更新时间
            parameter.setUpdatedAt(LocalDateTime.now());
            
            int result = processParameterRepository.updateById(parameter);
            
            if (result > 0) {
                log.info("工艺参数更新成功，parameterId={}", parameter.getId());
                return true;
            } else {
                log.error("工艺参数更新失败，parameterId={}", parameter.getId());
                return false;
            }
        } catch (Exception e) {
            log.error("更新工艺参数失败，parameterId={}", parameter.getId(), e);
            throw new RuntimeException("更新工艺参数失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProcessParameter(Long parameterId) {
        log.info("删除工艺参数，parameterId={}", parameterId);
        
        try {
            int result = processParameterRepository.deleteById(parameterId);
            
            if (result > 0) {
                log.info("工艺参数删除成功，parameterId={}", parameterId);
                return true;
            } else {
                log.error("工艺参数删除失败，parameterId={}", parameterId);
                return false;
            }
        } catch (Exception e) {
            log.error("删除工艺参数失败，parameterId={}", parameterId, e);
            throw new RuntimeException("删除工艺参数失败", e);
        }
    }

    @Override
    public List<ProcessParameter> getProcessParametersByEquipmentId(Long equipmentId) {
        log.info("根据设备ID查询工艺参数，equipmentId={}", equipmentId);
        
        try {
            return processParameterRepository.selectByEquipmentId(equipmentId);
        } catch (Exception e) {
            log.error("查询设备工艺参数失败，equipmentId={}", equipmentId, e);
            throw new RuntimeException("查询设备工艺参数失败", e);
        }
    }

    @Override
    public List<ProcessParameter> getProcessParametersByType(String parameterType) {
        log.info("根据参数类型查询工艺参数，parameterType={}", parameterType);
        
        try {
            return processParameterRepository.selectByParameterType(parameterType);
        } catch (Exception e) {
            log.error("查询工艺参数失败，parameterType={}", parameterType, e);
            throw new RuntimeException("查询工艺参数失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchCreateProcessParameters(List<ProcessParameter> parameters) {
        log.info("批量创建工艺参数，参数数量={}", parameters.size());
        
        try {
            // 设置创建时间
            parameters.forEach(param -> param.setCreatedAt(LocalDateTime.now()));
            
            int result = processParameterRepository.batchInsert(parameters);
            log.info("批量创建工艺参数成功，创建数量={}", result);
            return result;
        } catch (Exception e) {
            log.error("批量创建工艺参数失败", e);
            throw new RuntimeException("批量创建工艺参数失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateProcessParameters(List<ProcessParameter> parameters) {
        log.info("批量更新工艺参数，参数数量={}", parameters.size());
        
        try {
            // 设置更新时间
            parameters.forEach(param -> param.setUpdatedAt(LocalDateTime.now()));
            
            int result = processParameterRepository.batchUpdate(parameters);
            log.info("批量更新工艺参数成功，更新数量={}", result);
            return result;
        } catch (Exception e) {
            log.error("批量更新工艺参数失败", e);
            throw new RuntimeException("批量更新工艺参数失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteProcessParameters(List<Long> parameterIds) {
        log.info("批量删除工艺参数，参数ID数量={}", parameterIds.size());
        
        try {
            int result = 0;
            for (Long parameterId : parameterIds) {
                if (processParameterRepository.deleteById(parameterId) > 0) {
                    result++;
                }
            }
            log.info("批量删除工艺参数成功，删除数量={}", result);
            return result;
        } catch (Exception e) {
            log.error("批量删除工艺参数失败", e);
            throw new RuntimeException("批量删除工艺参数失败", e);
        }
    }

    @Override
    public Map<String, Object> calculateProcessCost(Long equipmentId, 
                                                  Double processTime, 
                                                  Double materialCost) {
        log.info("计算工艺成本，equipmentId={}, processTime={}, materialCost={}", 
                equipmentId, processTime, materialCost);
        
        try {
            // 获取设备工艺参数
            List<ProcessParameter> parameters = getProcessParametersByEquipmentId(equipmentId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("equipmentId", equipmentId);
            result.put("processTime", processTime);
            result.put("materialCost", materialCost);
            result.put("parameters", parameters);
            
            // 计算工艺成本
            double laborCost = 0.0;
            double energyCost = 0.0;
            double overheadCost = 0.0;
            
            for (ProcessParameter param : parameters) {
                switch (param.getParameterType()) {
                    case "LABOR":
                        laborCost = processTime * param.getParameterValue() * param.getCostRate();
                        break;
                    case "ENERGY":
                        energyCost = processTime * param.getParameterValue() * param.getCostRate();
                        break;
                    case "OVERHEAD":
                        overheadCost = processTime * param.getParameterValue() * param.getCostRate();
                        break;
                }
            }
            
            double totalCost = materialCost + laborCost + energyCost + overheadCost;
            
            result.put("laborCost", laborCost);
            result.put("energyCost", energyCost);
            result.put("overheadCost", overheadCost);
            result.put("totalCost", totalCost);
            
            log.info("工艺成本计算完成，总成本={}", totalCost);
            return result;
        } catch (Exception e) {
            log.error("计算工艺成本失败", e);
            throw new RuntimeException("计算工艺成本失败", e);
        }
    }

    @Override
    public Map<String, Object> getProcessParameterStatistics() {
        log.info("获取工艺参数统计信息");
        
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 统计各类型参数数量
            int laborParamCount = processParameterRepository.countByTypeAndEquipment("LABOR", null);
            int energyParamCount = processParameterRepository.countByTypeAndEquipment("ENERGY", null);
            int overheadParamCount = processParameterRepository.countByTypeAndEquipment("OVERHEAD", null);
            
            statistics.put("laborParamCount", laborParamCount);
            statistics.put("energyParamCount", energyParamCount);
            statistics.put("overheadParamCount", overheadParamCount);
            statistics.put("totalParamCount", laborParamCount + energyParamCount + overheadParamCount);
            
            log.info("工艺参数统计信息获取完成");
            return statistics;
        } catch (Exception e) {
            log.error("获取工艺参数统计信息失败", e);
            throw new RuntimeException("获取工艺参数统计信息失败", e);
        }
    }
} 