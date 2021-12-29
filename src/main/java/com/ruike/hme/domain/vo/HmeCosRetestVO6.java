package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/20 14:40
 */
@Data
public class HmeCosRetestVO6 implements Serializable {

    private static final long serialVersionUID = -735030092144308701L;

    @ApiModelProperty(value = "容器类型")
    private String containerTypeCode;

    @ApiModelProperty(value = "容器类型ID")
    private String containerTypeId;

    @ApiModelProperty(value = "COS类型")
    @LovValue(lovCode = "HME_COS_TYPE", meaningField = "cosTypeName")
    private String cosType;

    @ApiModelProperty(value = "COS描述")
    private String cosTypeName;
}
