package com.iobaf.domain.mes.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.mes.entity.EquipmentData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备数据数据访问层
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Mapper
public interface EquipmentDataRepository extends BaseMapper<EquipmentData> {

    /**
     * 分页查询设备数据列表
     * 
     * @param page 分页参数
     * @param equipmentId 设备ID
     * @param dataType 数据类型
     * @return 设备数据分页数据
     */
    IPage<EquipmentData> selectEquipmentDataPage(Page<EquipmentData> page,
                                                @Param("equipmentId") Long equipmentId,
                                                @Param("dataType") String dataType);

    /**
     * 根据设备ID查询设备数据
     * 
     * @param equipmentId 设备ID
     * @return 设备数据列表
     */
    List<EquipmentData> selectByEquipmentId(@Param("equipmentId") Long equipmentId);

    /**
     * 根据数据类型查询设备数据
     * 
     * @param dataType 数据类型
     * @return 设备数据列表
     */
    List<EquipmentData> selectByDataType(@Param("dataType") String dataType);

    /**
     * 批量插入设备数据
     * 
     * @param dataList 设备数据列表
     * @return 插入数量
     */
    int batchInsert(@Param("dataList") List<EquipmentData> dataList);

    /**
     * 批量更新设备数据
     * 
     * @param dataList 设备数据列表
     * @return 更新数量
     */
    int batchUpdate(@Param("dataList") List<EquipmentData> dataList);

    /**
     * 统计设备数据数量
     * 
     * @param dataType 数据类型
     * @param equipmentId 设备ID
     * @return 数据数量
     */
    int countByTypeAndEquipment(@Param("dataType") String dataType, 
                               @Param("equipmentId") Long equipmentId);
} 