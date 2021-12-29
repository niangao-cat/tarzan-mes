package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-08-05 19:31
 */
@Data
public class WmsStockAllocateSettingDTO {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "主键")
    private String allocateSettingId;
    @ApiModelProperty(value = "组织")
    private String siteId;
    @ApiModelProperty(value = "组织编码")
    private String siteCode;
    @ApiModelProperty(value = "组织编码")
    private String siteName;
    @ApiModelProperty(value = "来源仓库id")
    private String fromLocatorId;
    @ApiModelProperty(value = "来源仓库编码")
    private String fromLocatorCode;
    @ApiModelProperty(value = "来源仓库名称")
    private String fromLocatorName;
    @ApiModelProperty(value = "目标仓库id")
    private String toLocatorId;
    @ApiModelProperty(value = "目标仓库编码")
    private String toLocatorCode;
    @ApiModelProperty(value = "目标仓库名称")
    private String toLocatorName;
    @ApiModelProperty(value = "审批设置")
    @LovValue(value = "WMS.APPRO_SETTING", meaningField = "approveSettingMeaning")
    private String approveSetting;
    @ApiModelProperty(value = "审批设置说明")
    private String approveSettingMeaning;

}
