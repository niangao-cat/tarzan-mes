package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnSingleVO6 implements Serializable {
    private static final long serialVersionUID = -8338592508383595278L;
    @ApiModelProperty(value = "执行作业ID")
    private String eoId;
    @ApiModelProperty("虚拟号ID列表")
    private List<String> virtualIdList;
}
