package com.github.foundation.pagination.model;

/**
 * 排序方向的枚举类。
 */
public enum Direction {
    ASC, DESC;

    /**
     * 根据给定的${@link String} 值返回{@link Direction} enum。
     * @param value
     * @return
     */
    public static Direction fromString(String value) {
        try {
            return Direction.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format(
                            "Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).",
                            value), e);
        }
    }
}