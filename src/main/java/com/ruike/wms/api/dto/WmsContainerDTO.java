package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsContainerDTO implements Serializable {
    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "容器条码(Z.CONTAINER)")
    private String containerCode;

    @ApiModelProperty(value = "器具类型ID(Z.CONTAINER_TYPE)")
    private String containerTypeId;

    @ApiModelProperty(value = "器具类型Code(Z.CONTAINER_TYPE)")
    private String containerTypeCode;

    @ApiModelProperty(value = "容器名称")
    private String containerName;

    @ApiModelProperty(value = "容器描述")
    private String description;

    @ApiModelProperty(value = "工厂ID(MT.MOD.SITE)")
    private String siteId;

    @ApiModelProperty(value = "工厂")
    private String siteCode;

    @ApiModelProperty(value = "货位ID(MT.MTL_LOCATOR)")
    private String locatorId;

    @ApiModelProperty(value = "货位")
    private String locatorCode;

    @ApiModelProperty(value = "所有者类型(Z.CONTAINER.OWNER_TYPE)")
    private String ownerType;

    @ApiModelProperty(value = "所有者ID")
    private String ownerId;

    @ApiModelProperty(value = "所有者编码")
    private String ownerCode;

    @ApiModelProperty(value = "容器状态(Z.CONTAINER.STATUS)")
    private String containerStatus;

    @ApiModelProperty(value = "创建原因")
    private String createReason;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "批量创建数量")
    private Long batchNum;
}
