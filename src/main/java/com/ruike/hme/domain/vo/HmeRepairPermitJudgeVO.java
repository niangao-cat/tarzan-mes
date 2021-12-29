package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @author yaqiong.zhou@raycus.com 2021/9/14 8:52
 */
@Data
public class HmeRepairPermitJudgeVO implements Serializable {
    private static final long serialVersionUID = 8350656633694321231L;

    @ApiModelProperty(value = "主键Id")
    private String repairRecordId;

    @ApiModelProperty(value = "物料批Id")
    private String materialLotId;

    @ApiModelProperty(value = "SN号")
    private String materialLotCode;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "工序Id")
    private String workcellId;

    @ApiModelProperty(value = "工序编码")
    private String workcellCode;

    @ApiModelProperty(value = "工序名称")
    private String workcellName;

    @ApiModelProperty(value = "返修总次数")
    private Long repairCount;

    @ApiModelProperty(value = "限制次数")
    private Long limitCount;

    @ApiModelProperty(value = "每次放行时设置的返修次数")
    private Long permitCount;

    @ApiModelProperty(value = "放行次数")
    private Long passCount;

    @ApiModelProperty(value = "允许返修的总次数")
    private Long permitRepairCount;

    @LovValue(value = "HME.REPAIR_STATUS", meaningField ="statusMeaning")
    @ApiModelProperty(value = "返修状态")
    private String status;

    @ApiModelProperty(value = "返修状态含义")
    private String statusMeaning;

    @ApiModelProperty(value = "部门编码")
    private String departmentCode;

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "操作时间")
    private String lastUpdateDate;

    @ApiModelProperty("操作人userId")
    private Long lastUpdatedBy;

    @ApiModelProperty("操作人姓名")
    private String realName;

}
