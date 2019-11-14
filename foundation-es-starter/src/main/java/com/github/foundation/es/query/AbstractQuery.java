package com.github.foundation.es.query;

import com.github.foundation.es.domain.Pageable;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.addAll;

public abstract class AbstractQuery implements Query {

    // protected Pageable pageable = DEFAULT_PAGE;

    protected Pageable pageable;
    protected List<String> types = new ArrayList<>();
    protected List<String> indices = new ArrayList<>();
    protected List<String> fields = new ArrayList<>();
    protected SourceFilter sourceFilter;
    protected float minScore;
    protected Collection<String> ids;
    protected String route;
    protected SearchType searchType = SearchType.DFS_QUERY_THEN_FETCH;
    protected IndicesOptions indicesOptions;
    protected boolean trackScores;

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
        // TODO 是否需要操作 pageable
        return (T) this;
    }

    @Override
    public Pageable getPageable() {
        return this.pageable;
    }

    @Override
    public void addFields(String... fields) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<String> getFields() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addSourceFilter(SourceFilter sourceFilter) {
        this.sourceFilter = sourceFilter;
    }

    @Override
    public SourceFilter getSourceFilter() {
        return this.sourceFilter;
    }

    @Override
    public float getMinScore() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean getTrackScores() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Collection<String> getIds() {
        return ids;
    }

    public void setIds(Collection<String> ids) {
        this.ids = ids;
    }

    @Override
    public SearchType getSearchType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IndicesOptions getIndicesOptions() {
        // TODO Auto-generated method stub
        return null;
    }

}
