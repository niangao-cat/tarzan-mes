package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmePumpPreSelectionVO4
 *
 * @author: chaonan.hu@hand-china.com 2021/08/30 14:19:34
 **/
@Data
public class HmePumpPreSelectionVO4 implements Serializable {
    private static final long serialVersionUID = -3504980598359489982L;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @ApiModelProperty(value = "条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "条码号")
    private String materialLotCode;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "质量状态")
    private String qualityStatus;

    @ApiModelProperty(value = "有效性")
    private String enableFlag;

    @ApiModelProperty(value = "货位")
    private String locatorId;

    @ApiModelProperty(value = "容器ID")
    private String containerId;

    @ApiModelProperty(value = "组号,有值代表已筛选,相等代表筛选后为一组,无值代表未筛选")
    private Long groupNum;

}
