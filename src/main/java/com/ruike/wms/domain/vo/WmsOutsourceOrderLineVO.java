package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;
import tarzan.instruction.domain.entity.MtInstruction;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* @Classname WmsOutsourceOrderHeadVO
* @Description 外协管理平台 - 查询外协单行VO
* @Date  2020/6/11 19:54
* @Created by Deng xu
*/
@Getter
@Setter
@ToString
public class WmsOutsourceOrderLineVO implements Serializable {

    private static final long serialVersionUID = -9062103095780697832L;

    @ApiModelProperty("行号")
    private String instructionLineNum;

    @ApiModelProperty("单号")
    private String instructionDocNum;

    @ApiModelProperty("外协单头ID")
    private String sourceDocId;

    @ApiModelProperty("外协单行ID")
    private String instructionId;

    @ApiModelProperty("行类型")
    private String instructionType;

    @ApiModelProperty("物料ID")
    private String materialId;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料描述")
    private String materialName;

    @ApiModelProperty("物料版本")
    private String materialVersion;

    @ApiModelProperty("制单数量")
    private BigDecimal quantity;

    @ApiModelProperty("实际制单数量")
    private BigDecimal actualOrderedQty;

    @ApiModelProperty("单位ID")
    private String uomId;

    @ApiModelProperty("单位编码")
    private String uomCode;

    @ApiModelProperty("单位名称")
    private String uomName;

    @ApiModelProperty("行状态")
    @LovValue(value = "WMS.OUTSOURCING_LINE_STATUS",meaningField ="instructionStatusMeaning" )
    private String instructionStatus;

    @ApiModelProperty("行状态描述")
    private String instructionStatusMeaning;

    @ApiModelProperty("采购订单头ID")
    private String poId;

    @ApiModelProperty("采购订单行ID")
    private String poLineId;

    @ApiModelProperty("采购订单行号")
    private String poLineNum;

    @ApiModelProperty("发出仓库ID")
    private String fromLocatorId;

    @ApiModelProperty("发出仓库")
    private String fromLocatorCode;

    @ApiModelProperty("接收仓库ID")
    private String toLocatorId;

    @ApiModelProperty("接收仓库")
    private String toLocatorCode;

    @ApiModelProperty("已接收数量")
    private BigDecimal actualQty;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("库存数量")
    private BigDecimal inventoryQty;

}
