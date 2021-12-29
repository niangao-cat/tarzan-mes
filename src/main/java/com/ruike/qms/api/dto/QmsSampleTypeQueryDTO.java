package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 抽样类型管理查询参数
 * @author: han.zhang
 * @create: 2020/05/06 10:22
 */
@Getter
@Setter
@ToString
public class QmsSampleTypeQueryDTO implements Serializable {
    private static final long serialVersionUID = -1463660168251350381L;

    @ApiModelProperty(value = "抽样类型编码")
    private String sampleTypeCode;

    @ApiModelProperty(value = "抽样类型")
    private String sampleType;
}