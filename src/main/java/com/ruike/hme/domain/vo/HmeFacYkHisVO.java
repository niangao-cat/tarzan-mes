package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/7 10:44
 */
@Data
public class HmeFacYkHisVO implements Serializable {

    private static final long serialVersionUID = -9437193698407758L;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "芯片类型")
    private String cosType;

    @ApiModelProperty(value = "FAC物料编码")
    private String facMaterialCode;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "标准值")
    private BigDecimal standardValue;

    @ApiModelProperty(value = "允差")
    private BigDecimal allowDiffer;

    @ApiModelProperty(value = "变更人")
    private String lastUpdateByName;

    @ApiModelProperty(value = "变更时间")
    private Date lastUpdateDate;
}
