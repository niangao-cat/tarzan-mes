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
public class WmsContainerLineResultDTO implements Serializable {
    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "容器行ID")
    private String  containerLoadDetailId;

    @ApiModelProperty(value = "容器条码")
    private String containerCode;

    @ApiModelProperty(value = "容器ID")
    private String containerId;

    @ApiModelProperty(value = "容器类型")
    private String containerTypeDescription;

    @ApiModelProperty(value = "对象类型")
    @LovValue(lovCode = "Z.OBJECT_TYPE")
    private String loadObjectType;
    private String loadObjectTypeMeaning;

    @ApiModelProperty(value = "对象类型描述")
    private String loadObjectTypeDescription;

    @ApiModelProperty(value = "对象CODE")
    private String loadObjectCode;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "装载数量")
    private Integer loadQty;

    @ApiModelProperty(value = "创建时间")
    private Date creationDate;

    @ApiModelProperty(value = "创建人")
    private String createdBy;

    @ApiModelProperty(value = "最后更新时间")
    private Date LastUpdateDate;

    @ApiModelProperty(value = "最后更新人")
    private String LastUpdatedBy;

    @ApiModelProperty(value = "单位")
    private String uom;

    @ApiModelProperty(value = "批次")
    private String lot;

    @ApiModelProperty(value = "物料描述")
    private String codeName;
}
