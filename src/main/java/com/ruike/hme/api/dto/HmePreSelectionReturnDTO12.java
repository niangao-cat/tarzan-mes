package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @program: tarzan-mes->HmePreSelectionReturnDTO12
 * @description:
 * @author: chaonan.hu@hand-china.com 2020/01/12 15:06:12
 **/
@Data
public class HmePreSelectionReturnDTO12 implements Serializable {
    private static final long serialVersionUID = 4080591284668532513L;

    @ApiModelProperty(value = "物料批Id")
    private String materialLotId;
    @ApiModelProperty(value = "物料批Code")
    private String materialLotCode;
    @ApiModelProperty(value = "物料批Code")
    private String locatorCode;
    @ApiModelProperty(value = "数量")
    private String num;
    @ApiModelProperty(value = "原盒位置")
    private String oldLoad;
    @ApiModelProperty(value = "热沉")
    private String hotSinkCode;
    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;
    @ApiModelProperty(value = "状态")
    @LovValue(value = "HME.SELECT_STATUS", meaningField = "statusMeaning")
    private String status;
    @ApiModelProperty(value = "状态")
    private String statusMeaning;
    @ApiModelProperty(value = "器件编号")
    private String deviceNumber;
    @ApiModelProperty(value = "路数")
    private String ways;
    @ApiModelProperty(value = "目标盒子")
    private String targetMaterialLotCode;
    @ApiModelProperty(value = "目标位置")
    private String targetLoad;
    @ApiModelProperty(value = "明细行Id")
    private String selectionDetailsId;
    @ApiModelProperty(value = "芯片序列号")
    private String loadSequence;
    @ApiModelProperty(value = "挑选时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;
    @ApiModelProperty(value = "挑选批次")
    private String selectLot;

}
