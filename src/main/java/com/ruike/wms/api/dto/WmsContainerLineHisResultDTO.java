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
public class WmsContainerLineHisResultDTO implements Serializable {
    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "容器条码")
    private String containerCode;

    @ApiModelProperty(value = "容器ID")
    private String containerId;

    @ApiModelProperty(value = "对象类型")
    private String loadObjectType;

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

    @ApiModelProperty(value = "本次变更数量")
    private Integer trxLoadQty;

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

    @ApiModelProperty(value = "物料描述")
    private String codeName;

}
