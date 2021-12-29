package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/26 10:51
 */
@Data
public class ItfReworkIfaceVO implements Serializable {

    @ApiModelProperty(value = "结果,true代表成功,false代表失败")
    private Boolean result;

    @ApiModelProperty(value = "错误消息,当结果为false时才有错误消息")
    private String errorDescription;

    @ApiModelProperty(value = "当前工序")
    private String currentStepName;

    @ApiModelProperty(value = "当前工序描述")
    private String currentStepDescription;

    @ApiModelProperty(value = "下一道工序")
    private String nextStepName;

    @ApiModelProperty(value = "下一工序描述")
    private String nextStepDescription;

    @ApiModelProperty(value = "加工次数")
    private String eoStepNum;

    @ApiModelProperty(value = "已加工数量")
    private String woQuantityOut;

    @ApiModelProperty(value = "已预装数量")
    private String woQuantity;

    @ApiModelProperty(value = "产品型号")
    private String snMaterialCode;

    @ApiModelProperty(value = "生产版本")
    private String productionVersion;

    @ApiModelProperty(value = "开始时间")
    private String siteInDate;

    @ApiModelProperty(value = "完成时间")
    private String siteOutDate;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "节拍")
    private String meterTimeStr;

    @ApiModelProperty(value = "特殊需求")
    private String remark;

    @ApiModelProperty(value = "物料描述")
    private String snMaterialName;

    @ApiModelProperty(value = "sap物料描述")
    private String sapMaterialName;

    @ApiModelProperty(value = "sap物料编码")
    private String sapMaterialCode;

    @ApiModelProperty(value = "当前工位")
    private String workcellName;

    @ApiModelProperty(value = "工单类型")
    @LovValue(lovCode = "MT.WO_TYPE", meaningField = "workOrderTypeMeaning")
    private String workOrderType;

    @ApiModelProperty(value = "工单类型含义")
    private String workOrderTypeMeaning;

    @ApiModelProperty(value = "EO状态含义")
    private String eoStatusMeaning;

    @ApiModelProperty(value = "EO状态")
    private String eoStatus;

    @ApiModelProperty(value = "不良发起工序编码")
    private String ncRecordWorkcellCode;

    @ApiModelProperty(value = "不良发起工序名称")
    private String ncRecordWorkcellName;

    @ApiModelProperty(value = "不良信息")
    private String ncMsg;
}
