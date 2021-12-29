package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeCosGetChipSiteOutConfirmResponseDTO
 * @Description COS取片平台-出站确认返回数据
 * @Date 2020/8/19 11:48
 * @Author yuchao.wang
 */
@Data
public class HmeCosGetChipSiteOutConfirmResponseDTO implements Serializable {
    private static final long serialVersionUID = -2785196691946383711L;

    @ApiModelProperty("工单工艺工位在制记录ID")
    private String operationRecordId;

    @ApiModelProperty("物料批ID")
    private String materialLotId;

    @ApiModelProperty("物料批条码")
    private String materialLotCode;

    @ApiModelProperty("eoJobSnId")
    private String eoJobSnId;

    @ApiModelProperty("单盒待转移芯片数")
    private Long containerShipNum;

    @ApiModelProperty("装载表ID集合")
    private List<String> materialLotLoadList;
}