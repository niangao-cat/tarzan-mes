package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsContainerQryDTO implements Serializable {
    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "容器ID")
    private String containerId;

    @ApiModelProperty(value = "容器条码(Z.CONTAINER)")
    private String containerCode;

    @ApiModelProperty(value = "容器状态(Z.CONTAINER.STATUS)")
    private String containerStatus;

    @ApiModelProperty(value = "货位ID(MT.MTL_LOCATOR)")
    private String locatorId;

    @ApiModelProperty(value = "货位")
    private String locatorCode;

    @ApiModelProperty(value = "仓库ID(MT.WARE.HOUSE)")
    private String wareHouseId;

    @ApiModelProperty(value = "仓库")
    private String wareHouse;

    @ApiModelProperty(value = "关联物料(MT.MATERIAL)")
    private String materialId;

    @ApiModelProperty(value = "物料号")
    private String materialCode;

    @ApiModelProperty(value = "工厂ID(MT.MOD.SITE)")
    private String siteId;

    @ApiModelProperty(value = "工厂")
    private String siteCode;

    @ApiModelProperty(value = "器具类型(Z.CONTAINER_TYPE)")
    private String containerTypeId;

    @ApiModelProperty(value = "是否存在(Z.CONTAINER.ISEMPTY)")
    private String isEmpty;

    @ApiModelProperty(value = "物料批号")
    private String materialLotCode;

    @ApiModelProperty(value = "货位ID(MT.MTL_LOCATOR)", hidden = true)
    @JsonIgnore
    private List<String> locatorIdList;

    @ApiModelProperty(value = "仓库ID(MT.WARE.HOUSE)", hidden = true)
    @JsonIgnore
    private List<String> warehouseIdList;

    @ApiModelProperty(value = "关联物料(MT.MATERIAL)", hidden = true)
    @JsonIgnore
    private List<String> materialIdList;

    public void initParam() {
        this.locatorIdList = StringUtils.isNotBlank(this.locatorId) ? Arrays.asList(StringUtils.split(this.locatorId, ",")) : new ArrayList<>();
        this.warehouseIdList = StringUtils.isNotBlank(this.wareHouseId) ? Arrays.asList(StringUtils.split(this.wareHouseId, ",")) : new ArrayList<>();
        this.materialIdList = StringUtils.isNotBlank(this.materialId) ? Arrays.asList(StringUtils.split(this.materialId, ",")) : new ArrayList<>();
    }
}
