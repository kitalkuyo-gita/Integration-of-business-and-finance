package com.iobaf.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.common.response.Result;
import com.iobaf.domain.mes.entity.ProcessParameter;
import com.iobaf.domain.mes.entity.EquipmentData;
import com.iobaf.domain.mes.service.ProcessParameterService;
import com.iobaf.domain.mes.service.EquipmentDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * MES系统集成控制器
 * 提供工艺参数管理、设备数据采集、成本计算等功能
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/mes")
public class MesController {

    @Autowired
    private ProcessParameterService processParameterService;

    @Autowired
    private EquipmentDataService equipmentDataService;

    /**
     * 分页查询工艺参数列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param workOrderNo 工单号
     * @param productCode 产品编码
     * @param processCode 工序编码
     * @param parameterType 参数类型
     * @return 工艺参数分页数据
     */
    @GetMapping("/process-parameters")
    public Result<IPage<ProcessParameter>> getProcessParameterPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String workOrderNo,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String processCode,
            @RequestParam(required = false) String parameterType) {
        
        log.info("查询工艺参数列表，参数：current={}, size={}, workOrderNo={}, productCode={}, processCode={}, parameterType={}",
                current, size, workOrderNo, productCode, processCode, parameterType);
        
        try {
            Page<ProcessParameter> page = new Page<>(current, size);
            IPage<ProcessParameter> result = processParameterService.getProcessParameterPage(page, workOrderNo, productCode, processCode, parameterType);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询工艺参数列表失败", e);
            return Result.error("查询工艺参数列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询工艺参数详情
     * 
     * @param parameterId 参数ID
     * @return 工艺参数详情
     */
    @GetMapping("/process-parameters/{parameterId}")
    public Result<ProcessParameter> getProcessParameterById(@PathVariable Long parameterId) {
        log.info("查询工艺参数详情，parameterId={}", parameterId);
        
        try {
            ProcessParameter parameter = processParameterService.getProcessParameterById(parameterId);
            if (parameter != null) {
                return Result.success(parameter);
            } else {
                return Result.error("工艺参数不存在");
            }
        } catch (Exception e) {
            log.error("查询工艺参数详情失败，parameterId={}", parameterId, e);
            return Result.error("查询工艺参数详情失败：" + e.getMessage());
        }
    }

    /**
     * 创建工艺参数
     * 
     * @param parameter 工艺参数信息
     * @return 创建结果
     */
    @PostMapping("/process-parameters")
    public Result<Boolean> createProcessParameter(@RequestBody ProcessParameter parameter) {
        log.info("创建工艺参数，参数信息：{}", parameter);
        
        try {
            boolean result = processParameterService.createProcessParameter(parameter);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("创建工艺参数失败");
            }
        } catch (Exception e) {
            log.error("创建工艺参数失败", e);
            return Result.error("创建工艺参数失败：" + e.getMessage());
        }
    }

    /**
     * 更新工艺参数
     * 
     * @param parameterId 参数ID
     * @param parameter 工艺参数信息
     * @return 更新结果
     */
    @PutMapping("/process-parameters/{parameterId}")
    public Result<Boolean> updateProcessParameter(@PathVariable Long parameterId, @RequestBody ProcessParameter parameter) {
        log.info("更新工艺参数，parameterId={}", parameterId);
        
        try {
            parameter.setId(parameterId);
            boolean result = processParameterService.updateProcessParameter(parameter);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("更新工艺参数失败");
            }
        } catch (Exception e) {
            log.error("更新工艺参数失败，parameterId={}", parameterId, e);
            return Result.error("更新工艺参数失败：" + e.getMessage());
        }
    }

    /**
     * 删除工艺参数
     * 
     * @param parameterId 参数ID
     * @return 删除结果
     */
    @DeleteMapping("/process-parameters/{parameterId}")
    public Result<Boolean> deleteProcessParameter(@PathVariable Long parameterId) {
        log.info("删除工艺参数，parameterId={}", parameterId);
        
        try {
            boolean result = processParameterService.deleteProcessParameter(parameterId);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("删除工艺参数失败");
            }
        } catch (Exception e) {
            log.error("删除工艺参数失败，parameterId={}", parameterId, e);
            return Result.error("删除工艺参数失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询设备数据列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param equipmentCode 设备编码
     * @param dataType 数据类型
     * @param runningStatus 运行状态
     * @return 设备数据分页数据
     */
    @GetMapping("/equipment-data")
    public Result<IPage<EquipmentData>> getEquipmentDataPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String equipmentCode,
            @RequestParam(required = false) String dataType,
            @RequestParam(required = false) String runningStatus) {
        
        log.info("查询设备数据列表，参数：current={}, size={}, equipmentCode={}, dataType={}, runningStatus={}",
                current, size, equipmentCode, dataType, runningStatus);
        
        try {
            Page<EquipmentData> page = new Page<>(current, size);
            IPage<EquipmentData> result = equipmentDataService.getEquipmentDataPage(page, equipmentCode, dataType, runningStatus);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询设备数据列表失败", e);
            return Result.error("查询设备数据列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询设备数据详情
     * 
     * @param dataId 数据ID
     * @return 设备数据详情
     */
    @GetMapping("/equipment-data/{dataId}")
    public Result<EquipmentData> getEquipmentDataById(@PathVariable Long dataId) {
        log.info("查询设备数据详情，dataId={}", dataId);
        
        try {
            EquipmentData data = equipmentDataService.getEquipmentDataById(dataId);
            if (data != null) {
                return Result.success(data);
            } else {
                return Result.error("设备数据不存在");
            }
        } catch (Exception e) {
            log.error("查询设备数据详情失败，dataId={}", dataId, e);
            return Result.error("查询设备数据详情失败：" + e.getMessage());
        }
    }

    /**
     * 创建设备数据
     * 
     * @param data 设备数据信息
     * @return 创建结果
     */
    @PostMapping("/equipment-data")
    public Result<Boolean> createEquipmentData(@RequestBody EquipmentData data) {
        log.info("创建设备数据，数据信息：{}", data);
        
        try {
            boolean result = equipmentDataService.createEquipmentData(data);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("创建设备数据失败");
            }
        } catch (Exception e) {
            log.error("创建设备数据失败", e);
            return Result.error("创建设备数据失败：" + e.getMessage());
        }
    }

    /**
     * 更新设备数据
     * 
     * @param dataId 数据ID
     * @param data 设备数据信息
     * @return 更新结果
     */
    @PutMapping("/equipment-data/{dataId}")
    public Result<Boolean> updateEquipmentData(@PathVariable Long dataId, @RequestBody EquipmentData data) {
        log.info("更新设备数据，dataId={}", dataId);
        
        try {
            data.setId(dataId);
            boolean result = equipmentDataService.updateEquipmentData(data);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("更新设备数据失败");
            }
        } catch (Exception e) {
            log.error("更新设备数据失败，dataId={}", dataId, e);
            return Result.error("更新设备数据失败：" + e.getMessage());
        }
    }

    /**
     * 删除设备数据
     * 
     * @param dataId 数据ID
     * @return 删除结果
     */
    @DeleteMapping("/equipment-data/{dataId}")
    public Result<Boolean> deleteEquipmentData(@PathVariable Long dataId) {
        log.info("删除设备数据，dataId={}", dataId);
        
        try {
            boolean result = equipmentDataService.deleteEquipmentData(dataId);
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("删除设备数据失败");
            }
        } catch (Exception e) {
            log.error("删除设备数据失败，dataId={}", dataId, e);
            return Result.error("删除设备数据失败：" + e.getMessage());
        }
    }

    /**
     * 计算工艺成本
     * 
     * @param workOrderNo 工单号
     * @param productCode 产品编码
     * @return 成本计算结果
     */
    @PostMapping("/cost-calculation")
    public Result<Map<String, Object>> calculateProcessCost(@RequestParam String workOrderNo,
                                                          @RequestParam String productCode) {
        log.info("计算工艺成本，workOrderNo={}, productCode={}", workOrderNo, productCode);
        
        try {
            Map<String, Object> costResult = processParameterService.calculateProcessCost(workOrderNo, productCode);
            return Result.success(costResult);
        } catch (Exception e) {
            log.error("计算工艺成本失败，workOrderNo={}", workOrderNo, e);
            return Result.error("计算工艺成本失败：" + e.getMessage());
        }
    }

    /**
     * 获取设备能耗分析
     * 
     * @param equipmentCode 设备编码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 能耗分析数据
     */
    @GetMapping("/energy-analysis")
    public Result<Map<String, Object>> getEnergyAnalysis(@RequestParam String equipmentCode,
                                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        log.info("获取设备能耗分析，equipmentCode={}, startTime={}, endTime={}", equipmentCode, startTime, endTime);
        
        try {
            Map<String, Object> energyData = equipmentDataService.getEnergyAnalysis(equipmentCode, startTime, endTime);
            return Result.success(energyData);
        } catch (Exception e) {
            log.error("获取设备能耗分析失败，equipmentCode={}", equipmentCode, e);
            return Result.error("获取设备能耗分析失败：" + e.getMessage());
        }
    }

    /**
     * 获取设备运行效率分析
     * 
     * @param equipmentCode 设备编码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 运行效率分析数据
     */
    @GetMapping("/efficiency-analysis")
    public Result<Map<String, Object>> getEfficiencyAnalysis(@RequestParam String equipmentCode,
                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        log.info("获取设备运行效率分析，equipmentCode={}, startTime={}, endTime={}", equipmentCode, startTime, endTime);
        
        try {
            Map<String, Object> efficiencyData = equipmentDataService.getEfficiencyAnalysis(equipmentCode, startTime, endTime);
            return Result.success(efficiencyData);
        } catch (Exception e) {
            log.error("获取设备运行效率分析失败，equipmentCode={}", equipmentCode, e);
            return Result.error("获取设备运行效率分析失败：" + e.getMessage());
        }
    }

    /**
     * 同步MES系统数据
     * 
     * @param dataType 数据类型（PROCESS_PARAMETER-工艺参数, EQUIPMENT_DATA-设备数据）
     * @param data 同步数据
     * @return 同步结果
     */
    @PostMapping("/sync/{dataType}")
    public Result<Boolean> syncMesData(@PathVariable String dataType, @RequestBody Map<String, Object> data) {
        log.info("同步MES系统数据，dataType={}", dataType);
        
        try {
            boolean result = false;
            if ("PROCESS_PARAMETER".equals(dataType)) {
                result = processParameterService.syncMesData(data);
            } else if ("EQUIPMENT_DATA".equals(dataType)) {
                result = equipmentDataService.syncMesData(data);
            } else {
                return Result.error("不支持的数据类型：" + dataType);
            }
            
            if (result) {
                return Result.success(true);
            } else {
                return Result.error("同步MES系统数据失败");
            }
        } catch (Exception e) {
            log.error("同步MES系统数据失败，dataType={}", dataType, e);
            return Result.error("同步MES系统数据失败：" + e.getMessage());
        }
    }
} 