package com.iobaf.domain.finance.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 凭证视图对象
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Data
public class VoucherVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 凭证号
     */
    private String voucherNo;

    /**
     * 凭证日期
     */
    private LocalDate voucherDate;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 凭证类型：1-收款，2-付款，3-转账，4-其他
     */
    private Integer voucherType;

    /**
     * 凭证类型名称
     */
    private String voucherTypeName;

    /**
     * 状态：1-草稿，2-已审核，3-已过账
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 制单人ID
     */
    private Long createUserId;

    /**
     * 制单人姓名
     */
    private String createUserName;

    /**
     * 审核人ID
     */
    private Long auditUserId;

    /**
     * 审核人姓名
     */
    private String auditUserName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 凭证明细列表
     */
    private List<VoucherDetailVO> details;
} 