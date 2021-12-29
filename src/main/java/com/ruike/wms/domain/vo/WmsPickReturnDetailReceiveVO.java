package com.ruike.wms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.*;

/**
 * @program: tarzan-mes
 * @description: 领退料明细查询返回VO
 * @author: han.zhang
 * @create: 2020/06/02 11:11
 */
@Getter
@Setter
@ToString
public class WmsPickReturnDetailReceiveVO implements Serializable {
    private static final long serialVersionUID = -1102444078121440321L;

    @ApiModelProperty(value = "")
    private String actualId;

    @ApiModelProperty(value = "物料批id")
    private String materialLotId;

    @ApiModelProperty(value = "执行数量")
    private String actualQty;

    @ApiModelProperty(value = "物料批条码")
    private String materialLotCode;

    @ApiModelProperty(value = "是否有效")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty(value = "是否有效翻译")
    private String enableFlagMeaning;

    @ApiModelProperty(value = "货位Id")
    private String locatorId;

    @ApiModelProperty(value = "货位编码")
    private String locatorCode;

    @ApiModelProperty(value = "仓库Id")
    private String storageId;

    @ApiModelProperty(value = "仓库编码")
    private String storageCode;

    @ApiModelProperty(value = "物料id")
    private String materialId;

    @ApiModelProperty(value = "批次")
    private String lot;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "状态")
    private String materialLotStatus;

    @ApiModelProperty(value = "状态含义")
    private String materialLotStatusMeaning;

    @ApiModelProperty(value = "上次更新人")
    private Long lastUpdatedBy;

    @ApiModelProperty(value = "上次更新人姓名")
    private String lastUpdatedByName;

    @ApiModelProperty(value = "上次更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateDate;

    @ApiModelProperty(value = "容器id")
    private String containerId;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;
}