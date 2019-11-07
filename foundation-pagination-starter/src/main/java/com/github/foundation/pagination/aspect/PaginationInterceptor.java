package com.github.foundation.pagination.aspect;

import com.github.foundation.pagination.model.Order;
import com.github.foundation.pagination.model.Pagination;
import com.github.foundation.pagination.model.Sort;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * 分页拦截器.
 */
@Component
@Aspect
@Slf4j
public class PaginationInterceptor {

    @SuppressWarnings("rawtypes")
    @Pointcut("args(..,pagination) && @annotation(com.github.foundation.pagination.annotation.Pageable)")
    public void aspect(Pagination pagination) {

    }

    /**
     * 在调用分页的service method之前对分页信息进行设置.
     * 避免了每个分页方法都进行一次分页处理.
     * @param joinPoint
     * @param pagination 分页信息
     */
    @Before("aspect(pagination)")
    @SuppressWarnings("rawtypes")
    public void before(JoinPoint joinPoint, Pagination pagination) {
        log.debug("the target method {} is pagination, do Pagination config.", joinPoint.getSignature());
        Preconditions.checkNotNull(pagination);

        PageHelper.startPage(pagination.getPage(), pagination.getPageSize());
        Sort sort = pagination.getSort();
        if (sort != null) {
            Iterator<Order> it = sort.iterator();
            StringBuilder sb = new StringBuilder();
            while (it.hasNext()) {
                Order order = it.next();
                // 将java命名格式转换为数据库的下划线命名格式
                String underscoreName = underscoreName(order.getProperty());
                sb.append(underscoreName + " " + order.getDirection().name());
                if (it.hasNext()) {
                    sb.append(",");
                }
            }
            PageHelper.orderBy(sb.toString());
        }
    }

    private static String underscoreName(String name) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(name.substring(0, 1).toLowerCase());
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            String slc = s.toLowerCase();
            if (!s.equals(slc)) {
                result.append("_").append(slc);
            } else {
                result.append(s);
            }
        }
        return result.toString();
    }
}
