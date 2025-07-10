package com.iobaf.domain.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.user.entity.User;

import java.util.List;

/**
 * 用户领域服务接口 - 用户领域业务逻辑
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
public interface UserDomainService {

    /**
     * 用户注册
     * 
     * @param user 用户实体
     * @return 注册后的用户
     */
    User register(User user);

    /**
     * 用户登录
     * 
     * @param username 用户名
     * @param password 密码
     * @return 登录后的用户信息
     */
    User login(String username, String password);

    /**
     * 用户登出
     * 
     * @param userId 用户ID
     */
    void logout(Long userId);

    /**
     * 修改密码
     * 
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 重置密码
     * 
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 是否重置成功
     */
    boolean resetPassword(Long userId, String newPassword);

    /**
     * 激活用户
     * 
     * @param userId 用户ID
     * @return 是否激活成功
     */
    boolean activateUser(Long userId);

    /**
     * 禁用用户
     * 
     * @param userId 用户ID
     * @return 是否禁用成功
     */
    boolean deactivateUser(Long userId);

    /**
     * 分配角色
     * 
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否分配成功
     */
    boolean assignRoles(Long userId, List<Long> roleIds);

    /**
     * 移除角色
     * 
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否移除成功
     */
    boolean removeRoles(Long userId, List<Long> roleIds);

    /**
     * 根据ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户实体
     */
    User findById(Long userId);

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户实体
     */
    User findByUsername(String username);

    /**
     * 分页查询用户列表
     * 
     * @param page 分页参数
     * @param username 用户名（模糊查询）
     * @param realName 真实姓名（模糊查询）
     * @param status 状态
     * @return 分页结果
     */
    IPage<User> findUserPage(Page<User> page, String username, String realName, Integer status);

    /**
     * 创建用户
     * 
     * @param user 用户实体
     * @return 创建后的用户
     */
    User createUser(User user);

    /**
     * 更新用户
     * 
     * @param user 用户实体
     * @return 更新后的用户
     */
    User updateUser(User user);

    /**
     * 删除用户
     * 
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteUser(Long userId);

    /**
     * 批量删除用户
     * 
     * @param userIds 用户ID列表
     * @return 删除数量
     */
    int deleteUsers(List<Long> userIds);

    /**
     * 批量激活用户
     * 
     * @param userIds 用户ID列表
     * @return 激活数量
     */
    int activateUsers(List<Long> userIds);

    /**
     * 批量禁用用户
     * 
     * @param userIds 用户ID列表
     * @return 禁用数量
     */
    int deactivateUsers(List<Long> userIds);

    /**
     * 检查用户是否有指定权限
     * 
     * @param userId 用户ID
     * @param permissionCode 权限编码
     * @return 是否有权限
     */
    boolean hasPermission(Long userId, String permissionCode);

    /**
     * 检查用户是否有指定角色
     * 
     * @param userId 用户ID
     * @param roleCode 角色编码
     * @return 是否有角色
     */
    boolean hasRole(Long userId, String roleCode);
} 