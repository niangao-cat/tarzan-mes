package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeEquipmentMonitorVO9
 *
 * @author chaonan.hu@hand-china.com 2020/07/22 14:30:26
 */
@Data
public class HmeEquipmentMonitorVO9 implements Serializable {
    private static final long serialVersionUID = -1356120579937629517L;

    @ApiModelProperty(value = "序号")
    private long number;

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

    @ApiModelProperty(value = "异常类型ID")
    private String exceptionGroupId;

    @ApiModelProperty(value = "异常类型名称")
    private String exceptionGroupName;

    @ApiModelProperty(value = "发现时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @ApiModelProperty(value = "响应时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date respondTime;

    @ApiModelProperty(value = "响应人ID")
    private String respondBy;

    @ApiModelProperty(value = "响应人名称")
    private String respondByName;

    @ApiModelProperty(value = "关闭时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date closeTime;

    @ApiModelProperty(value = "关闭人ID")
    private String closedBy;

    @ApiModelProperty(value = "关闭人名称")
    private String closedByName;
}
