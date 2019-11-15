package com.github.foundation.es.query;

import com.github.foundation.es.domain.Pageable;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.addAll;

public abstract class AbstractQuery implements Query {


    protected Pageable pageable;
    protected List<String> types = new ArrayList<>();
    protected List<String> indices = new ArrayList<>();
    protected List<String> fields = new ArrayList<>();
    protected SourceFilter sourceFilter;

    @Override
    public List<String> getIndices() {
        return indices;
    }

    @Override
    public void addIndices(String... indices) {
        addAll(this.indices, indices);
    }

    @Override
    public void addTypes(String... types) {
        addAll(this.types, types);
    }

    @Override
    public List<String> getTypes() {
        return types;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Query> T setPageable(Pageable pageable) {
        this.pageable = pageable;
        return (T) this;
    }

    @Override
    public Pageable getPageable() {
        return this.pageable;
    }

    @Override
    public void addFields(String... fields) {
        addAll(this.fields, fields);
    }

    @Override
    public List<String> getFields() {
        return this.fields;
    }

    @Override
    public void addSourceFilter(SourceFilter sourceFilter) {
        this.sourceFilter = sourceFilter;
    }

    @Override
    public SourceFilter getSourceFilter() {
        return this.sourceFilter;
    }


}
