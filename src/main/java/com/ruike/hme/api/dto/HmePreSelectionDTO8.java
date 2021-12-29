package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmePreSelectionDTO8
 *
 * @author: chaonan.hu@hand-china.com 2021/6/21 10:14:41
 **/
@Data
public class HmePreSelectionDTO8 implements Serializable {
    private static final long serialVersionUID = 6111247543755549427L;

    @ApiModelProperty(value = "计算类型")
    private String countType;

    @ApiModelProperty(value = "电流")
    private String current;

    @ApiModelProperty(value = "采集项")
    private String collectionItem;

    @ApiModelProperty(value = "范围类型及值的集合")
    private List<HmePreSelectionDTO9> hmePreSelectionDTO9List;
}
