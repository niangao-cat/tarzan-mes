package com.ruike.wms.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName WmsPoDeliveryScanReturnDTO2
 * @Deacription TODO
 * @Author ywz
 * @Date 2020/4/13 17:35
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class WmsPoDeliveryScanReturnDTO2 implements Serializable {
    private static final long serialVersionUID = -5279117914797806696L;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "数量")
    private Double materialQty;

    @ApiModelProperty(value = "单位")
    private String uomCode;

    @ApiModelProperty(value = "送货单状态")
    @LovValue(lovCode = "WMS.DELIVERY_DOC_LINE.STATUS", meaningField = "statusMeaning", defaultMeaning = "无")
    private String instructionStatus;

    @ApiModelProperty(value = "单据状态含义")
    private String statusMeaning;

    @ApiModelProperty(value = "本次执行数量")
    private BigDecimal coverQty;

    @ApiModelProperty(value = "本次料废数量")
    private BigDecimal exchangedQty;

    @ApiModelProperty(value = "扫描送货单信息")
    private WmsPoDeliveryScanReturnDTO WmsPoDeliveryScanReturnDTO;
}
