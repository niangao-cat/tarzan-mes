package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 外协发货条码扫描的查询vo
 * @author: han.zhang
 * @create: 2020/06/22 09:48
 */
@Getter
@Setter
@ToString
public class WmsOutSourceScanMaterialQueryVO implements Serializable {
    private static final long serialVersionUID = 1638335961959874418L;

    @ApiModelProperty(value = "单据头id")
    private String instructionDocId;

//    @ApiModelProperty(value = "单据id")
//    private String instructionId;

    @ApiModelProperty(value = "物料条码")
    private String materialLotCode;
    @ApiModelProperty(value = "指令id")
    private String instructionId;
    @ApiModelProperty(value = "指令状态")
    @LovValue(lovCode = "WMS.OUTSOURCING_LINE_STATUS",meaningField="instructionStatusMeaning")
    private String instructionStatus;
    @ApiModelProperty(value = "指令状态含义")
    private String instructionStatusMeaning;

    @ApiModelProperty(value = "物料条码id")
    private List<String> materialLotIds;

    @ApiModelProperty(value = "取料货位Id")
    private String getMaterialLocatorId;
    @ApiModelProperty(value = "取料货位编码")
    private String getMaterialLocatorCode;

    @ApiModelProperty(value = "直接缓存标识")
    private Boolean cacheFlag;

    @ApiModelProperty(value = "行信息集合")
    private List<WmsOutSourceLineVO> docLineList;
}