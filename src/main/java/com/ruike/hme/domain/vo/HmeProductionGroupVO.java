package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * HmeProductionGroupVO
 *
 * @author: chaonan.hu@hand-china.com 2021-05-27 14:39:25
 **/
@Data
public class HmeProductionGroupVO implements Serializable {
    private static final long serialVersionUID = -7482538673842632011L;

    @ApiModelProperty(value = "主键")
    private String productionGroupId;

    @ApiModelProperty(value = "站点ID")
    private String siteId;

    @ApiModelProperty(value = "站点编码")
    private String siteCode;

    @ApiModelProperty(value = "站点名称")
    private String siteName;

    @ApiModelProperty(value = "产品组编码")
    private String productionGroupCode;

    @ApiModelProperty(value = "产品组名称")
    private String description;

    @ApiModelProperty(value = "有效性")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty(value = "有效性含义")
    private String enableFlagMeaning;
}
