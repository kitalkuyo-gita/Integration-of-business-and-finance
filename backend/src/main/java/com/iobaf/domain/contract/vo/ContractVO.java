package com.iobaf.domain.contract.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合同视图对象
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Data
public class ContractVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 合同金额
     */
    private BigDecimal contractAmount;

    /**
     * 合同类型：1-销售合同，2-采购合同
     */
    private Integer contractType;

    /**
     * 合同类型名称
     */
    private String contractTypeName;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 状态：1-草稿，2-审批中，3-已生效，4-已完成，5-已终止
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 