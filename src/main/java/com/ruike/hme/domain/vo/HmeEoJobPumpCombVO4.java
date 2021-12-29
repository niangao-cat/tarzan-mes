package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeEoJobPumpCombVO3
 *
 * @author: chaonan.hu@hand-china.com 2021/08/25 14:32
 **/
@Data
public class HmeEoJobPumpCombVO4 implements Serializable {
    private static final long serialVersionUID = -1935392215776568390L;

    @ApiModelProperty(value = "jobId")
    private String jobId;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "工序ID")
    private String processId;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;

    @ApiModelProperty(value = "最后更新时间")
    private Date lastUpdateDate;
}
