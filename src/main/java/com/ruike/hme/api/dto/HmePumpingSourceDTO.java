package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 泵浦源性能数据展示查询条件
 *
 * @author wengang.qiang@hand-china.com 2021/09/01 12:02
 */
@Data
public class HmePumpingSourceDTO implements Serializable {

    private static final long serialVersionUID = 8212243967019296138L;

    @ApiModelProperty("标识说明")
    private String identification;

}
