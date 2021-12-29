package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * description
 *
 * @author li.zhang 2021/02/01 11:06
 */
@Data
public class HmeSsnInspectHeaderVO implements Serializable {

    private static final long serialVersionUID = -6954617193650307736L;

    @ApiModelProperty("头表ID")
    private String ssnInspectHeaderId;

    @ApiModelProperty("标准件编码")
    private String standardSnCode;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料描述")
    private String materialName;

    @ApiModelProperty("芯片类型")
    private String cosType;

    @ApiModelProperty("工作方式")
    @LovValue(lovCode = "HME.SSN_WORK_WAY", meaningField = "workWayMeaning")
    private String workWay;

    @ApiModelProperty("工作方式意义")
    private String workWayMeaning;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("工位编码")
    private String workcellCode;

    @ApiModelProperty("工位描述")
    private String workcellName;

    @ApiModelProperty("有效性")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty("有效性含义")
    private String enableFlagMeaning;

    @ApiModelProperty("变更人")
    private String lastUpdateByName;

    @ApiModelProperty("变更时间")
    private Date lastUpdateDate;
}
