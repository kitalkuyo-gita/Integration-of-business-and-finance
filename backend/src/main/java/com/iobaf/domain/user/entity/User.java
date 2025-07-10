package com.iobaf.domain.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户实体 - 用户领域聚合根
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名 - 领域标识
     */
    @TableField("username")
    private String username;

    /**
     * 密码 - 加密存储
     */
    @TableField("password")
    private String password;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 用户状态：0-禁用，1-启用
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标志：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    /**
     * 用户角色列表 - 领域关联
     */
    @TableField(exist = false)
    private List<Role> roles;

    /**
     * 用户权限列表 - 领域关联
     */
    @TableField(exist = false)
    private List<Permission> permissions;

    /**
     * 领域业务方法 - 用户激活
     */
    public void activate() {
        if (this.status == 0) {
            this.status = 1;
        }
    }

    /**
     * 领域业务方法 - 用户禁用
     */
    public void deactivate() {
        if (this.status == 1) {
            this.status = 0;
        }
    }

    /**
     * 领域业务方法 - 检查用户是否激活
     */
    public boolean isActive() {
        return this.status == 1;
    }

    /**
     * 领域业务方法 - 检查用户是否有指定角色
     */
    public boolean hasRole(String roleCode) {
        if (roles == null || roles.isEmpty()) {
            return false;
        }
        return roles.stream().anyMatch(role -> roleCode.equals(role.getRoleCode()));
    }

    /**
     * 领域业务方法 - 检查用户是否有指定权限
     */
    public boolean hasPermission(String permissionCode) {
        if (permissions == null || permissions.isEmpty()) {
            return false;
        }
        return permissions.stream().anyMatch(permission -> permissionCode.equals(permission.getPermissionCode()));
    }
} 