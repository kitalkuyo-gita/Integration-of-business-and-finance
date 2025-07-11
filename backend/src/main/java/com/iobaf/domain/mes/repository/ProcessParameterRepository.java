package com.iobaf.domain.mes.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.mes.entity.ProcessParameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MES工艺参数数据访问层
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Mapper
public interface ProcessParameterRepository extends BaseMapper<ProcessParameter> {

    /**
     * 分页查询工艺参数列表
     * 
     * @param page 分页参数
     * @param parameterName 参数名称
     * @param parameterType 参数类型
     * @param equipmentId 设备ID
     * @return 工艺参数分页数据
     */
    IPage<ProcessParameter> selectProcessParameterPage(Page<ProcessParameter> page,
                                                     @Param("parameterName") String parameterName,
                                                     @Param("parameterType") String parameterType,
                                                     @Param("equipmentId") Long equipmentId);

    /**
     * 根据设备ID查询工艺参数
     * 
     * @param equipmentId 设备ID
     * @return 工艺参数列表
     */
    List<ProcessParameter> selectByEquipmentId(@Param("equipmentId") Long equipmentId);

    /**
     * 根据参数类型查询工艺参数
     * 
     * @param parameterType 参数类型
     * @return 工艺参数列表
     */
    List<ProcessParameter> selectByParameterType(@Param("parameterType") String parameterType);

    /**
     * 批量插入工艺参数
     * 
     * @param parameters 工艺参数列表
     * @return 插入数量
     */
    int batchInsert(@Param("parameters") List<ProcessParameter> parameters);

    /**
     * 批量更新工艺参数
     * 
     * @param parameters 工艺参数列表
     * @return 更新数量
     */
    int batchUpdate(@Param("parameters") List<ProcessParameter> parameters);

    /**
     * 统计工艺参数数量
     * 
     * @param parameterType 参数类型
     * @param equipmentId 设备ID
     * @return 参数数量
     */
    int countByTypeAndEquipment(@Param("parameterType") String parameterType, 
                               @Param("equipmentId") Long equipmentId);
} 