package com.rjsoft.magina.spi.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("分页数据")
public class PageFeign<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<T> content = new ArrayList<>();

    @ApiModelProperty(value = "是否最后一页")
    private boolean last;

    private int totalPages;


    @ApiModelProperty(value = "总条数")
    private int totalElements;


    @ApiModelProperty(value = "当前页的条数")
    private int numberOfElements;

    @ApiModelProperty(value = "每页显示条数")
    private int size;

    @ApiModelProperty(value = "当前页码")
    private int number;

}
