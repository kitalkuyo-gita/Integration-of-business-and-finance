package com.iobaf.domain.finance.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 会计科目实体类
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fin_account")
public class Account {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 科目编码
     */
    @TableField("account_code")
    private String accountCode;

    /**
     * 科目名称
     */
    @TableField("account_name")
    private String accountName;

    /**
     * 父级科目ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 科目类型：1-资产，2-负债，3-权益，4-成本，5-损益
     */
    @TableField("account_type")
    private Integer accountType;

    /**
     * 余额方向：1-借，2-贷
     */
    @TableField("direction")
    private Integer direction;

    /**
     * 状态：0-禁用，1-启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
} 