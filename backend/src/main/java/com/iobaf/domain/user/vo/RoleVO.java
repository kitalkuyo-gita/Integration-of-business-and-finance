package com.iobaf.domain.user.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色视图对象 - 用户领域视图
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Data
public class RoleVO {

    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 角色状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 状态文本
     */
    private String statusText;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 获取状态文本
     */
    public String getStatusText() {
        if (status == null) {
            return "未知";
        }
        return status == 1 ? "启用" : "禁用";
    }
} 