package com.ruike.itf.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/6 17:51
 */
@Data
public class ItfStocktakeVO2 implements Serializable {

    private static final long serialVersionUID = -7942604693946548796L;

    @ApiModelProperty("设备id")
    @JsonIgnore
    private String equipmentId;
    @ApiModelProperty("资产编码")
    private String assetEncoding;
    @ApiModelProperty("资产名称")
    private String assetName;
    @ApiModelProperty("机身序列号")
    private String equipmentBodyNum;
    @ApiModelProperty("保管部门")
    private String businessName;
    @ApiModelProperty("型号")
    private String model;
    @ApiModelProperty("存放地点")
    private String location;
    @ApiModelProperty("使用频次")
    private String frequency;
    @ApiModelProperty("使用频次含义")
    private String equipmentStatusMeaning;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("盘点单Id")
    @JsonIgnore
    private String stocktakeId;
    @ApiModelProperty("盘点单")
    private String stocktakeNum;
    @ApiModelProperty("盘点标识（是/否）")
    private String stocktakeFlag;
    @ApiModelProperty("盘点时间")
    private String stocktakeDate;
    @ApiModelProperty("备用字段1")
    private String attribute1;
    @ApiModelProperty("备用字段2")
    private String attribute2;
    @ApiModelProperty("备用字段3")
    private String attribute3;
    @ApiModelProperty("备用字段4")
    private String attribute4;
    @ApiModelProperty("备用字段5")
    private String attribute5;
    @ApiModelProperty("租户")
    private Long tenantId;
    @ApiModelProperty("上传状态")
    private String uploadStatus;

    @ApiModelProperty("资产名称")
    @JsonIgnore
    private String assetClass;
}
