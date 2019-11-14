package com.github.foundation.es.domain;

import org.elasticsearch.search.sort.SortBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 *  * @Description: PageRequest
 */
public class PageRequest extends AbstractPageRequest {


    /**
     * Creates a new {@link PageRequest}. Pages are zero indexed, thus providing 0 for {@code page} will return the first
     * page.
     * @param page zero-based page index.
     * @param size the size of the page to be returned.
     */
    public PageRequest(int page, int size) {
        this(page, size, new ArrayList<>());
    }

    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     * @param page zero-based page index.
     * @param size the size of the page to be returned.
     * @param sort can be {@literal null}.
     */
    public PageRequest(int page, int size, List<SortBuilder<?>> sortList) {
        super(page, size);

        this.sortList.addAll(sortList);
    }

    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     * @param page zero-based page index.
     * @param size the size of the page to be returned.
     * @param sort can be {@literal null}.
     */
    public PageRequest(int page, int size, SortBuilder<?> sortBuilder) {
        super(page, size);
        this.sortList.add(sortBuilder);
    }

    /**
     * Creates a new unsorted {@link PageRequest}.
     * @param page zero-based page index.
     * @param size the size of the page to be returned.
     * @since 2.0
     */
    public static PageRequest of(int page, int size) {
        return of(page, size, new ArrayList<>());
    }

    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     * @param page zero-based page index.
     * @param size the size of the page to be returned.
     * @since 2.0
     */
    public static PageRequest of(int page, int size, List<SortBuilder<?>> sortList) {
        return new PageRequest(page, size, sortList);
    }


    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     * @param page zero-based page index.
     * @param size the size of the page to be returned.
     * @since 2.0
     */
    public static PageRequest of(int page, int size, SortBuilder<?> sortBuilder) {
        return new PageRequest(page, size, sortBuilder);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#getSort()
     */
    public List<SortBuilder<?>> getSortList() {
        return sortList;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#next()
     */
    public Pageable next() {
        return new PageRequest(getPageNumber() + 1, getPageSize(), getSortList());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.AbstractPageRequest#previous()
     */
    public PageRequest previous() {
        return getPageNumber() == 0 ? this : new PageRequest(getPageNumber() - 1, getPageSize(), getSortList());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#first()
     */
    public Pageable first() {
        return new PageRequest(0, getPageSize(), getSortList());
    }


}
