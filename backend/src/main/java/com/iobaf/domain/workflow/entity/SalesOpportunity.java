package com.iobaf.domain.workflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 销售机会实体类
 * 
 * @author iobaf
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sales_opportunities")
public class SalesOpportunity {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 机会编号
     */
    private String opportunityNo;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 机会名称
     */
    private String opportunityName;

    /**
     * 机会描述
     */
    private String description;

    /**
     * 机会金额
     */
    private BigDecimal amount;

    /**
     * 赢单概率（百分比）
     */
    private BigDecimal winProbability;

    /**
     * 预计成交时间
     */
    private LocalDateTime expectedCloseDate;

    /**
     * 机会来源
     */
    private String source;

    /**
     * 机会状态：LEAD（线索）、QUALIFIED（已确认）、PROPOSAL（方案中）、NEGOTIATION（谈判中）、CLOSED_WON（已成交）、CLOSED_LOST（已失败）
     */
    private String status;

    /**
     * 负责人ID
     */
    private Long ownerId;

    /**
     * 创建人ID
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 备注
     */
    private String remarks;
} 