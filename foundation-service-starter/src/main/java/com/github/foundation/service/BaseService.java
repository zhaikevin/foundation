package com.github.foundation.service;

import com.github.foundation.datasource.mapper.BaseMapper;
import com.github.foundation.pagination.model.Pagination;

import java.util.List;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/26 14:38
 */
public interface BaseService<T, S extends BaseMapper> {

    /**
     * 分页查询
     * @param pagination
     */
    void queryByPage(Pagination<T> pagination);

    /**
     * 插入操作
     * @param record
     * @return
     */
    void insertUseGeneratedKeys(T record);

    /**
     * 根据主键更新属性不为null的值
     * @param record
     */
    void UpdateByPrimaryKeySelective(T record);

    /**
     * 根据主键删除字段
     * @param id
     */
    void deleteByPrimaryKey(Long id);

    /**
     * 根据主键id查询
     * @param id
     * @return
     */
    T getById(Long id);

    /**
     * 获取所有数据
     * @return
     */
    List<T> getAll();
}
