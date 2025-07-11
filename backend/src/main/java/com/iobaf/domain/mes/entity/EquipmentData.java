package com.iobaf.domain.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备数据实体类
 * 记录设备运行数据，包括能耗、工时等信息
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("mes_equipment_data")
public class EquipmentData {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备编码
     */
    @TableField("equipment_code")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @TableField("equipment_name")
    private String equipmentName;

    /**
     * 设备类型（MACHINE-机床, FURNACE-炉子, PUMP-泵, COMPRESSOR-压缩机）
     */
    @TableField("equipment_type")
    private String equipmentType;

    /**
     * 数据类型（ENERGY-能耗, RUNTIME-运行时间, TEMPERATURE-温度, PRESSURE-压力）
     */
    @TableField("data_type")
    private String dataType;

    /**
     * 数据值
     */
    @TableField("data_value")
    private BigDecimal dataValue;

    /**
     * 数据单位
     */
    @TableField("data_unit")
    private String dataUnit;

    /**
     * 记录时间
     */
    @TableField("record_time")
    private LocalDateTime recordTime;

    /**
     * 工单号
     */
    @TableField("work_order_no")
    private String workOrderNo;

    /**
     * 产品编码
     */
    @TableField("product_code")
    private String productCode;

    /**
     * 工序编码
     */
    @TableField("process_code")
    private String processCode;

    /**
     * 运行状态（RUNNING-运行中, STOPPED-停止, MAINTENANCE-维护中）
     */
    @TableField("running_status")
    private String runningStatus;

    /**
     * 能耗成本（元）
     */
    @TableField("energy_cost")
    private BigDecimal energyCost;

    /**
     * 维护成本（元）
     */
    @TableField("maintenance_cost")
    private BigDecimal maintenanceCost;

    /**
     * 折旧成本（元）
     */
    @TableField("depreciation_cost")
    private BigDecimal depreciationCost;

    /**
     * 总成本（元）
     */
    @TableField("total_cost")
    private BigDecimal totalCost;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 是否删除（0-未删除，1-已删除）
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
} 