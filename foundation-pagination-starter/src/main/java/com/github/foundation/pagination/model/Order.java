package com.github.foundation.pagination.model;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Order item，记录排序方向和字段。
 */
public class Order implements Serializable {
    private static final long serialVersionUID = -9216310755141420727L;

    public static final Direction DEFAULT_DIRECTION = Direction.ASC;

    private final Direction direction;
    private final String property;

    /**
     * 创建新 {@link Order} 实例. 如果参数direction 为{@literal null} 则采用默认排序方向{@link Order#DEFAULT_DIRECTION}
     * @param direction 排序方向，可以为${@literal null}, 若为{@literal null} 则采用默认排序方向{@link Order#DEFAULT_DIRECTION}
     * @param property  排序字段，不能为{@literal null} or empty.
     */
    public Order(Direction direction, String property) {
        if (!StringUtils.isNotBlank(property)) {
            throw new IllegalArgumentException("Property must not null or empty!");
        }

        this.direction = direction == null ? DEFAULT_DIRECTION : direction;
        this.property = property;
    }

    /**
     * 依据给定的字段创建新的${@link Order}实例. Direction默认为{@link Sort#DEFAULT_DIRECTION}.
     * @param property 排序字段不能为{@literal null} or empty.
     */
    public Order(String property) {
        this(DEFAULT_DIRECTION, property);
    }

    /**
     * Returns the order the property shall be sorted for.
     * @return
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Returns the property to order for.
     * @return
     */
    public String getProperty() {
        return property;
    }

    /**
     * Returns whether sorting for this property shall be ascending.
     * @return
     */
    public boolean isAscending() {
        return this.direction.equals(Direction.ASC);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((direction == null) ? 0 : direction.hashCode());
        result = prime * result + ((property == null) ? 0 : property.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Order other = (Order) obj;
        if (direction != other.direction) {
            return false;
        }
        if (property == null) {
            if (other.property != null) {
                return false;
            }
        } else if (!property.equals(other.property)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.property + " " + this.direction.name();
    }

}
