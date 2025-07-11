package com.iobaf.domain.mes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.mes.entity.ProcessParameter;

import java.util.List;
import java.util.Map;

/**
 * MES工艺参数服务接口
 * 
 * @author iobaf
 * @since 2024-01-01
 */
public interface ProcessParameterService {

    /**
     * 分页查询工艺参数列表
     * 
     * @param page 分页参数
     * @param parameterName 参数名称
     * @param parameterType 参数类型
     * @param equipmentId 设备ID
     * @return 工艺参数分页数据
     */
    IPage<ProcessParameter> getProcessParameterPage(Page<ProcessParameter> page,
                                                 String parameterName,
                                                 String parameterType,
                                                 Long equipmentId);

    /**
     * 根据ID查询工艺参数详情
     * 
     * @param parameterId 参数ID
     * @return 工艺参数详情
     */
    ProcessParameter getProcessParameterById(Long parameterId);

    /**
     * 创建工艺参数
     * 
     * @param parameter 工艺参数信息
     * @return 创建结果
     */
    boolean createProcessParameter(ProcessParameter parameter);

    /**
     * 更新工艺参数
     * 
     * @param parameter 工艺参数信息
     * @return 更新结果
     */
    boolean updateProcessParameter(ProcessParameter parameter);

    /**
     * 删除工艺参数
     * 
     * @param parameterId 参数ID
     * @return 删除结果
     */
    boolean deleteProcessParameter(Long parameterId);

    /**
     * 根据设备ID查询工艺参数
     * 
     * @param equipmentId 设备ID
     * @return 工艺参数列表
     */
    List<ProcessParameter> getProcessParametersByEquipmentId(Long equipmentId);

    /**
     * 根据参数类型查询工艺参数
     * 
     * @param parameterType 参数类型
     * @return 工艺参数列表
     */
    List<ProcessParameter> getProcessParametersByType(String parameterType);

    /**
     * 批量创建工艺参数
     * 
     * @param parameters 工艺参数列表
     * @return 创建数量
     */
    int batchCreateProcessParameters(List<ProcessParameter> parameters);

    /**
     * 批量更新工艺参数
     * 
     * @param parameters 工艺参数列表
     * @return 更新数量
     */
    int batchUpdateProcessParameters(List<ProcessParameter> parameters);

    /**
     * 批量删除工艺参数
     * 
     * @param parameterIds 参数ID列表
     * @return 删除数量
     */
    int batchDeleteProcessParameters(List<Long> parameterIds);

    /**
     * 计算工艺成本
     * 
     * @param equipmentId 设备ID
     * @param processTime 加工时间
     * @param materialCost 材料成本
     * @return 成本计算结果
     */
    Map<String, Object> calculateProcessCost(Long equipmentId, 
                                           Double processTime, 
                                           Double materialCost);

    /**
     * 获取工艺参数统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getProcessParameterStatistics();
} 