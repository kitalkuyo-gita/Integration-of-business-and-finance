package com.iobaf.domain.customer.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 客户视图对象
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Data
public class CustomerVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系邮箱
     */
    private String contactEmail;

    /**
     * 地址
     */
    private String address;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 客户等级：1-普通，2-重要，3-VIP
     */
    private Integer customerLevel;

    /**
     * 客户等级名称
     */
    private String customerLevelName;

    /**
     * 状态：0-禁用，1-启用
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