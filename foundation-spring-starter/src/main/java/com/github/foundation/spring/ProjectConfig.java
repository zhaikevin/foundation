package com.github.foundation.spring;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/6 17:19
 */
@Component
@Data
@ConfigurationProperties(prefix = "foundation.project")
public class ProjectConfig {

    private String name;

    private String env;
}
