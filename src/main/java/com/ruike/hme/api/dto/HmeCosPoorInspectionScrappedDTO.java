package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeCosPoorInspectionNcRecordDTO
 * @Description 报废入参
 * @Date 2020/8/20 21:13
 * @Author yuchao.wang
 */
@Data
public class HmeCosPoorInspectionScrappedDTO implements Serializable {
    private static final long serialVersionUID = 2327519608549296612L;

    @ApiModelProperty("物料批条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "装载表id")
    private String materialLotLoadId;

    @ApiModelProperty("工艺ID")
    private String operationId;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("工作单元ID")
    private String wkcShiftId;

    @ApiModelProperty("jobID")
    private String jobId;

    @ApiModelProperty(value = "装载表id集合")
    private List<String> materialLotLoadIdList;
}