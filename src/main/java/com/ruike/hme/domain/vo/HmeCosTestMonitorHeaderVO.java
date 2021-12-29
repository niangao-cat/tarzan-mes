package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * COS测试良率监控头表 返回数据
 *
 * @author wengang.qiang@hand-china.com 2021/09/16 14:54
 */
@Data
public class HmeCosTestMonitorHeaderVO implements Serializable {

    private static final long serialVersionUID = -3705600251314289994L;

    @ApiModelProperty("主键")
    private String cosMonitorHeaderId;

    @ApiModelProperty(value = "监控单据号")
    private String monitorDocNum;

    @ApiModelProperty(value = "单据状态")
    @LovValue(lovCode = "HME.COS_DOC_STATUS", meaningField = "docStatusMeaning")
    private String docStatus;

    @LovValue(lovCode = "HME.COS_CHECK_STATUS", meaningField = "checkStatusMeaning")
    @ApiModelProperty(value = "审核状态")
    private String checkStatus;

    @ApiModelProperty(value = "cos类型")
    private String cosType;

    @ApiModelProperty(value = "WAFER")
    private String wafer;

    @ApiModelProperty(value = "cos良率")
    private BigDecimal testPassRate;

    @ApiModelProperty(value = "放行时间")
    private Date passDate;

    @ApiModelProperty(value = "放行人")
    private Long passBy;

    @ApiModelProperty(value = "放行人姓名")
    private String passByName;

    @ApiModelProperty(value = "单据状态描述")
    private String docStatusMeaning;

    @ApiModelProperty(value = "审核状态描述")
    private String checkStatusMeaning;

    @ApiModelProperty(value = "盒子号")
    private String materialLotId;

    @ApiModelProperty(value = "创建时间")
    private Date creationDate;

    @ApiModelProperty(value = "芯片物料编码")
    private String materialCode;

    @ApiModelProperty(value = "芯片物料描述")
    private String materialName;

    @ApiModelProperty(value = "测试数量")
    private Long testQty;
}
