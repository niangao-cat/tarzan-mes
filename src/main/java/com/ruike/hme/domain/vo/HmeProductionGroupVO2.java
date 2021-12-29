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
public class HmeProductionGroupVO2 implements Serializable {
    private static final long serialVersionUID = -7482538673842632011L;

    @ApiModelProperty(value = "头ID")
    private String productionGroupId;

    @ApiModelProperty(value = "主键")
    private String productionGroupLineId;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "生产版本")
    private String productionVersion;

    @ApiModelProperty(value = "有效性")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty(value = "有效性含义")
    private String enableFlagMeaning;
}
