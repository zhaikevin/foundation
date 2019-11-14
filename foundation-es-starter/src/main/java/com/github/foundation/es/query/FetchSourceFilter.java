package com.github.foundation.es.query;

/**
 *  * @Description: FetchSourceFilter
 */
public class FetchSourceFilter implements SourceFilter {

    private final String[] includes;
    private final String[] excludes;

    public FetchSourceFilter(final String[] includes, final String[] excludes) {
        this.includes = includes;
        this.excludes = excludes;
    }

    @Override
    public String[] getIncludes() {
        return includes;
    }

    @Override
    public String[] getExcludes() {
        return excludes;
    }

}
