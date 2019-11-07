package com.github.foundation.redis;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/5 16:33
 */
@Data
public class User implements Serializable {

    private Long id;

    private String name;

    private int sex;
}
