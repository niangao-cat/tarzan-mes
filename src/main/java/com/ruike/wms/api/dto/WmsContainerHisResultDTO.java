package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsContainerHisResultDTO implements Serializable {
    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "容器条码")
    private String containerCode;

    @ApiModelProperty(value = "容器ID")
    private String containerId;

    @ApiModelProperty(value = "容器名称")
    private String containerName;

    @ApiModelProperty(value = "器具类型ID")
    private String containerTypeId;

    @ApiModelProperty(value = "器具类型")
    private String containerTypeDescription;

    @ApiModelProperty(value = "容器状态")
    @LovValue(value = "Z.CONTAINER.STATUS", meaningField = "containerStatusMeaning")
    private String containerStatus;

    @ApiModelProperty(value = "容器状态meaning")
    private String containerStatusMeaning;

    @ApiModelProperty(value = "货位ID")
    private String locatorId;

    @ApiModelProperty(value = "货位")
    private String locatorCode;

    @ApiModelProperty(value = "工厂ID")
    private String siteId;

    @ApiModelProperty(value = "工厂")
    private String siteCode;

    @ApiModelProperty(value = "所有者类型")
    private String ownerType;

    @ApiModelProperty(value = "所有者编码")
    private String ownerCode;

    @ApiModelProperty(value = "最后一次装载时间")
    private String lastLoadTime;

    @ApiModelProperty(value = "最后一次卸载时间")
    private String lastUnloadTime;

    @ApiModelProperty(value = "创建时间")
    private String creationDate;

    @ApiModelProperty(value = "创建人")
    private String createdBy;

    @ApiModelProperty(value = "最后更新时间")
    private String lastUpdateDate;

    @ApiModelProperty(value = "最后更新人")
    private String lastUpdatedBy;

    @ApiModelProperty(value = "事件")
    private String eventTypeCode;

    @ApiModelProperty(value = "事件创建时间")
    private String eventTime;

    @ApiModelProperty(value = "事件创建人")
    private String eventBy;

    @ApiModelProperty(value = "创建原因")
    private String creationReason;
}
