package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeEquipmentMonitorVO10
 *
 * @author chaonan.hu@hand-china.com 2020/07/22 15:42:14
 */
@Data
public class HmeEquipmentMonitorVO10 implements Serializable {
    private static final long serialVersionUID = 7439421336944027993L;

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @ApiModelProperty(value = "原因ID")
    private String exceptionGroupId;

    @ApiModelProperty(value = "原因")
    private String exceptionGroupName;

    @ApiModelProperty(value = "停机时长")
    private String downTime;

    @ApiModelProperty(value = "关闭时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date closeTime;

    @ApiModelProperty(value = "处理人ID")
    private String respondedBy;

    @ApiModelProperty(value = "处理人名称")
    private String respondedByName;
}
