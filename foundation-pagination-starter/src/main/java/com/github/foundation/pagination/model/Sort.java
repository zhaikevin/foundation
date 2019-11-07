package com.github.foundation.pagination.model;

import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 排序数据模型
 **/
public class Sort implements Iterable<Order>, Serializable {
    private static final long serialVersionUID = -2476277461097853426L;

    public static final Direction DEFAULT_DIRECTION = Direction.ASC;

    private final List<Order> orders;

    /**
     * 用给定的orders创建新的排序对象.
     * @param orders 不能为{@literal null}.
     */
    public Sort(Order... orders) {
        this(Arrays.asList(orders));
    }

    /**
     * 创建新的Sort实例
     * @param orders 不能为空和null。
     */
    public Sort(List<Order> orders) {
        if (null == orders || orders.isEmpty()) {
            throw new IllegalArgumentException("You have to provide at least one sort property to sort by!");
        }

        this.orders = orders;
    }

    /**
     * 用默认排序顺序和给定的排序字段创建新的{@Sort}实例。
     * @param properties 不能为{@literal null} 或者包含{@literal null}以及空字符串。
     */
    public Sort(String... properties) {
        this(DEFAULT_DIRECTION, properties);
    }

    /**
     * 创建新 {@link Sort} 实例.
     * @param direction  如果指定为{@literal null} 则采用默认值 {@link Sort#DEFAULT_DIRECTION}
     * @param properties 不能为{@literal null} 或者包含{@literal null}以及空字符串。
     */
    public Sort(Direction direction, String... properties) {
        this(direction, properties == null ? new ArrayList<String>() : Arrays.asList(properties));
    }

    /**
     * 创建新的${link Sort}实例
     * @param sort
     * @return
     */
    public Sort and(Sort sort) {
        if (sort == null) {
            return this;
        }

        ArrayList<Order> these = new ArrayList<Order>(this.orders);
        for (Order order : sort) {
            these.add(order);
        }

        return new Sort(these);
    }

    /**
     * 创建新的 {@link Sort} 实例。
     * @param direction  排序方向
     * @param properties 排序字段。字段集合要求不能为${@literal null}或者为empty。
     */
    public Sort(Direction direction, List<String> properties) {
        if (properties == null || properties.isEmpty()) {
            throw new IllegalArgumentException("You have to provide at least one property to sort by!");
        }

        this.orders = new ArrayList<Order>(properties.size());

        for (String property : properties) {
            this.orders.add(new Order(direction, property));
        }
    }

    /**
     * 返回注册的order集合中指定的字段的信息。
     * @param property 字段名称。
     * @return 返回注册的order集合中指定的字段的Order信息
     */
    public Order getOrderFor(String property) {
        for (Order order : this) {
            if (order.getProperty().equals(property)) {
                return order;
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<Order> iterator() {
        return this.orders.iterator();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (CollectionUtils.isEmpty(orders)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<?> it = orders.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
