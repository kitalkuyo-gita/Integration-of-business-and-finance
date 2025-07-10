package com.iobaf.domain.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 权限实体 - 用户领域实体
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 权限名称
     */
    @TableField("permission_name")
    private String permissionName;

    /**
     * 权限编码 - 领域标识
     */
    @TableField("permission_code")
    private String permissionCode;

    /**
     * 权限类型：1-菜单，2-按钮，3-接口
     */
    @TableField("permission_type")
    private Integer permissionType;

    /**
     * 权限路径
     */
    @TableField("permission_path")
    private String permissionPath;

    /**
     * 权限描述
     */
    @TableField("description")
    private String description;

    /**
     * 权限状态：0-禁用，1-启用
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
     * 领域业务方法 - 权限激活
     */
    public void activate() {
        if (this.status == 0) {
            this.status = 1;
        }
    }

    /**
     * 领域业务方法 - 权限禁用
     */
    public void deactivate() {
        if (this.status == 1) {
            this.status = 0;
        }
    }

    /**
     * 领域业务方法 - 检查权限是否激活
     */
    public boolean isActive() {
        return this.status == 1;
    }

    /**
     * 领域业务方法 - 检查是否为菜单权限
     */
    public boolean isMenuPermission() {
        return this.permissionType == 1;
    }

    /**
     * 领域业务方法 - 检查是否为按钮权限
     */
    public boolean isButtonPermission() {
        return this.permissionType == 2;
    }

    /**
     * 领域业务方法 - 检查是否为接口权限
     */
    public boolean isApiPermission() {
        return this.permissionType == 3;
    }
} 