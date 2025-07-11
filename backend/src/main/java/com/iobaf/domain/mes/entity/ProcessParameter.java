package com.iobaf.domain.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 工艺参数实体类
 * 记录生产过程中的工艺参数，用于成本计算和质量追溯
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("mes_process_parameters")
public class ProcessParameter {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 产品名称
     */
    @TableField("product_name")
    private String productName;

    /**
     * 工序编码
     */
    @TableField("process_code")
    private String processCode;

    /**
     * 工序名称
     */
    @TableField("process_name")
    private String processName;

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
     * 参数类型（TEMPERATURE-温度, PRESSURE-压力, SPEED-速度, TIME-时间, POWER-功率）
     */
    @TableField("parameter_type")
    private String parameterType;

    /**
     * 参数名称
     */
    @TableField("parameter_name")
    private String parameterName;

    /**
     * 参数值
     */
    @TableField("parameter_value")
    private BigDecimal parameterValue;

    /**
     * 参数单位
     */
    @TableField("parameter_unit")
    private String parameterUnit;

    /**
     * 标准值
     */
    @TableField("standard_value")
    private BigDecimal standardValue;

    /**
     * 上限值
     */
    @TableField("upper_limit")
    private BigDecimal upperLimit;

    /**
     * 下限值
     */
    @TableField("lower_limit")
    private BigDecimal lowerLimit;

    /**
     * 是否合格（0-不合格，1-合格）
     */
    @TableField("is_qualified")
    private Integer isQualified;

    /**
     * 记录时间
     */
    @TableField("record_time")
    private LocalDateTime recordTime;

    /**
     * 操作员ID
     */
    @TableField("operator_id")
    private Long operatorId;

    /**
     * 操作员姓名
     */
    @TableField("operator_name")
    private String operatorName;

    /**
     * 备注信息
     */
    @TableField("remarks")
    private String remarks;

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