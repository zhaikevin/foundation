package com.github.foundation.pagination.model;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Description: 查询参数
 */
public class SearchParams {

    public static class Param {

        /**
         * 查询字段名称
         */
        private String name;

        /**
         * 值
         */
        private Object value;

        /**
         * 比较关系
         */
        private Compare compare;

        public Param(String name, Object value, Compare compare) {
            this.name = name;
            this.value = value;
            this.compare = compare;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Compare getCompare() {
            return compare;
        }

        public void setCompare(Compare compare) {
            this.compare = compare;
        }
    }

    public enum Compare {
        EQUAL, LIKE, NOT_EQUAL, IN
    }

    private List<Param> params = new ArrayList<>();

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public void addParam(Param param) {
        params.add(param);
    }

    @Override
    public String toString() {
        if (CollectionUtils.isEmpty(params)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<?> it = params.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
