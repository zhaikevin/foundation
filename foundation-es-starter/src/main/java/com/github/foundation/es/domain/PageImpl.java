package com.github.foundation.es.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 *  * @Description: 分页接口的基本实现
 */
public class PageImpl<T> implements Page<T>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4321097703045880453L;

    /**
     * 元素的全部数量
     */
    private final long total;

    /**
     * 数据内容
     */
    private final List<T> content = new ArrayList<T>();


    /**
     * 分页接口
     */
    private final Pageable pageable;

    /**
     * Constructor of {@code PageImpl}.
     * @param content  the content of this page, must not be {@literal null}.
     * @param pageable the paging information, can be {@literal null}.
     * @param total    the total amount of items available.
     */
    public PageImpl(List<T> content, Pageable pageable, long total) {
        this.pageable = pageable;
        this.total = !content.isEmpty() && pageable != null && pageable.getOffset() + pageable.getPageSize() > total
                ? pageable.getOffset() + content.size() : total;
        this.content.addAll(content);
    }

    /**
     * Creates a new {@link PageImpl} with the given content. This will result in the created {@link Page} being identical
     * to the entire {@link List}.
     * @param content must not be {@literal null}.
     */
    public PageImpl(List<T> content) {
        this(content, null, null == content ? 0 : content.size());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Page#getTotalPages()
     */
    @Override
    public int getTotalPages() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Page#getTotalElements()
     */
    @Override
    public long getTotalElements() {
        return total;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#getNumber()
     */
    public int getNumber() {
        return pageable == null ? 0 : pageable.getPageNumber();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#getSize()
     */
    public int getSize() {
        return pageable == null ? 0 : pageable.getPageSize();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#getNumberOfElements()
     */
    public int getNumberOfElements() {
        return content.size();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#getContent()
     */
    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }


    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        String contentType = "UNKNOWN";
        List<T> content = getContent();

        if (content.size() > 0) {
            contentType = content.get(0).getClass().getName();
        }

        return String.format("Page %s of %d containing %s instances", getNumber() + 1, getTotalPages(), contentType);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PageImpl<?>)) {
            return false;
        }

        PageImpl<?> that = (PageImpl<?>) obj;

        return this.total == that.total && super.equals(obj);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int result = 17;

        result += 31 * (int) (total ^ total >>> 32);
        result += 31 * super.hashCode();

        return result;
    }

}
