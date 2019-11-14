package com.github.foundation.es.domain;

import java.util.Collections;
import java.util.List;

/**
 *  * @Description: 对象列表上的子集, 可以从整个列表中获取对应的位置信息
 * @param <T>
 */
public interface Page<T> {

    /**
     * Creates a new empty {@link Page}.
     * @return
     * @since 2.0
     */
    static <T> Page<T> empty() {
        return empty(Pageable.unpaged());
    }

    /**
     * Creates a new empty {@link Page} for the given {@link Pageable}.
     * @param pageable must not be {@literal null}.
     * @return
     * @since 2.0
     */
    static <T> Page<T> empty(Pageable pageable) {
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    /**
     * Returns the number of total pages.
     * @return the number of total pages
     */
    int getTotalPages();

    /**
     * Returns the total amount of elements.
     * @return the total amount of elements
     */
    long getTotalElements();

    /**
     * @return
     * @Description: 获取数据
     */
    List<T> getContent();

}
