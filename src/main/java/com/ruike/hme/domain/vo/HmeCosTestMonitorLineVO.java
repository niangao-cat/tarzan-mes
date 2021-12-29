package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * COS测试良率监控行表Mapper
 *
 * @author wengang.qiang@hand-china.com 2021/09/16 18:18
 */
@Data
public class HmeCosTestMonitorLineVO implements Serializable {

    private static final long serialVersionUID = 703351548586661028L;

    @ApiModelProperty(value = "行表主键")
    private String cosMonitorLineId;

    @ApiModelProperty(value = "盒子号id")
    private String materialLotId;

    @ApiModelProperty(value = "盒子号code")
    private String materialLotCode;

    @ApiModelProperty(value = "盒子审核状态")
    @LovValue(lovCode = "HME.COS_CHECK_STATUS", meaningField = "lineCheckStatusMeaning")
    private String lineCheckStatus;

    @ApiModelProperty(value = "盒子状态")
    @LovValue(lovCode = "HME.COS_DOC_STATUS", meaningField = "materialLotStatusMeaning")
    private String materialLotStatus;

    @ApiModelProperty(value = "放行时间")
    private Date passDate;

    @ApiModelProperty(value = "放行人")
    private Long passBy;

    @ApiModelProperty(value = "放行人姓名")
    private String passByName;

    @ApiModelProperty(value = "盒子审核状态描述")
    private String lineCheckStatusMeaning;

    @ApiModelProperty(value = "盒子状态描述")
    private String materialLotStatusMeaning;

    @ApiModelProperty(value = "cosType值")
    private String cosTypeValue;

    @ApiModelProperty(value = "waferNum值")
    private String waferNumValue;

}
