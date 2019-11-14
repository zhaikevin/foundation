package com.github.foundation.es.domain;

import org.elasticsearch.search.sort.SortBuilder;

import java.util.List;


/**
 *  * @Description: 分页信息的抽象接口
 */
public interface Pageable {

    /**
     * Returns a {@link Pageable} instance representing no pagination setup.
     * @return
     */
    static Pageable unpaged() {
        return Unpaged.INSTANCE;
    }

    /**
     * Returns whether the current {@link Pageable} contains pagination information.
     * @return
     */
    default boolean isPaged() {
        return true;
    }

    /**
     * Returns whether the current {@link Pageable} does not contain pagination information.
     * @return
     */
    default boolean isUnpaged() {
        return !isPaged();
    }

    /**
     * Returns the page to be returned.
     * @return the page to be returned.
     */
    int getPageNumber();

    /**
     * Returns the number of items to be returned.
     * @return the number of items of that page
     */
    int getPageSize();

    /**
     * Returns the offset to be taken according to the underlying page and page size.
     * @return the offset to be taken
     */
    int getOffset();

    /**
     * get sort list
     * @return
     * @Description: get sort list
     */
    List<SortBuilder<?>> getSortList();

    /**
     * set sort list
     * @param sortList
     * @Description: set sort list
     */
    void setSortList(List<SortBuilder<?>> sortList);
}

