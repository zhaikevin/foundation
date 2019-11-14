package com.github.foundation.test.es;

import com.github.foundation.es.annotations.Document;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/12 11:28
 */
@Document(indexName = "log-2019.11.10-finance-bank")
@Data
public class Log {

    @Id
    private String id;

    private Fields fields;

    private String message;
}
