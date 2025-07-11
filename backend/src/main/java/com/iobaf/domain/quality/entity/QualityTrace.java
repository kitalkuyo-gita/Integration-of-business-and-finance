package com.iobaf.domain.quality.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 质量追溯实体类
 * 记录产品全生命周期的质量信息，支持二维码/RFID追溯
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("quality_traces")
public class QualityTrace {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 追溯码（二维码或RFID编码）
     */
    @TableField("trace_code")
    private String traceCode;

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
     * 批次号
     */
    @TableField("batch_no")
    private String batchNo;

    /**
     * 工单号
     */
    @TableField("work_order_no")
    private String workOrderNo;

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
     * 检测项目
     */
    @TableField("inspection_item")
    private String inspectionItem;

    /**
     * 检测标准
     */
    @TableField("inspection_standard")
    private String inspectionStandard;

    /**
     * 检测值
     */
    @TableField("inspection_value")
    private BigDecimal inspectionValue;

    /**
     * 检测单位
     */
    @TableField("inspection_unit")
    private String inspectionUnit;

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
     * 检测结果（PASS-合格, FAIL-不合格, WARNING-警告）
     */
    @TableField("inspection_result")
    private String inspectionResult;

    /**
     * 检测时间
     */
    @TableField("inspection_time")
    private LocalDateTime inspectionTime;

    /**
     * 缺陷类型（DIMENSION-尺寸, SURFACE-表面, MATERIAL-材料, ASSEMBLY-装配）
     */
    @TableField("defect_type")
    private String defectType;

    /**
     * 缺陷描述
     */
    @TableField("defect_description")
    private String defectDescription;

    /**
     * 缺陷等级（MINOR-轻微, MAJOR-严重, CRITICAL-致命）
     */
    @TableField("defect_level")
    private String defectLevel;

    /**
     * 处理方式（REPAIR-返修, SCRAP-报废, REWORK-重做）
     */
    @TableField("treatment_method")
    private String treatmentMethod;

    /**
     * 处理成本
     */
    @TableField("treatment_cost")
    private BigDecimal treatmentCost;

    /**
     * 处理人ID
     */
    @TableField("treated_by")
    private Long treatedBy;

    /**
     * 处理人姓名
     */
    @TableField("treated_by_name")
    private String treatedByName;

    /**
     * 处理时间
     */
    @TableField("treatment_time")
    private LocalDateTime treatmentTime;

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
     * 创建人ID
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 更新人ID
     */
    @TableField("updated_by")
    private Long updatedBy;

    /**
     * 是否删除（0-未删除，1-已删除）
     */
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
} 