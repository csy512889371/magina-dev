package com.rjsoft.magina.spi.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@ApiModel(value = "响应消息类")
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    @ApiModelProperty(value = "响应码", required = true)
    private String code;

    @ApiModelProperty(value = "响应消息", required = true)
    private String message;

    @ApiModelProperty(value = "返回数据")
    private T data;

    private Map<String, T> properties = new HashMap<>();

    public BaseResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static BaseResponse<Object> ofMessage(String code, String message) {
        return new BaseResponse<>(code, message, null);
    }


    public static BaseResponse<Object> ofSuccess(Object data) {
        return new BaseResponse<>(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMessage(), data);
    }

    public static BaseResponse<Object> ofSuccess(Object data, String message) {
        return new BaseResponse<>(ResponseStatusEnum.SUCCESS.getCode(), message, data);
    }

    public static BaseResponse<Object> ofStatus(ResponseStatusEnum status) {
        return new BaseResponse<>(status.getCode(), status.getMessage(), null);
    }

    public BaseResponse addProperties(String key, T value) {
        this.properties.put(key, value);
        return this;
    }
}
