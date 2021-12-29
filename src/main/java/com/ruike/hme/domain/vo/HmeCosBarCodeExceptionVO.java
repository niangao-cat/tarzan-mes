package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * COS条码加工异常汇总返回
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/26 15:00
 */
@Data
public class HmeCosBarCodeExceptionVO implements Serializable {

    private static final long serialVersionUID = 4563749491472143022L;

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("工单芯片数")
    private BigDecimal qty;

    @ApiModelProperty("盒子号")
    private String materialLotCode;

    @ApiModelProperty("原始盒子号")
    private String sourceMaterialLotCode;

    @ApiModelProperty("WAFER")
    private String waferNum;

    @ApiModelProperty("COS类型")
    private String cosType;

    @ApiModelProperty("不良总数")
    private BigDecimal defectCountSum;

    @ApiModelProperty("不良类型")
    private String description;

    @ApiModelProperty("不良数量")
    private BigDecimal defectCount;

    @ApiModelProperty("热沉类型")
    private String heatSinkType;

    @ApiModelProperty("热沉批次")
    private String heatSinkLot;

    @ApiModelProperty("金线批次")
    private String goldWireLot;

    @ApiModelProperty("操作者")
    private String realName;

    @ApiModelProperty("岗位")
    private String workcellName;

    @ApiModelProperty("操作时间")
    private String creationDate;

    @ApiModelProperty("设备台机")
    private String assetEncoding;

    @ApiModelProperty("产品编码")
    private String materialCode;

    @ApiModelProperty("产品描述")
    private String materialName;

    @ApiModelProperty("位置")
    private String location;

    @ApiModelProperty("工序作业ID")
    private String jobId;
}
