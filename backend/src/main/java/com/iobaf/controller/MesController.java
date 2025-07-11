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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * MES系统控制器
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
     * @param parameterName 参数名称
     * @param parameterType 参数类型
     * @param equipmentId 设备ID
     * @return 工艺参数分页数据
     */
    @GetMapping("/process-parameters")
    public Result<IPage<ProcessParameter>> getProcessParameterPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String parameterName,
            @RequestParam(required = false) String parameterType,
            @RequestParam(required = false) Long equipmentId) {
        
        log.info("查询工艺参数列表，参数：current={}, size={}, parameterName={}, parameterType={}, equipmentId={}",
                current, size, parameterName, parameterType, equipmentId);
        
        try {
            Page<ProcessParameter> page = new Page<>(current, size);
            IPage<ProcessParameter> result = processParameterService.getProcessParameterPage(page, parameterName, parameterType, equipmentId);
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
     * 根据设备ID查询工艺参数
     * 
     * @param equipmentId 设备ID
     * @return 工艺参数列表
     */
    @GetMapping("/process-parameters/equipment/{equipmentId}")
    public Result<List<ProcessParameter>> getProcessParametersByEquipmentId(@PathVariable Long equipmentId) {
        log.info("查询设备工艺参数，equipmentId={}", equipmentId);
        
        try {
            List<ProcessParameter> parameters = processParameterService.getProcessParametersByEquipmentId(equipmentId);
            return Result.success(parameters);
        } catch (Exception e) {
            log.error("查询设备工艺参数失败，equipmentId={}", equipmentId, e);
            return Result.error("查询设备工艺参数失败：" + e.getMessage());
        }
    }

    /**
     * 根据参数类型查询工艺参数
     * 
     * @param parameterType 参数类型
     * @return 工艺参数列表
     */
    @GetMapping("/process-parameters/type/{parameterType}")
    public Result<List<ProcessParameter>> getProcessParametersByType(@PathVariable String parameterType) {
        log.info("查询工艺参数，parameterType={}", parameterType);
        
        try {
            List<ProcessParameter> parameters = processParameterService.getProcessParametersByType(parameterType);
            return Result.success(parameters);
        } catch (Exception e) {
            log.error("查询工艺参数失败，parameterType={}", parameterType, e);
            return Result.error("查询工艺参数失败：" + e.getMessage());
        }
    }

    /**
     * 批量创建工艺参数
     * 
     * @param parameters 工艺参数列表
     * @return 创建数量
     */
    @PostMapping("/process-parameters/batch")
    public Result<Integer> batchCreateProcessParameters(@RequestBody List<ProcessParameter> parameters) {
        log.info("批量创建工艺参数，参数数量={}", parameters.size());
        
        try {
            int result = processParameterService.batchCreateProcessParameters(parameters);
            return Result.success(result);
        } catch (Exception e) {
            log.error("批量创建工艺参数失败", e);
            return Result.error("批量创建工艺参数失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新工艺参数
     * 
     * @param parameters 工艺参数列表
     * @return 更新数量
     */
    @PutMapping("/process-parameters/batch")
    public Result<Integer> batchUpdateProcessParameters(@RequestBody List<ProcessParameter> parameters) {
        log.info("批量更新工艺参数，参数数量={}", parameters.size());
        
        try {
            int result = processParameterService.batchUpdateProcessParameters(parameters);
            return Result.success(result);
        } catch (Exception e) {
            log.error("批量更新工艺参数失败", e);
            return Result.error("批量更新工艺参数失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除工艺参数
     * 
     * @param parameterIds 参数ID列表
     * @return 删除数量
     */
    @DeleteMapping("/process-parameters/batch")
    public Result<Integer> batchDeleteProcessParameters(@RequestBody List<Long> parameterIds) {
        log.info("批量删除工艺参数，参数ID数量={}", parameterIds.size());
        
        try {
            int result = processParameterService.batchDeleteProcessParameters(parameterIds);
            return Result.success(result);
        } catch (Exception e) {
            log.error("批量删除工艺参数失败", e);
            return Result.error("批量删除工艺参数失败：" + e.getMessage());
        }
    }

    /**
     * 计算工艺成本
     * 
     * @param equipmentId 设备ID
     * @param processTime 加工时间
     * @param materialCost 材料成本
     * @return 成本计算结果
     */
    @PostMapping("/process-cost/calculate")
    public Result<Map<String, Object>> calculateProcessCost(@RequestParam Long equipmentId,
                                                          @RequestParam Double processTime,
                                                          @RequestParam Double materialCost) {
        log.info("计算工艺成本，equipmentId={}, processTime={}, materialCost={}", equipmentId, processTime, materialCost);
        
        try {
            Map<String, Object> result = processParameterService.calculateProcessCost(equipmentId, processTime, materialCost);
            return Result.success(result);
        } catch (Exception e) {
            log.error("计算工艺成本失败", e);
            return Result.error("计算工艺成本失败：" + e.getMessage());
        }
    }

    /**
     * 获取工艺参数统计信息
     * 
     * @return 统计信息
     */
    @GetMapping("/process-parameters/statistics")
    public Result<Map<String, Object>> getProcessParameterStatistics() {
        log.info("获取工艺参数统计信息");
        
        try {
            Map<String, Object> statistics = processParameterService.getProcessParameterStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取工艺参数统计信息失败", e);
            return Result.error("获取工艺参数统计信息失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询设备数据列表
     * 
     * @param current 当前页
     * @param size 每页大小
     * @param equipmentId 设备ID
     * @param dataType 数据类型
     * @return 设备数据分页数据
     */
    @GetMapping("/equipment-data")
    public Result<IPage<EquipmentData>> getEquipmentDataPage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long equipmentId,
            @RequestParam(required = false) String dataType) {
        
        log.info("查询设备数据列表，参数：current={}, size={}, equipmentId={}, dataType={}",
                current, size, equipmentId, dataType);
        
        try {
            Page<EquipmentData> page = new Page<>(current, size);
            IPage<EquipmentData> result = equipmentDataService.getEquipmentDataPage(page, equipmentId, dataType);
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
} 