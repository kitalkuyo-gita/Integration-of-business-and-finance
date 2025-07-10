package com.iobaf.domain.user.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iobaf.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户仓储接口 - 用户领域仓储
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Mapper
public interface UserRepository extends BaseMapper<User> {

    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 用户实体
     */
    User findByUsername(@Param("username") String username);

    /**
     * 根据邮箱查找用户
     * 
     * @param email 邮箱
     * @return 用户实体
     */
    User findByEmail(@Param("email") String email);

    /**
     * 根据手机号查找用户
     * 
     * @param phone 手机号
     * @return 用户实体
     */
    User findByPhone(@Param("phone") String phone);

    /**
     * 分页查询用户列表
     * 
     * @param page 分页参数
     * @param username 用户名（模糊查询）
     * @param realName 真实姓名（模糊查询）
     * @param status 状态
     * @return 分页结果
     */
    IPage<User> selectUserPage(Page<User> page, 
                              @Param("username") String username,
                              @Param("realName") String realName,
                              @Param("status") Integer status);

    /**
     * 根据用户ID查询用户及其角色和权限
     * 
     * @param userId 用户ID
     * @return 用户实体（包含角色和权限）
     */
    User findUserWithRolesAndPermissions(@Param("userId") Long userId);

    /**
     * 根据角色编码查询用户列表
     * 
     * @param roleCode 角色编码
     * @return 用户列表
     */
    List<User> findUsersByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @param excludeUserId 排除的用户ID（用于更新时检查）
     * @return 是否存在
     */
    boolean existsByUsername(@Param("username") String username, 
                           @Param("excludeUserId") Long excludeUserId);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @param excludeUserId 排除的用户ID（用于更新时检查）
     * @return 是否存在
     */
    boolean existsByEmail(@Param("email") String email, 
                         @Param("excludeUserId") Long excludeUserId);

    /**
     * 检查手机号是否存在
     * 
     * @param phone 手机号
     * @param excludeUserId 排除的用户ID（用于更新时检查）
     * @return 是否存在
     */
    boolean existsByPhone(@Param("phone") String phone, 
                         @Param("excludeUserId") Long excludeUserId);

    /**
     * 批量删除用户
     * 
     * @param userIds 用户ID列表
     * @return 删除数量
     */
    int deleteBatchByIds(@Param("userIds") List<Long> userIds);

    /**
     * 批量更新用户状态
     * 
     * @param userIds 用户ID列表
     * @param status 状态
     * @return 更新数量
     */
    int updateStatusBatch(@Param("userIds") List<Long> userIds, 
                         @Param("status") Integer status);
} 