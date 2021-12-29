package com.ruike.hme.api.controller.v1;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description 多模板打印
 *
 * @author penglin.sui@hand-china 2021/09/30 9:27
 */
@Data
public class HmeProductionPrintDTO implements Serializable {
    private static final long serialVersionUID = 4003691623391532662L;

    @ApiModelProperty(value = "物料序列号", required = true)
    private String snNum;

    @ApiModelProperty(value = "工单ID", required = true)
    private String workOrderId;

    @ApiModelProperty(value = "sap料号", required = true)
    private String sapMaterial;

    @ApiModelProperty(value = "打印张数")
    private Long printNumber;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "工位id")
    private String workcellId;

    @ApiModelProperty(value = "子条码")
    private String subCode;

    @ApiModelProperty(value = "扫描条码，逗号分隔")
    private String scanBarCode;

    @ApiModelProperty(value = "扫描条码集合")
    private List<String> scanBarCodeList;

    @ApiModelProperty(value = "模板路径")
    private String basePath;

    @ApiModelProperty(value = "uuid")
    private String uuid;

    @ApiModelProperty(value = "打印类型，0：多模板打印，1：进站打印，2：扫描打印")
    private String printType;

    @ApiModelProperty(value = "扫描打印信息")
    private List<HmeProductionPrintDTO> scanPrintDtoList;
}
