package com.iobaf.domain.user.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 权限视图对象 - 用户领域视图
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Data
public class PermissionVO {

    /**
     * 权限ID
     */
    private Long id;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限编码
     */
    private String permissionCode;

    /**
     * 权限类型：1-菜单，2-按钮，3-接口
     */
    private Integer permissionType;

    /**
     * 权限类型文本
     */
    private String permissionTypeText;

    /**
     * 权限路径
     */
    private String permissionPath;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 权限状态：0-禁用，1-启用
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
     * 获取权限类型文本
     */
    public String getPermissionTypeText() {
        if (permissionType == null) {
            return "未知";
        }
        switch (permissionType) {
            case 1:
                return "菜单";
            case 2:
                return "按钮";
            case 3:
                return "接口";
            default:
                return "未知";
        }
    }

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