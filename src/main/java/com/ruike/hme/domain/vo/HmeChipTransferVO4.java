package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/8/18 10:17
 */
@Data
public class HmeChipTransferVO4 implements Serializable {

    private static final long serialVersionUID = -7373814645029516763L;

    @ApiModelProperty("站点ID")
    private String siteId;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("工艺路线ID")
    private String operationId;

    @ApiModelProperty("来源物料id")
    private String transMaterialLotId;

    @ApiModelProperty("取片规则")
    private String loadingRules;

    @ApiModelProperty("装载规则")
    private String loadRule;

    @ApiModelProperty(value = "目标条码")
    private List<String> materialLotCodeList;
}
