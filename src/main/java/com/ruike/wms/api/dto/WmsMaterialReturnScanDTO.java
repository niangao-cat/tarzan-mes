package com.ruike.wms.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;
import tarzan.instruction.domain.entity.MtInstruction;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName WmsMaterialReturnScanDTO
 * @Deacription  成本中心退料单扫描条码
 * @Author ywz
 * @Date 2020/4/20 10:18
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class WmsMaterialReturnScanDTO implements Serializable {
    private static final long serialVersionUID = 6184575912468020319L;

    @ApiModelProperty(value = "单据ID")
    private String instructionDocId;

    @ApiModelProperty(value = "单据状态")
    @LovValue(lovCode = "WMS.COST_CENTER_DOCUMENT.STATUS",meaningField="statusMeaning",defaultMeaning = "无")
    private String instructionDocStatus;

    @ApiModelProperty(value = "单据状态说明")
    private String statusMeaning;

    @ApiModelProperty(value = "单据类型")
    @LovValue(lovCode = "WMS.COST_CENTER_DOCUMENT.TYPE",meaningField="typeMeaning",defaultMeaning = "无")
    private String instructionDocType;

    @ApiModelProperty(value = "单据类型说明")
    private String typeMeaning;

    @ApiModelProperty(value = "工厂ID")
    private String siteId;

    @ApiModelProperty(value = "工厂")
    private String siteCode;

    @ApiModelProperty(value = "工厂名称")
    private String siteName;

    @ApiModelProperty(value = "成本中心ID")
    private String costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "成本中心/内部订单")
    private String settleAccounts;

    @ApiModelProperty(value = "成本中心/内部订单编码")
    private String costCenterCode;



    @ApiModelProperty(value = "行信息")
    private List<WmsMaterialReturnScanLineDTO> docLineList;

}
