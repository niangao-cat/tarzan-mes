package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/10 10:35
 */
@Data
public class WmsReceiptDocReqVO implements Serializable {

    private static final long serialVersionUID = 2533627721718118783L;

    @ApiModelProperty(value = "入库单号")
    private String receiptDocNum;

    @ApiModelProperty(value = "工厂")
    private String siteId;

    @ApiModelProperty(value = "单据状态")
    private String docStatus;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "实物条码")
    private String materialLotCode;

    @ApiModelProperty(value = "创建时间从")
    private String creationDateFrom;

    @ApiModelProperty(value = "创建时间至")
    private String creationDateTo;

    @ApiModelProperty(value = "创建人")
    private String createdByName;
}
