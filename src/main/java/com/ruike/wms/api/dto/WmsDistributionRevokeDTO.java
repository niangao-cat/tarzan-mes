package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName WmsDistributionRevokeDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/9 19:38
 * @Version 1.0
 **/
@Data
public class WmsDistributionRevokeDTO implements Serializable {
    private static final long serialVersionUID = 4861830219232811845L;

    @ApiModelProperty("单据头Id")
    private String instructionDocId;

    @ApiModelProperty("单据")
    private List<WmsDistributionRevokeDTO2>  list;

    @Data
    public static class WmsDistributionRevokeDTO2{

        @ApiModelProperty("单据行Id")
        private String instructionId;

        @ApiModelProperty("物料批Id")
        private String materialLotId;

        @ApiModelProperty("目标货位")
        private String toLocatorId;

        @ApiModelProperty("当前货位")
        private String locatorId;

        @ApiModelProperty("容器Id")
        private String containerId;
    }
}
