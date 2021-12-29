package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/10 17:00
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsReceiptDetailReqVO implements Serializable {

    private static final long serialVersionUID = 1176746919689822052L;

    @ApiModelProperty(value = "实物条码")
    private String materialLotCode;

    @ApiModelProperty(value = "条码状态")
    private String materialLotStatus;

    @ApiModelProperty(value = "质量状态")
    private String qualityStatus;

    @ApiModelProperty(value = "容器条码")
    private String container;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "入库时间从")
    private String receiptDateFrom;

    @ApiModelProperty(value = "入库时间至")
    private String receiptDateTo;

    @ApiModelProperty(value = "货位")
    private String locator;

    @ApiModelProperty(value = "行id")
    private List<String> instructionIdList;
}
