package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.util.Date;

/**
 * 生产领退料单查询 : 传出参数（头信息）
 *
 * @author taowen.wang@hand-china.com
 * @version 1.0
 * @date 2021/7/8 16:01
 */
@Data
public class WmsInstructionDocsDTO {
    @ApiModelProperty(value = "头id")
    private String instructionDocId;
    @ApiModelProperty(value = "单号")
    private String instructionDocNum;
    @ApiModelProperty(value = "单据类型")
    @LovValue(lovCode = "WX.WMS.WO_IO_DM_TYPE" , meaningField = "instructionDocTypeMeaning")
    private String instructionDocType;
    @ApiModelProperty(value = "单据值集类型")
    private String instructionDocTypeMeaning;
    @ApiModelProperty(value = "单据状态")
    @LovValue(lovCode = "WX.WMS_C/R_DOC_STATUS" , meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;
    @ApiModelProperty(value = "单据值集状态")
    private String instructionDocStatusMeaning;
    @ApiModelProperty(value = "工厂ID")
    private String siteCode;
    @ApiModelProperty(value = "创建人")
    private String createdBy;
    @ApiModelProperty(value = "创建时间")
    private Date creationDate;
    @ApiModelProperty(value = "执行人")
    private String lastUpdatedBy;
    @ApiModelProperty(value = "执行时间")
    private Date lastUpdateDate;
    @ApiModelProperty(value = "头备注")
    @LovValue(lovCode = "WX.WMS.DEPARTMENT" , meaningField = "remarkMeaning")
    private String remark;
    @ApiModelProperty(value = "部门描述")
    private String remarkMeaning;
    @ApiModelProperty(value = "工单号")
    private String attrValue;
//    @ApiModelProperty(value = "计划内外")
//    private String inplanOutplan;

}
