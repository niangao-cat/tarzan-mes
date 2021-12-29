package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeEquipmentMonitorVO8
 *
 * @author chaonan.hu@hand-china.com 2020/07/22 11:29:43
 */
@Data
public class HmeEquipmentMonitorVO8 implements Serializable {
    private static final long serialVersionUID = -5337961954280603702L;

    @ApiModelProperty(value = "序号")
    private long number;

    @ApiModelProperty(value = "记录ID")
    private String recordId;

    @ApiModelProperty(value = "设备ID")
    private String equipmentId;

    @ApiModelProperty(value = "设备编码")
    private String equipmentCode;

    @ApiModelProperty(value = "设备名称")
    private String equipmentName;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "工位名称")
    private String workcellName;

    @ApiModelProperty(value = "处理时长")
    private String disposeTime;

    @ApiModelProperty(value = "停机时长")
    private String downTime;
}
