package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeReportSetup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2020/10/22 14:16
 */
@Data
public class HmeReportSetupVO2 extends HmeReportSetup implements Serializable {

    private static final long serialVersionUID = -7524873231049078333L;

    @ApiModelProperty(value = "站点名称")
    private String siteName;

    @ApiModelProperty(value = "站点编码")
    private String siteCode;

    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "产线编码")
    private String prodLineName;

    @ApiModelProperty(value = "工段编码")
    private String workcellCode;

    @ApiModelProperty(value = "工段编码")
    private String workcellName;

    @ApiModelProperty(value = "有效性含义")
    private String enableFlagMeaning;

    @ApiModelProperty(value = "看板类型含义")
    private String reportTypeMeaning;
}
