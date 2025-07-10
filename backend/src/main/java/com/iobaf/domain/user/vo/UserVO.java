package com.iobaf.domain.user.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户视图对象 - 用户领域视图
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Data
public class UserVO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户状态：0-禁用，1-启用
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
     * 角色列表
     */
    private List<RoleVO> roles;

    /**
     * 权限列表
     */
    private List<PermissionVO> permissions;

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