package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * COS筛选滞留表 输入
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/02/24 16:24
 */
@Data
public class HmeCosSelectionRetentionDTO implements Serializable {

    private static final long serialVersionUID = -4539709329813308056L;

    @ApiModelProperty(value = "仓库")
    private String parentLocatorId;

    @ApiModelProperty(value = "货位")
    private String locatorId;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "物料编码")
    private String materialId;

    @ApiModelProperty(value = "COS类型")
    private String cosType;

    @ApiModelProperty(value = "筛选状态")
    private String selectionStatus;

    @ApiModelProperty(value = "条码")
    private List<String> materialLotCodeList;

    @ApiModelProperty(value = "热沉编码")
    private List<String> hotSinkCodeList;

    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;

    @ApiModelProperty(value = "器件序列号")
    private String identification;

    @ApiModelProperty(value = "器件物料")
    private String deviceMaterialId;

    @ApiModelProperty(value = "WAFER")
    private List<String> waferList;

    @ApiModelProperty(value = "热沉供应商批次号")
    private String attribute6;

    @ApiModelProperty(value = "操作人")
    private String id;

    @ApiModelProperty(value = "操作时间从")
    private String timeStart;

    @ApiModelProperty(value = "操作时间从")
    private String timeTo;

    @ApiModelProperty(value = "电流点")
    private String current;

}
