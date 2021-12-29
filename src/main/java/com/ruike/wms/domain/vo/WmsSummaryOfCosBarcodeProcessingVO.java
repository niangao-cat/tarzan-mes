package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
public class WmsSummaryOfCosBarcodeProcessingVO implements Serializable {

    private static final long serialVersionUID = -1658498761312564300L;

    @ApiModelProperty("工单主键")
    private String workOrderId;

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("工单芯片数")
    private String qty;

    @ApiModelProperty("WAFER")
    private String waferNum;

    @ApiModelProperty("COS类型")
    @LovValue(lovCode = "HME_COS_TYPE", meaningField = "cosTypeMeaning")
    private String cosType;

    @ApiModelProperty("COS类型描述")
    private String cosTypeMeaning;

    @ApiModelProperty("盒子号")
    private String materialLotCode;

    @ApiModelProperty("条码id")
    private String materialLotId;

    @ApiModelProperty("设备Id")
    private String jobId;

    @ApiModelProperty("cos数量")
    private BigDecimal snQty;

    @ApiModelProperty("合格数量")
    private BigDecimal okQty;

    @ApiModelProperty("不良总数")
    private BigDecimal ngQty;

    @ApiModelProperty("热沉类型")
    private String sinkType;

    @ApiModelProperty("热沉条码")
    private String sinkCode;

    @ApiModelProperty("金线条码")
    private String goldCode;

    @ApiModelProperty("操作人")
    private String realName;

    @ApiModelProperty("操作人ID")
    private String createdBy;

    @ApiModelProperty("工位编码")
    private String workcellCode;

    @ApiModelProperty("工位编码ID")
    private String workcellId;

    @ApiModelProperty("工位描述")
    private String workcellName;

    @ApiModelProperty("时间")
    private String creationDate;

    @ApiModelProperty("设备编码")
    private String assetEncoding;

    @ApiModelProperty("产品编码")
    private String materialCode;

    @ApiModelProperty("产品描述")
    private String materialName;

}
