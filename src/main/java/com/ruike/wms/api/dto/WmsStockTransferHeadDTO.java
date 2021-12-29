package com.ruike.wms.api.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;


@Data
public class WmsStockTransferHeadDTO implements Serializable {

    private static final long serialVersionUID = 4727928066036049333L;

    @ApiModelProperty(value = "单据ID")
    private String instructionDocId;

    @ApiModelProperty(value = "单据号")
    private String instructionDocNum;

    @ApiModelProperty(value = "工厂ID")
    private String siteId;

    @ApiModelProperty(value = "工厂编码")
    private String siteCode;

    @ApiModelProperty(value = "工厂名称")
    private String siteName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "单据状态")
    @LovValue(value = "WMS.STOCK_ALLOCATION_DOC.STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;

    @ApiModelProperty(value = "单据类型")
    @LovValue(value = "WMS.STOCK_ALLOCATION_DOC.TYPE", meaningField = "instructionDocTypeMeaning")
    private String instructionDocType;

    @ApiModelProperty(value = "创建人")
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    private Date creationDate;

    @ApiModelProperty(value = "单据状态含义")
    private String instructionDocStatusMeaning;

    @ApiModelProperty(value = "单据类型")
    private String instructionDocTypeMeaning;

    @ApiModelProperty(value = "执行人姓名")
    private String createdUserName;

    @ApiModelProperty(value = "打印标识")
    @LovValue(value = "WMS.PRINT_FLAG", meaningField = "printFlagMeaning")
    private String printFlag;

    @ApiModelProperty(value = "打印标识含义")
    private String printFlagMeaning;
}

