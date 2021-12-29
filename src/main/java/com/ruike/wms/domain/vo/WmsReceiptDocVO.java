package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/10 10:26
 */
@Data
public class WmsReceiptDocVO implements Serializable {

    private static final long serialVersionUID = -3983314758216683713L;

    @ApiModelProperty(value = "指令头id")
    private String instructionDocId;

    @ApiModelProperty(value = "工厂")
    private String siteId;

    @ApiModelProperty(value = "工厂编码")
    private String siteCode;

    @ApiModelProperty(value = "工厂名称")
    private String siteName;

    @ApiModelProperty(value = "入库单号")
    private String receiptDocNum;

    @ApiModelProperty(value = "单据状态")
    @LovValue(lovCode = "WMS.RECEIPT_DOC_STATUS", meaningField = "docStatusMeaning")
    private String docStatus;

    @ApiModelProperty(value = "单据状态含义")
    private String docStatusMeaning;

    @ApiModelProperty(value = "创建时间")
    private Date creationDate;

    @ApiModelProperty(value = "创建人")
    private String createdBy;

    @ApiModelProperty(value = "创建人名称")
    private String createdByName;

    @ApiModelProperty(value = "备注")
    private String remark;
}
