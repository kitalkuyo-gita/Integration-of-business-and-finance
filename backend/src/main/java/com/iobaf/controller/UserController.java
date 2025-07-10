package com.iobaf.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.common.response.Result;
import com.iobaf.domain.user.entity.User;
import com.iobaf.domain.user.service.UserDomainService;
import com.iobaf.domain.user.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户控制器 - 应用层
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    private final UserDomainService userDomainService;

    /**
     * 分页查询用户列表
     */
    @GetMapping
    @Operation(summary = "分页查询用户列表", description = "根据条件分页查询用户列表")
    @PreAuthorize("hasAuthority('user:list')")
    public Result<IPage<UserVO>> getUserPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "真实姓名") @RequestParam(required = false) String realName,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        log.info("分页查询用户列表，参数：current={}, size={}, username={}, realName={}, status={}", 
                current, size, username, realName, status);
        
        Page<User> page = new Page<>(current, size);
        IPage<User> userPage = userDomainService.findUserPage(page, username, realName, status);
        
        // 转换为VO
        IPage<UserVO> userVOPage = userPage.convert(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        });
        
        return Result.success(userVOPage);
    }

    /**
     * 根据ID查询用户详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询用户详情", description = "根据用户ID查询用户详情")
    @PreAuthorize("hasAuthority('user:detail')")
    public Result<UserVO> getUserById(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        
        log.info("查询用户详情，用户ID：{}", id);
        
        User user = userDomainService.findById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        return Result.success(userVO);
    }

    /**
     * 创建用户
     */
    @PostMapping
    @Operation(summary = "创建用户", description = "创建新用户")
    @PreAuthorize("hasAuthority('user:create')")
    public Result<UserVO> createUser(
            @Parameter(description = "用户信息") @RequestBody @Valid User user) {
        
        log.info("创建用户，用户信息：{}", user);
        
        User createdUser = userDomainService.createUser(user);
        
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(createdUser, userVO);
        
        return Result.success(userVO);
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新用户", description = "更新用户信息")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<UserVO> updateUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @Parameter(description = "用户信息") @RequestBody @Valid User user) {
        
        log.info("更新用户，用户ID：{}，用户信息：{}", id, user);
        
        user.setId(id);
        User updatedUser = userDomainService.updateUser(user);
        
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(updatedUser, userVO);
        
        return Result.success(userVO);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    @PreAuthorize("hasAuthority('user:delete')")
    public Result<Void> deleteUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        
        log.info("删除用户，用户ID：{}", id);
        
        boolean success = userDomainService.deleteUser(id);
        if (!success) {
            return Result.error("删除用户失败");
        }
        
        return Result.success();
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除用户", description = "批量删除用户")
    @PreAuthorize("hasAuthority('user:delete')")
    public Result<Integer> deleteUsers(
            @Parameter(description = "用户ID列表") @RequestBody @Valid List<Long> userIds) {
        
        log.info("批量删除用户，用户ID列表：{}", userIds);
        
        int count = userDomainService.deleteUsers(userIds);
        
        return Result.success(count);
    }

    /**
     * 激活用户
     */
    @PutMapping("/{id}/activate")
    @Operation(summary = "激活用户", description = "激活指定用户")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Void> activateUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        
        log.info("激活用户，用户ID：{}", id);
        
        boolean success = userDomainService.activateUser(id);
        if (!success) {
            return Result.error("激活用户失败");
        }
        
        return Result.success();
    }

    /**
     * 禁用用户
     */
    @PutMapping("/{id}/deactivate")
    @Operation(summary = "禁用用户", description = "禁用指定用户")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Void> deactivateUser(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id) {
        
        log.info("禁用用户，用户ID：{}", id);
        
        boolean success = userDomainService.deactivateUser(id);
        if (!success) {
            return Result.error("禁用用户失败");
        }
        
        return Result.success();
    }

    /**
     * 批量激活用户
     */
    @PutMapping("/batch/activate")
    @Operation(summary = "批量激活用户", description = "批量激活用户")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Integer> activateUsers(
            @Parameter(description = "用户ID列表") @RequestBody @Valid List<Long> userIds) {
        
        log.info("批量激活用户，用户ID列表：{}", userIds);
        
        int count = userDomainService.activateUsers(userIds);
        
        return Result.success(count);
    }

    /**
     * 批量禁用用户
     */
    @PutMapping("/batch/deactivate")
    @Operation(summary = "批量禁用用户", description = "批量禁用用户")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Integer> deactivateUsers(
            @Parameter(description = "用户ID列表") @RequestBody @Valid List<Long> userIds) {
        
        log.info("批量禁用用户，用户ID列表：{}", userIds);
        
        int count = userDomainService.deactivateUsers(userIds);
        
        return Result.success(count);
    }

    /**
     * 分配角色
     */
    @PutMapping("/{id}/roles")
    @Operation(summary = "分配角色", description = "为用户分配角色")
    @PreAuthorize("hasAuthority('user:assign')")
    public Result<Void> assignRoles(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @Parameter(description = "角色ID列表") @RequestBody @Valid List<Long> roleIds) {
        
        log.info("分配角色，用户ID：{}，角色ID列表：{}", id, roleIds);
        
        boolean success = userDomainService.assignRoles(id, roleIds);
        if (!success) {
            return Result.error("分配角色失败");
        }
        
        return Result.success();
    }

    /**
     * 移除角色
     */
    @DeleteMapping("/{id}/roles")
    @Operation(summary = "移除角色", description = "移除用户的角色")
    @PreAuthorize("hasAuthority('user:assign')")
    public Result<Void> removeRoles(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @Parameter(description = "角色ID列表") @RequestBody @Valid List<Long> roleIds) {
        
        log.info("移除角色，用户ID：{}，角色ID列表：{}", id, roleIds);
        
        boolean success = userDomainService.removeRoles(id, roleIds);
        if (!success) {
            return Result.error("移除角色失败");
        }
        
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/{id}/password")
    @Operation(summary = "修改密码", description = "修改用户密码")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Void> changePassword(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @Parameter(description = "旧密码") @RequestParam @NotBlank String oldPassword,
            @Parameter(description = "新密码") @RequestParam @NotBlank String newPassword) {
        
        log.info("修改密码，用户ID：{}", id);
        
        boolean success = userDomainService.changePassword(id, oldPassword, newPassword);
        if (!success) {
            return Result.error("修改密码失败");
        }
        
        return Result.success();
    }

    /**
     * 重置密码
     */
    @PutMapping("/{id}/password/reset")
    @Operation(summary = "重置密码", description = "重置用户密码")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Void> resetPassword(
            @Parameter(description = "用户ID") @PathVariable @NotNull Long id,
            @Parameter(description = "新密码") @RequestParam @NotBlank String newPassword) {
        
        log.info("重置密码，用户ID：{}", id);
        
        boolean success = userDomainService.resetPassword(id, newPassword);
        if (!success) {
            return Result.error("重置密码失败");
        }
        
        return Result.success();
    }
} 