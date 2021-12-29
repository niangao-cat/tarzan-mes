package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/07/13 9:53
 */
@Data
public class WmsProductionReturnVO implements Serializable {

    private static final long serialVersionUID = 8478810658104511136L;

    @ApiModelProperty("单据ID")
    private String instructionDocId;
    @ApiModelProperty("单据编号")
    private String instructionDocNum;
    @ApiModelProperty("站点Id")
    private String siteId;
    @ApiModelProperty("站点名称")
    private String siteName;
    @ApiModelProperty("单据类型")
    private String instructionDocType;
    @ApiModelProperty("单据类型意义")
    private String instructionDocTypeMeaning;
    @ApiModelProperty("单据状态")
    private String instructionDocStatus;
    @ApiModelProperty("单据状态意义")
    private String instructionDocStatusMeaning;
    @ApiModelProperty("工单")
    private String workOrderNum;
    @ApiModelProperty("计划内/外")
    private String plant;
    @ApiModelProperty("单据行信息")
    private List<WmsProductionReturnInstructionVO> instructionList;

}
