package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsContainerResultDTO implements Serializable {
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
    private Date lastLoadTime;

    @ApiModelProperty(value = "最后一次卸载时间")
    private Date lastUnloadTime;

    @ApiModelProperty(value = "创建时间")
    private Date creationDate;

    @ApiModelProperty(value = "创建人")
    private String createdBy;

    @ApiModelProperty(value = "最后更新时间")
    private Date LastUpdateDate;

    @ApiModelProperty(value = "最后更新人")
    private String LastUpdatedBy;

    @ApiModelProperty(value = "器具描述")
    private String description;

    @ApiModelProperty(value = "创建原因")
    private String creationReason;

    @ApiModelProperty(value = "仓库")
    private String wareHouse;

    @ApiModelProperty(value = "顶层容器ID")
    private String topContainerId;

    @ApiModelProperty(value = "顶层容器编码")
    private String topContainerCode;

    @ApiModelProperty(value = "上层容器ID")
    private String currentContainerId;

    @ApiModelProperty(value = "上层容器编码")
    private String currentContainerCode;
}
