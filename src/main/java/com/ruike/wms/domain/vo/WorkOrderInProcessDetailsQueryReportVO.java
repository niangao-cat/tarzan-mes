package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description 工单在制明细查询报表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @version 0.0.1
 * @return
 */
@Data
public class WorkOrderInProcessDetailsQueryReportVO implements Serializable {

    private static final long serialVersionUID = 5117709893672116182L;

    @ApiModelProperty("工单号")
    private String workOrderNum;

    @ApiModelProperty("工单物料编码")
    private String materialCode;

    @ApiModelProperty("工单物料名称")
    private String materialName;

    @ApiModelProperty("生产版本")
    private String productionVersion;

    @ApiModelProperty("产品组")
    private String itemGroupCode;

    @ApiModelProperty("产品组描述")
    private String itemGroupDescription;

    @ApiModelProperty("工单类型")
    private String typedesc;

    @ApiModelProperty("生产线编码")
    private String prodLineCode;

    @ApiModelProperty("生产线描述")
    private String prodlinedesc;

    @ApiModelProperty("工单状态")
    private String statusdesc;

    @ApiModelProperty("工单数量")
    private Long qty;

    @ApiModelProperty("下达数量")
    private Long releasedQty;

    @ApiModelProperty("完工数量")
    private Long completedQty;

    @ApiModelProperty("EO编码")
    private String eoNum;

    @ApiModelProperty("SN编码")
    private String identification;

    @ApiModelProperty("SN物料编码")
    private String snMaterialCode;

    @ApiModelProperty("SN物料描述")
    private String snMaterialName;

    @ApiModelProperty("序号")
    private String stepName;

    @ApiModelProperty("工艺步骤")
    private String rsdesc;

    @ApiModelProperty("当前工序编码")
    private String processCode;

    @ApiModelProperty("当前工序描述")
    private String processName;

    @ApiModelProperty("当前工位编码")
    private String workcellCode;

    @ApiModelProperty("当前工位描述")
    private String mwdesc;

    @ApiModelProperty("加工开始时间")
    private Date workingDate;

    @ApiModelProperty("加工结束时间")
    private Date completedDate;

    @ApiModelProperty("加工时长（分）")
    private BigDecimal timeDifference;

    @ApiModelProperty("呆滞时间")
    private String timeDifferenceStr;

    @ApiModelProperty("呆滞标准")
    private String timeStardand;

    @ApiModelProperty("呆滞标记")
    private String timeFlag;

    @ApiModelProperty("加工人员")
    private String realName;

    @ApiModelProperty(value = "产品状态")
    @LovValue(lovCode = "HME.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;

    @ApiModelProperty(value = "产品状态含义")
    private String qualityStatusMeaning;

    @ApiModelProperty("是否不良")
    private String reworkStepFlag;

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

    @ApiModelProperty(value = "最新不良代码项")
    private String latestNcTag;

}
