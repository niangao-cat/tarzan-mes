package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-04-27 09:50
 */
@Data
public class WmsInvTransferDTO {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "单据ID")
    private String instructionDocId;
    @ApiModelProperty(value = "单据编码")
    private String instructionDocNum;
    @ApiModelProperty(value = "单据类型")
    @LovValue(lovCode = "WMS.STOCK_ALLOCATION_DOC.TYPE",meaningField="typeMeaning",defaultMeaning = "无")
    private String instructionDocType;
    @ApiModelProperty(value = "单据类型说明")
    private String typeMeaning;
    @ApiModelProperty(value = "单据状态")
    @LovValue(lovCode = "WMS.STOCK_ALLOCATION_DOC.STATUS",meaningField="statusMeaning",defaultMeaning = "无")
    private String instructionDocStatus;
    @ApiModelProperty(value = "单据状态说明")
    private String statusMeaning;
    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "工厂编码")
    private String siteCode;
    @ApiModelProperty(value = "工厂名称")
    private String siteName;
    @ApiModelProperty(value = "备注")
    private String remark;

    private List<WmsInvTransferDTO2> docLineList;
}
