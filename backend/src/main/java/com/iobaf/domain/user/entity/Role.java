package com.iobaf.domain.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色实体 - 用户领域实体
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 角色编码 - 领域标识
     */
    @TableField("role_code")
    private String roleCode;

    /**
     * 角色描述
     */
    @TableField("description")
    private String description;

    /**
     * 角色状态：0-禁用，1-启用
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
     * 角色权限列表 - 领域关联
     */
    @TableField(exist = false)
    private List<Permission> permissions;

    /**
     * 领域业务方法 - 角色激活
     */
    public void activate() {
        if (this.status == 0) {
            this.status = 1;
        }
    }

    /**
     * 领域业务方法 - 角色禁用
     */
    public void deactivate() {
        if (this.status == 1) {
            this.status = 0;
        }
    }

    /**
     * 领域业务方法 - 检查角色是否激活
     */
    public boolean isActive() {
        return this.status == 1;
    }
} 