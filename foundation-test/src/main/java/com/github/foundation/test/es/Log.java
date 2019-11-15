package com.github.foundation.test.es;

import com.github.foundation.es.annotations.Document;
import com.github.foundation.es.annotations.Id;
import lombok.Data;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/12 11:28
 */
@Document(indexName = "log-2019.11.15-jsd-mgmt")
@Data
public class Log {

    @Id
    private String id;

    private Fields fields;

    private String message;
}
