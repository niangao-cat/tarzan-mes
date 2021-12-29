package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 工单派工
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */

@Data
public class HmeModAreaVO implements Serializable {

    private static final long serialVersionUID = -2451409199928730165L;

    @ApiModelProperty(value = "车间ID")
    private String areaId;
    @ApiModelProperty(value = "车间编码")
    private String areaCode;
    @ApiModelProperty(value = "车间名称")
    private String areaName;

    private List<String> prodLineIds;

    private List<HmeWoDispatchVO> woProdList;

}
