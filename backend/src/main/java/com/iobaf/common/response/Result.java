package com.iobaf.common.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 统一响应格式
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@Data
public class Result<T> {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间
     */
    private LocalDateTime timestamp;

    /**
     * 成功响应
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功响应
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        result.setTimestamp(LocalDateTime.now());
        return result;
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setTimestamp(LocalDateTime.now());
        return result;
    }

    /**
     * 自定义响应
     */
    public static <T> Result<T> of(Integer code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        result.setTimestamp(LocalDateTime.now());
        return result;
    }
} 