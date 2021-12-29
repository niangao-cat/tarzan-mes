package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeEquipmentMonitorVO11
 *
 * @author chaonan.hu@hand-china.com 2020/07/22 16:18:53
 */
@Data
public class HmeEquipmentMonitorVO11 implements Serializable {
    private static final long serialVersionUID = 4197212667216190180L;

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @ApiModelProperty(value = "关闭日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date closeTime;

    @ApiModelProperty(value = "停机时长")
    private String downTime;

    @ApiModelProperty(value = "处理人ID")
    private String respondedBy;

    @ApiModelProperty(value = "处理人名称")
    private String respondedByName;

    @ApiModelProperty(value = "处理办法")
    private String respondRemark;

}
