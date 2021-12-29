package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.*;
import java.util.*;

/**
 * @Classname HmeEoJobDataRecordQueryDTO2
 * @Description 数据采集项查询结果
 * @Date 2020/11/23 18:37
 * @Author yuchao.wang
 */
@Data
public class HmeEoJobDataRecordVO3 implements Serializable {
    private static final long serialVersionUID = 9073362500365062687L;

    @ApiModelProperty(value = "SN作业ID")
    private String jobId;

    @ApiModelProperty(value = "SN容器作业ID")
    private String jobContainerId;

    @ApiModelProperty(value = "容器ID")
    private String containerId;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @ApiModelProperty(value = "eoId")
    private String eoId;

    @ApiModelProperty(value = "工单Id")
    private String workOrderId;

    @ApiModelProperty(value = "物料Id")
    private String snMaterialId;

    @ApiModelProperty(value = "条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "条码编码")
    private String materialLotCode;

    @ApiModelProperty(value = "进站时间")
    private Date siteInDate;

    @ApiModelProperty(value = "标准时长")
    private BigDecimal standardReqdTimeInProcess;

    @ApiModelProperty(value = "炉内时长")
    private Long inSiteTime;

    @ApiModelProperty(value = "是否时效达标")
    private String timeStandardFlag;

}
