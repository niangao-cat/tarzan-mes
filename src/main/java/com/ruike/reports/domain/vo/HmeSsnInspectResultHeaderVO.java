package com.ruike.reports.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * 标准件检验结果头数据
 *
 * @author wenqiang.yin@hand-china.com 2021/02/04 8:31
 */
@Data
public class HmeSsnInspectResultHeaderVO implements Serializable {

    private static final long serialVersionUID = 5636986803820199523L;

    @ApiModelProperty("标准件检验结果头表Id")
    private String ssnInspectResultHeaderId;
    @ApiModelProperty("标准件编码")
    private String standardSnCode;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料描述")
    private String materialName;
    @ApiModelProperty("芯片类型")
    private String cosType;
    @ApiModelProperty("工作方式")
    @LovValue(value = "HME.SSN_WORK_WAY", meaningField = "workWayMeaning")
    private String workWay;
    @ApiModelProperty("工作方式含义")
    private String workWayMeaning;
    @ApiModelProperty("工位编码")
    private String workcellCode;
    @ApiModelProperty("工位描述")
    private String workcellName;
    @ApiModelProperty("日期")
    private String shiftDate;
    @ApiModelProperty("班次")
    private String shiftCode;
    @ApiModelProperty("检验人")
    private String realName;
    @ApiModelProperty("检验结果")
    private String result;
}
