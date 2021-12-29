package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeWipStocktakeDocDTO6
 *
 * @author: chaonan.hu@hand-china.com 2021-03-04 16:31:23
 **/
@Data
public class HmeWipStocktakeDocDTO6 implements Serializable {
    private static final long serialVersionUID = -4742467955652674509L;

    @ApiModelProperty(value = "站点ID", required = true)
    private String siteId;

    @ApiModelProperty(value = "部门ID", required = true)
    private String areaId;

    @ApiModelProperty(value = "是否明盘标识", required = true)
    private String openFlag;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "物料ID集合")
    private List<String> materialIdList;

    @ApiModelProperty(value = "产线ID集合")
    private List<String> prodLineIdList;

    @ApiModelProperty(value = "工序ID集合")
    private List<String> workcellIdList;
}
