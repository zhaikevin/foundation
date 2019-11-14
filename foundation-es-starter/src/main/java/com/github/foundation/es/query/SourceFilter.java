package com.github.foundation.es.query;

/**
 *  * @Description: SourceFilter for providing includes and excludes.
 */
public interface SourceFilter {

    String[] getIncludes();

    String[] getExcludes();
}
