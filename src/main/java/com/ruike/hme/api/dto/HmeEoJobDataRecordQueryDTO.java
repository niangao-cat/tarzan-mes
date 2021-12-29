package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeEoJobDataRecordQueryDTO2
 * @Description 数据采集项查询参数
 * @Date 2020/11/23 18:37
 * @Author yuchao.wang
 */
@Data
public class HmeEoJobDataRecordQueryDTO implements Serializable {
    private static final long serialVersionUID = -3158035139286105481L;

    @ApiModelProperty(value = "jobId")
    private String jobId;

    @ApiModelProperty(value = "jobContainerId")
    private String jobContainerId;

    @ApiModelProperty(value = "条码SN")
    private String materialLotCode;

    @ApiModelProperty(value = "容器SN")
    private String containerCode;

    @ApiModelProperty(value = "是否时效达标")
    private String timeStandardFlag;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "工艺ID")
    private String operationId;

    @ApiModelProperty(value = "设备编码")
    private String equipmentCode;

    @ApiModelProperty(value = "采集项ID列表")
    private List<String> tagIdList;
}