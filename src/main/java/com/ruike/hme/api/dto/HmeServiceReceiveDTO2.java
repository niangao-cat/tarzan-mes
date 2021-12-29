package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeServiceReceiveDTO2
 *
 * @author: chaonan.hu@hand-china.com 2020/9/1 15:59:42
 **/
@Data
public class HmeServiceReceiveDTO2 implements Serializable {
    private static final long serialVersionUID = -4720480808499530943L;

    @ApiModelProperty(value = "默认站点ID", required = true)
    private String siteId;

    @ApiModelProperty(value = "物流单ID", required = true)
    private String logisticsInfoId;

    @ApiModelProperty(value = "物料批及物料数据", required = true)
    private List<HmeServiceReceiveDTO3> materialDataList;

    @ApiModelProperty(value = "消息")
    private String message;
}
