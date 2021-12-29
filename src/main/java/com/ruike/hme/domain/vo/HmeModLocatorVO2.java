package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.modeling.domain.entity.MtModLocator;

@Data
public class HmeModLocatorVO2 extends MtModLocator implements Serializable {
    private static final long serialVersionUID = -1883535748065352910L;

    @ApiModelProperty("子库位ID")
    private String subLocatorId;
    @ApiModelProperty("子库位CODE")
    private String subLocatorCode;
}
