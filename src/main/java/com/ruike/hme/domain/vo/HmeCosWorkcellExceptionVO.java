package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * COS工位加工异常汇总表 页面展示
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/13 12:38
 */
@Data
public class HmeCosWorkcellExceptionVO implements Serializable {

    private static final long serialVersionUID = 743107993983845239L;

    public HmeCosWorkcellExceptionVO(String workOrderNum, String status, BigDecimal qty, String waferNum, String cosType, BigDecimal defectCountQuantity, String description, String realName, String workcellCode, String workcellName, String assetEncoding, String assetName, String materialCode, String materialName) {
        this.workOrderNum = workOrderNum;
        this.status = status;
        this.qty = qty;
        this.waferNum = waferNum;
        this.cosType = cosType;
        this.defectCountQuantity = defectCountQuantity;
        this.description = description;
        this.realName = realName;
        this.workcellCode = workcellCode;
        this.workcellName = workcellName;
        this.assetEncoding = assetEncoding;
        this.assetName = assetName;
        this.materialCode = materialCode;
        this.materialName = materialName;
    }

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("jobId")
    private String jobId;

    @ApiModelProperty("状态")
    @LovValue(value = "Z_MTLOT_ENABLE_FLAG", meaningField = "statusMeaning")
    private String status;

    @ApiModelProperty("状态含义")
    private String statusMeaning;

    @ApiModelProperty("工单芯片数")
    private BigDecimal qty;

    @ApiModelProperty("WAFER")
    private String waferNum;

    @ApiModelProperty("COS类型")
    private String cosType;

    @ApiModelProperty("不良总数")
    private BigDecimal defectCountQuantity;

    @ApiModelProperty("不良类型")
    private String description;

    @ApiModelProperty("不良数量")
    private BigDecimal defectCountSum;

    @ApiModelProperty("操作者")
    private String realName;

    @ApiModelProperty("工位编码")
    private String workcellCode;

    @ApiModelProperty("工位描述")
    private String workcellName;

    @ApiModelProperty("设备编码")
    private String assetEncoding;

    @ApiModelProperty("设别描述")
    private String assetName;

    @ApiModelProperty("产品编码")
    private String materialCode;

    @ApiModelProperty("产品描述")
    private String materialName;

}
