package com.rjsoft.magina.web.common.exception.base;

import lombok.Data;


/**
 * 参数验证异常结果
 */
@Data
public class ArgumentInvalidResult {
    /**
     * 字段名
     */
    private String field;

    private Object rejectedValue;

    /**
     * 默认消息
     */
    private String defaultMessage;
}
