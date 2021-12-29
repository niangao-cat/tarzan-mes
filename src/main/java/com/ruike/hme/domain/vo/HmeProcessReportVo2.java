package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 工序采集项报表返回VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/13 14:36
 */
@Data
public class HmeProcessReportVo2 implements Serializable {

    private static final long serialVersionUID = 7943257422274505178L;

    @ApiModelProperty(value = "EO_ID")
    private String eoId;

    @ApiModelProperty(value = "SN产品描述")
    private String materialName;

    @ApiModelProperty(value = "SN产品料号")
    private String materialCode;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "工单产品描述")
    private String woMaterialName;

    @ApiModelProperty(value = "工单产品料号")
    private String woMaterialCode;

    @ApiModelProperty(value = "序列号")
    private String identification;

    @ApiModelProperty(value = "工位")
    private String workcellName;

    @ApiModelProperty(value = "工序")
    private String processWorkcellName;

    @ApiModelProperty(value = "加工人")
    private String worker;

    @ApiModelProperty(value = "加工时间")
    private Date workTime;

    @ApiModelProperty(value = "出站时间")
    private Date siteOutDate;

    @ApiModelProperty(value = "进站人")
    private String siteInBy;

    @ApiModelProperty(value = "工位")
    private String workcellId;

    @ApiModelProperty(value = "质量状态")
    @LovValue(lovCode = "HME.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;

    @ApiModelProperty(value = "质量状态含义")
    private String qualityStatusMeaning;

    @ApiModelProperty(value = "eo状态")
    private String eoStatus;

    @ApiModelProperty(value = "eo状态含义")
    private String eoStatusMeaning;

    @ApiModelProperty(value = "是否冻结")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "freezeFlagMeaning")
    private String freezeFlag;

    @ApiModelProperty(value = "是否冻结含义")
    private String freezeFlagMeaning;

    @ApiModelProperty(value = "是否转型")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "transformFlagMeaning")
    private String transformFlag;

    @ApiModelProperty(value = "是否转型含义")
    private String transformFlagMeaning;

    @ApiModelProperty(value = "工位最新不良代码")
    private String latestNcTag;

    @ApiModelProperty(value = "不良发起时间")
    private Date ncDate;

    @ApiModelProperty(value = "班次日期")
    private Date shiftDate;

    @ApiModelProperty(value = "班次编码")
    private String shiftCode;

    @ApiModelProperty(value = "工序")
    private String processId;

    @ApiModelProperty(value = "jobId")
    private String jobId;

    @ApiModelProperty(value = "采集项")
    private List<HmeProcessCollectVO> processCollectList;

}
