package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * QmsPqcReportVO
 *
 * @author: chaonan.hu@hand-china.com 2020/12/11 15:40:11
 **/
@Data
public class QmsPqcReportVO implements Serializable {
    private static final long serialVersionUID = -6314273636947700326L;

    @ApiModelProperty(value = "车间ID")
    private String areaId;

    @ApiModelProperty(value = "车间名称")
    private String areaName;

    @ApiModelProperty(value = "工序ID")
    private String processId;

    @ApiModelProperty(value = "工序名称")
    private String processName;

    @ApiModelProperty(value = "不合格数")
    private long ncNum;
}
