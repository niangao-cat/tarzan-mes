package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * @Description  DTO
 * @Author xuanyu.huang
 * @Date 11:55 上午 2020/4/21
 */
@ApiModel("调拨平台 DTO")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsStockTransferDTO implements Serializable {

    private static final long serialVersionUID = -2279117314797306296L;

    @ApiModelProperty(value = "单据号")
    private String instructionDocNum;
    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;

    @ApiModelProperty(value = "单据状态")
    private String instructionStatus;
    @ApiModelProperty(value = "物料id")
    private String materialId;
    @ApiModelProperty(value = "物料code")
    private String materialCode;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "来源工厂")
    private String fromSiteId;
    @ApiModelProperty(value = "来源工厂Code")
    private String fromSiteCode;
    @ApiModelProperty(value = "来源仓库")
    private String fromWarehouseId;
    @ApiModelProperty(value = "来源仓库Code")
    private String fromWarehouseCode;
    @ApiModelProperty("来源货位")
    private String fromLocatorCode;
    @ApiModelProperty("来源货位")
    private String fromLocatorId;


    @ApiModelProperty(value = "目标工厂")
    private String toSiteId;
    @ApiModelProperty(value = "目标工厂Code")
    private String toSiteCode;
    @ApiModelProperty(value = "目标仓库")
    private String toWarehouseId;
    @ApiModelProperty(value = "目标仓库Code")
    private String toWarehouseCode;
    @ApiModelProperty(value = "目标货位")
    private String toLocatorId;
    @ApiModelProperty(value = "目标货位")
    private String toLocatorCode;

    @ApiModelProperty(value = "创建时间从")
    private String creationDateFrom;
    @ApiModelProperty(value = "创建时间至")
    private String creationDateTo;


    @ApiModelProperty(value = "单据头id")
    private String sourceDocId;

    @ApiModelProperty(value = "制单人")
    private String createdBy;



    // 非前端传输参数

    @ApiModelProperty(value = "单据状态列表", hidden = true)
    @JsonIgnore
    private List<String> instructionStatusList;

    public void initParam() {
        this.instructionStatusList = StringUtils.isBlank(this.instructionStatus) ? null : Arrays.asList(StringUtils.split(this.instructionStatus, ","));
    }
}
