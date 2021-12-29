package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName WmsDistributionRevokeReturnDTO4
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/9 15:15
 * @Version 1.0
 **/
@Data
public class WmsDistributionRevokeReturnDTO4 implements Serializable {
    private static final long serialVersionUID = 1353729376212052425L;

    @ApiModelProperty("物料批Id")
    private List<WmsDistributionRevokeReturnDTO41> list;

    @ApiModelProperty("执行数量")
    private Double qty;

    @ApiModelProperty("当前货位")
    private String locatorId;

    @ApiModelProperty("当前货位")
    private String locatorCode;

    @ApiModelProperty("当前库位描述")
    private String locatorName;

    @ApiModelProperty("来源库位Id")
    private String fromLocatorId;

    @ApiModelProperty("来源库位")
    private String fromLocatorCode;

    @ApiModelProperty("来源库位描述")
    private String fromLocatorName;

    @ApiModelProperty("容器Id")
    private String containerId;

    @Data
    public static class WmsDistributionRevokeReturnDTO41{
        @ApiModelProperty("配送单行Id")
        private String instructionId;

        @ApiModelProperty("物料批Id")
        private List<String> materialLotIdList;

        @ApiModelProperty("执行数量")
        private String qty;

    }

}
