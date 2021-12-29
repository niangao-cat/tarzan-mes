package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/07/07 14:56
 */
@Data
public class WmsProductReturnVO implements Serializable {

    private static final long serialVersionUID = -8045625617474889368L;

    @ApiModelProperty("单据ID")
    private String instructionDocId;
    @ApiModelProperty("单据编号")
    private String instructionDocNum;
    @ApiModelProperty("站点Id")
    private String siteId;
    @ApiModelProperty("工厂")
    private String plantCode;
    @ApiModelProperty("单据类型")
    private String docTypeId;
    @ApiModelProperty("单据类型意义")
    private String docType;
    @ApiModelProperty("单据号")
    private String docNumber;
    @ApiModelProperty("单据状态")
    private String docStatusId;
    @ApiModelProperty("单据状态意义")
    private String docStatus;
    @ApiModelProperty("客户Id")
    private String customerId;
    @ApiModelProperty("客户")
    private String customerName;
    @ApiModelProperty("客户站点Id")
    private String customerSiteId;
    @ApiModelProperty("单据行信息")
    private List<WmsProductReturnVO2> instructionList;
}
