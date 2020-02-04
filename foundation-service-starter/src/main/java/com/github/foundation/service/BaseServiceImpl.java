package com.github.foundation.service;

import com.github.foundation.common.exception.DataAccessException;
import com.github.foundation.datasource.mapper.BaseMapper;
import com.github.foundation.pagination.model.Order;
import com.github.foundation.pagination.model.Pagination;
import com.github.foundation.pagination.model.SearchParams;
import com.github.foundation.pagination.model.Sort;
import com.github.foundation.service.annotations.ExcludeByPage;
import com.github.foundation.spring.SpringContextUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/26 16:27
 */
@Slf4j
public class BaseServiceImpl<T, S extends BaseMapper> implements BaseService<T, S> {

    /**
     * 实体类
     */
    private Class<T> entityClass;

    /**
     * mapper类
     */
    private Class<S> mapperClass;

    /**
     * mapper
     */
    private S mapper;

    /**
     * 实例字段和数据库字段对应关系
     */
    private Map<String, String> entityFieldMap;

    @Override
    public void queryByPage(Pagination<T> pagination) {
        startPage(pagination);
        List<T> list = searchByExample(pagination);
        pagination.setDataset(list);
        pagination.setTotal(((Page<T>) list).getTotal());
    }

    /**
     * 根据搜索字段进行查询
     * @param pagination
     * @return
     */
    private List<T> searchByExample(Pagination<T> pagination) {
        Example example = new Example(getEntityClass());
        getSearchParams(example, pagination);
        excludeProperties(example);
        return getMapper().selectByExample(example);
    }

    /**
     * 组装查询参数
     * @param example
     * @param pagination
     */
    private void getSearchParams(Example example, Pagination<T> pagination) {
        SearchParams searchParams = pagination.getParams();
        if (searchParams == null) {
            return;
        }
        List<SearchParams.Param> params = searchParams.getParams();
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        Example.Criteria criteria = example.createCriteria();
        for (SearchParams.Param param : params) {
            //String name = getFieldDbName(param.getName());
            String name = param.getName();
            if (param.getCompare().equals(SearchParams.Compare.EQUAL)) {
                criteria.andEqualTo(name, param.getValue());
            } else if (param.getCompare().equals(SearchParams.Compare.LIKE)) {
                criteria.andLike(name, "%" + param.getValue().toString() + "%");
            } else if (param.getCompare().equals(SearchParams.Compare.NOT_EQUAL)) {
                criteria.andNotEqualTo(name, param.getValue());
            } else if (param.getCompare().equals(SearchParams.Compare.IN)) {
                criteria.andIn(name, (Iterable) param.getValue());
            }
        }
    }

    /**
     * 查询时排除字段
     * @param example
     */
    private void excludeProperties(Example example) {
        for (Field field : getEntityClass().getDeclaredFields()) {
            ExcludeByPage excludeByPage = field.getAnnotation(ExcludeByPage.class);
            if (excludeByPage != null) {
                example.excludeProperties(field.getName());
            }
        }
    }

    /**
     * 开始分页
     * @param pagination
     */
    private void startPage(Pagination<T> pagination) {
        PageHelper.startPage(pagination.getPage(), pagination.getPageSize());
        Sort sort = pagination.getSort();
        if (sort != null) {
            Iterator<Order> it = sort.iterator();
            StringBuilder sb = new StringBuilder();
            while (it.hasNext()) {
                Order order = it.next();
                sb.append(getFieldDbName(order.getProperty()) + " " + order.getDirection().name());
                if (it.hasNext()) {
                    sb.append(",");
                }
            }
            PageHelper.orderBy(sb.toString());
        }
    }

    @Override
    public void insertUseGeneratedKeys(T record) {
        getMapper().insertUseGeneratedKeys(record);
    }

    @Override
    public void UpdateByPrimaryKeySelective(T record) {
        getMapper().updateByPrimaryKeySelective(record);
    }

    @Override
    public void deleteByPrimaryKey(Long id) {
        getMapper().deleteByPrimaryKey(id);
    }

    @Override
    public T getById(Long id) {
        return (T) getMapper().selectByPrimaryKey(id);
    }

    @Override
    public List<T> getAll() {
        return getMapper().selectAll();
    }

    /**
     * 获取实体类
     * @return
     */
    protected Class<T> getEntityClass() {
        if (entityClass == null) {
            try {
                ParameterizedType parameterizedType = resolveReturnedClassFromGenericType(getClass());
                entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
            } catch (Exception e) {
                log.error("Unable to resolve entityClass:{}", e.getMessage(), e);
                throw new DataAccessException("Unable to resolve entityClass", e);
            }
        }
        return entityClass;
    }

    /**
     * 获取mapper类
     * @return
     */
    protected Class<S> getMapperClass() {
        if (mapperClass == null) {
            try {
                ParameterizedType parameterizedType = resolveReturnedClassFromGenericType(getClass());
                mapperClass = (Class<S>) parameterizedType.getActualTypeArguments()[1];
            } catch (Exception e) {
                log.error("Unable to resolve mapperClass:{}", e.getMessage(), e);
                throw new DataAccessException("Unable to resolve mapperClass", e);
            }
        }
        return mapperClass;
    }

    private ParameterizedType resolveReturnedClassFromGenericType(Class<?> clazz) {
        Object genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            return parameterizedType;
        }
        return resolveReturnedClassFromGenericType(clazz.getSuperclass());
    }

    protected S getMapper() {
        if (mapper == null) {
            mapper = SpringContextUtils.getBean(getMapperClass());
        }
        return mapper;
    }

    /**
     * 获取字段对应的数据库中德字段名称
     * @param fieldName
     * @return
     */
    private String getFieldDbName(String fieldName) {
        String dbName = getEntityFieldMap().get(fieldName);
        if (dbName != null) {
            return dbName;
        }
        return fieldName;
    }

    protected Map<String, String> getEntityFieldMap() {
        if (entityFieldMap == null) {
            entityFieldMap = new HashMap<>();
            Field[] fields = getEntityClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(Transient.class) != null) {
                    continue;
                }
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    entityFieldMap.put(field.getName(), column.name());
                } else {
                    entityFieldMap.put(field.getName(), field.getName());
                }
            }
        }
        return entityFieldMap;
    }
}
