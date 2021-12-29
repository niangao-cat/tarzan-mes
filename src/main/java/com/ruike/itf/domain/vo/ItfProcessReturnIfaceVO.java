package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/28 16:30
 */
@Data
public class ItfProcessReturnIfaceVO implements Serializable {

    private static final long serialVersionUID = 4646733456918484647L;

    @ApiModelProperty(value = "结果,true代表成功,false代表失败")
    private Boolean result;

    @ApiModelProperty(value = "错误消息,当结果为false时才有错误消息")
    private String errorDescription;

    @ApiModelProperty("数量")
    private Integer sumEoCount;

    @ApiModelProperty("前工序")
    private String firstProcess;

    @ApiModelProperty("前序完工")
    private String firstProcessComplete;

    @ApiModelProperty("货位")
    private String locatorId;

    @ApiModelProperty(value = "标准时长")
    private BigDecimal standardReqdTimeInProcess;

    @ApiModelProperty(value = "进炉操作")
    private String siteInByName;

    @ApiModelProperty("下工序")
    private String nextProcess;

    @ApiModelProperty(value = "不良发起工序")
    private String ncRecordWorkcellCode;

    @ApiModelProperty(value = "不良发起工序描述")
    private String ncRecordWorkcellName;

    @ApiModelProperty(value = "实验备注")
    private String routerStepRemark;

    @ApiModelProperty(value = "实验代码")
    private String labCode;

    @ApiModelProperty(value = "条码信息")
    private List<ItfProcessReturnIfaceVO2> materialLotList;
}
