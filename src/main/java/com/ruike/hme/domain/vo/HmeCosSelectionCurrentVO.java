package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * HmeCosSelectionCurrentVO
 *
 * @author: chaonan.hu@hand-china.com 2021/08/18 11:01
 **/
@Data
public class HmeCosSelectionCurrentVO implements Serializable {
    private static final long serialVersionUID = -7600455473802249957L;

    @ApiModelProperty("主键")
    private String cosId;

    @ApiModelProperty("COS类型")
    @LovValue(value = "HME_COS_TYPE", meaningField = "cosTypeMeaning")
    private String cosType;

    @ApiModelProperty("COS类型含义")
    private String cosTypeMeaning;

    @ApiModelProperty("电流")
    private String current;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("有效性")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "enbaleFlagMeaning")
    private String enbaleFlag;

    @ApiModelProperty("有效性含义")
    private String enbaleFlagMeaning;
}
