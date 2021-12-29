package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName WmsPoDeliveryScanDTO2
 * @Deacription 送货单扫描实物条码
 * @Author ywz
 * @Date 2020/4/10 13:38
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class WmsPoDeliveryScanDTO2 implements Serializable {
    private static final long serialVersionUID = -6809443639021071639L;
    @ApiModelProperty(value = "实物条码号")
    private String materialLotCode;
    @ApiModelProperty(value = "送货单号")
    private String instructionDocNum;
    @ApiModelProperty(value = "接收批次号")
    private String number;
    private String barcode;
}
