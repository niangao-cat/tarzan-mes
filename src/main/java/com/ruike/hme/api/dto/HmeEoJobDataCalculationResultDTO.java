package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeEoJobDataRecordVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeEoJobDataCalculationResultDTO
 * @Description 首序作业平台数据项结果计算
 * @Date 2020/9/30 11:33
 * @Author yuchao.wang
 */
@Data
public class HmeEoJobDataCalculationResultDTO implements Serializable {
    private static final long serialVersionUID = -7890384099366579743L;

    @ApiModelProperty(value = "eoId")
    private String eoId;

    @ApiModelProperty(value = "站点ID")
    private String siteId;

    @ApiModelProperty(value = "工艺ID")
    private String operationId;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;

    @ApiModelProperty(value = "返修标识")
    private String reworkFlag;


    @ApiModelProperty(value = "数据项列表")
    List<HmeEoJobDataRecordVO> eoJobDataRecordVOList;

    @ApiModelProperty(value = "使用源数据标识")
    String useSourceFlag;

    @ApiModelProperty(value = "设备首序工序作业平台标识")
    private String isEquipmentFirstProcess;
}