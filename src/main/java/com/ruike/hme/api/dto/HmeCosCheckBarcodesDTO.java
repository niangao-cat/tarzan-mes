package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * cos目检条码
 *
 * @author li.zhang 2021/01/19 11:32
 */
@Data
public class HmeCosCheckBarcodesDTO {

    @ApiModelProperty("工单")
    private List<String> workOrderNumList;

    @ApiModelProperty("目检工位")
    private String workcellId;

    @ApiModelProperty("COS类型")
    private String cosType;

    @ApiModelProperty("WAFER")
    private List<String> waferList;

    @ApiModelProperty("产品编码")
    private String materialId;

    @ApiModelProperty("操作人")
    private String operatorId;

    @ApiModelProperty("盒子号")
    private List<String> boxList;

    @ApiModelProperty("热沉编号")
    private String hotSinkCode;

    @ApiModelProperty("热沉投料条码")
    private List<String> barcodeList;

    @ApiModelProperty("测试机台")
    private List<String> benchList;

    @ApiModelProperty("贴片设备")
    private List<String> patchList;

    @ApiModelProperty("开始时间")
    private String creationDateFrom;

    @ApiModelProperty("结束时间")
    private String creationDateTo;

    @ApiModelProperty("不良代码")
    private String ncCodeId;

    @ApiModelProperty(value = "实验代码")
    private List<String> experimentCodeList;

}
