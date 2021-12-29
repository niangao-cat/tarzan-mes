package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * HmePumpPreSelectionDTO4
 *
 * @author: chaonan.hu@hand-china.com 2021/08/31 15:38:21
 **/
@Data
public class HmePumpPreSelectionDTO4 implements Serializable {
    private static final long serialVersionUID = -8930615525939270761L;

    @ApiModelProperty(value = "目标容器编码", required = true)
    private String newContainerCode;

    @ApiModelProperty(value = "扫描条码集合", required = true)
    private List<String> materialLotCodeList;

    @ApiModelProperty(value = "工位ID", required = true)
    private String workcellId;

    @ApiModelProperty(value = "默认存储库位ID", required = true)
    private String defaultStorageLocatorId;
}
