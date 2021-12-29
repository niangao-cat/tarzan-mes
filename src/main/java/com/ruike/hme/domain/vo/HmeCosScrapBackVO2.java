package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * COS报废撤回-查询结果VO
 *
 * @author sanfeng.zhang@hand-china.com 2021/1/26 10:05
 */
@Data
public class HmeCosScrapBackVO2 implements Serializable {

    private static final long serialVersionUID = 7630851047960526107L;

    @ApiModelProperty(value = "报废位置")
    private String scrapPosition;

    @ApiModelProperty(value = "装入位置")
    private String loadPosition;

    @ApiModelProperty(value = "报废记录主键")
    private String cosScrapId;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "条码")
    private String materialLotCode;

    @ApiModelProperty(value = "报废行")
    private String scrapRow;

    @ApiModelProperty(value = "报废列")
    private String scrapColumn;

    @ApiModelProperty(value = "装入行")
    private String loadRow;

    @ApiModelProperty(value = "装入列")
    private String loadColumn;

    @ApiModelProperty(value = "热沉号")
    private String hotSinkCode;

    @ApiModelProperty(value = "数量")
    private BigDecimal defectCount;

    @ApiModelProperty(value = "不良代码")
    private String ncCode;

    @ApiModelProperty(value = "不良代码")
    private String ncCodeDesc;

    @ApiModelProperty(value = "报废时间")
    private Date creationDate;

    @ApiModelProperty(value = "COS类型")
    private String cosType;

    @ApiModelProperty(value = "COS类型含义")
    @LovValue(lovCode = "HME_COS_TYPE", meaningField = "cosTypeMeaning")
    private String cosTypeMeaning;

    @ApiModelProperty(value = "WAFER")
    private String waferNum;

    @ApiModelProperty(value = "NC记录的组件")
    private String componentMaterialId;

    @ApiModelProperty(value = "芯片序列号")
    private String loadSequence;

    @ApiModelProperty(value = "工位")
    private String workcellCode;
}
