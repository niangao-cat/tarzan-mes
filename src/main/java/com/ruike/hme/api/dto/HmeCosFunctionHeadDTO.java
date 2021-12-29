package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName HmeCosFunctionHeadDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/17 15:58
 * @Version 1.0
 **/
@Data
public class HmeCosFunctionHeadDTO implements Serializable {
    private static final long serialVersionUID = 7509839610429960908L;

    @ApiModelProperty(value = "芯片料号")
    private String materialCode;
    @ApiModelProperty(value = "芯片类型")
    @LovValue(lovCode = "HME_COS_TYPE", meaningField = "cosTypeMeaning")
    private String cosType;
    @ApiModelProperty(value = "芯片类型含义")
    private String cosTypeMeaning;
    @ApiModelProperty(value = "芯片序列号")
    private String loadSequence;
    @ApiModelProperty(value = "盒子号")
    private String materialLotCode;
    @ApiModelProperty(value = "盒子号")
    private String rowCloumn;
    @ApiModelProperty(value = "热沉")
    private String hotSinkCode;
    @ApiModelProperty(value = "库位")
    private String locatorCode;
    @ApiModelProperty(value = "状态")
    private String states;
    @ApiModelProperty(value = "wafer")
    private String wafer;
    @ApiModelProperty(value = "开始时间从")
    private Date startDate;
    @ApiModelProperty(value = "开始时间截止")
    private Date endDate;



}
