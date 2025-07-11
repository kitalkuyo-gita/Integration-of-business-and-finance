package com.iobaf.domain.mes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.mes.entity.EquipmentData;

import java.util.List;
import java.util.Map;

/**
 * 设备数据服务接口
 * 
 * @author iobaf
 * @since 2024-01-01
 */
public interface EquipmentDataService {

    /**
     * 分页查询设备数据列表
     * 
     * @param page 分页参数
     * @param equipmentId 设备ID
     * @param dataType 数据类型
     * @return 设备数据分页数据
     */
    IPage<EquipmentData> getEquipmentDataPage(Page<EquipmentData> page,
                                             Long equipmentId,
                                             String dataType);

    /**
     * 根据ID查询设备数据详情
     * 
     * @param dataId 数据ID
     * @return 设备数据详情
     */
    EquipmentData getEquipmentDataById(Long dataId);

    /**
     * 创建设备数据
     * 
     * @param data 设备数据信息
     * @return 创建结果
     */
    boolean createEquipmentData(EquipmentData data);

    /**
     * 更新设备数据
     * 
     * @param data 设备数据信息
     * @return 更新结果
     */
    boolean updateEquipmentData(EquipmentData data);

    /**
     * 删除设备数据
     * 
     * @param dataId 数据ID
     * @return 删除结果
     */
    boolean deleteEquipmentData(Long dataId);

    /**
     * 根据设备ID查询设备数据
     * 
     * @param equipmentId 设备ID
     * @return 设备数据列表
     */
    List<EquipmentData> getEquipmentDataByEquipmentId(Long equipmentId);

    /**
     * 根据数据类型查询设备数据
     * 
     * @param dataType 数据类型
     * @return 设备数据列表
     */
    List<EquipmentData> getEquipmentDataByType(String dataType);

    /**
     * 批量创建设备数据
     * 
     * @param dataList 设备数据列表
     * @return 创建数量
     */
    int batchCreateEquipmentData(List<EquipmentData> dataList);

    /**
     * 批量更新设备数据
     * 
     * @param dataList 设备数据列表
     * @return 更新数量
     */
    int batchUpdateEquipmentData(List<EquipmentData> dataList);

    /**
     * 批量删除设备数据
     * 
     * @param dataIds 数据ID列表
     * @return 删除数量
     */
    int batchDeleteEquipmentData(List<Long> dataIds);

    /**
     * 获取设备数据统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getEquipmentDataStatistics();
} 