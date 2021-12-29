package com.ruike.reports.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName HmeDocSummaryQueryDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/2/25 16:25
 * @Version 1.0
 **/
@Data
public class HmeDocSummaryQueryDTO implements Serializable {
    private static final long serialVersionUID = -8531851454571361996L;
    @ApiModelProperty("单据类型")
    private String instructionDocType;

    @ApiModelProperty("单据号")
    private List<String> instructionDocNumList;

    @ApiModelProperty("发料仓库")
    private List<String> fromWarehouseCodeList;

    @ApiModelProperty("发料货位")
    private List<String> fromLocatorCodeList;

    @ApiModelProperty("收料仓库")
    private List<String> toWarehouseCodeList;

    @ApiModelProperty("收料货位")
    private List<String> toLocatorCodeList;

    @ApiModelProperty("成本中心")
    private String costCenterId;

    @ApiModelProperty("物料编码")
    private List<String> materialCodeList;

    @ApiModelProperty("制单人")
    private String userId;

    @ApiModelProperty("单据状态")
    private String instructionDocStatus;

    @ApiModelProperty("物料组")
    private String itemGroupId;

    @ApiModelProperty("制单时间起")
    private String creationDateFrom;

    @ApiModelProperty("制单时间至")
    private String creationDateFromTo;

    @ApiModelProperty("执行时间起")
    private String lastUpdateDateFrom;

    @ApiModelProperty("执行时间至")
    private String lastUpdateDateTo;

}
