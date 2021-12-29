package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工单派工
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */

@Data
public class HmeModAreaVO2 implements Serializable {

    private static final long serialVersionUID = -8470445357084348826L;

    @ApiModelProperty(value = "车间ID")
    private String areaId;
    @ApiModelProperty(value = "车间编码")
    private String areaCode;
    @ApiModelProperty(value = "车间名称")
    private String areaName;

}
