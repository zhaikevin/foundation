package com.github.foundation.es.domain;

import org.elasticsearch.search.sort.SortBuilder;

import java.util.List;

public enum Unpaged implements Pageable {

    INSTANCE;

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#isPaged()
     */
    @Override
    public boolean isPaged() {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#getPageSize()
     */
    @Override
    public int getPageSize() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#getPageNumber()
     */
    @Override
    public int getPageNumber() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#getOffset()
     */
    @Override
    public int getOffset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<SortBuilder<?>> getSortList() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSortList(List<SortBuilder<?>> sortList) {
        throw new UnsupportedOperationException();
    }

}
