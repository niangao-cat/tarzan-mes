package com.ruike.hme.domain.vo;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * 作者：ruijie.wang01@hand-china.com
 * 时间：2020/8/13 16:37
 */
@Data
public class HmeEquipmentVO3 implements Serializable {

    private static final long serialVersionUID = 1914770767630405605L;

    @ApiModelProperty(value = "设备名称")
    private String assetName;

    @ApiModelProperty(value = "设备ID")
    private String equipmentId;

    @ApiModelProperty(value = "工位ID")
    private String stationId;

    @ApiModelProperty(value = "设备位置")
    private String workcellName;

    @ApiModelProperty(value = "设备商")
    private String supplier;

    @ApiModelProperty(value = "型号")
    private String model;

    @ApiModelProperty(value = "设备标识，true代表是设备，false代表是任务单号")
    private Boolean equipmentFlag;

    @ApiModelProperty(value = "类型标识，C代表是点检，M代表是保养")
    private String typeFlag;

    @ApiModelProperty(value = "任务单ID")
    private String taskDocId;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "工位Code")
    private String workcellCode;

}
