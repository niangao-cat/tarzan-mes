package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description COS条码加工汇总表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @version 0.0.1
 * @return
 */
@Data
public class WmsSummaryOfCosBarcodeProcessingDTO implements Serializable {

    private static final long serialVersionUID = 7459001122289550840L;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "工位")
    private String workcellCode;

    @ApiModelProperty(value = "工单")
    private String workOrderNum;

    @ApiModelProperty(value = "工单-ALL")
    private String workOrderNumAll;

    @ApiModelProperty(value = "COS类型")
    private String cosType;

    @ApiModelProperty(value = "WAFER")
    private String waferNum;

    @ApiModelProperty(value = "WAFER-ALL")
    private String waferNumALL;

    @ApiModelProperty(value = "产品编码ID")
    private String snMaterialId;

    @ApiModelProperty(value = "操作人ID")
    private String createdBy;

    @ApiModelProperty(value = "操作人")
    private String realName;

    @ApiModelProperty(value = "盒子号")
    private String materialLotCode;

    @ApiModelProperty(value = "盒子号-ALL")
    private String materialLotCodeAll;

    @ApiModelProperty(value = "热沉条码")
    private String sinkCode;

    @ApiModelProperty(value = "金线条码")
    private String goldCode;

    @ApiModelProperty(value = "开始时间", required = true)
    private String creationDateStart;

    @ApiModelProperty(value = "结束时间", required = true)
    private String creationDateEnd;

    @ApiModelProperty(value = "类型")
    private List<String> jobTypes;

}
