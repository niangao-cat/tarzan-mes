package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 售后返品拆机头信息
 *
 * @author jiangling.zheng@hand-china.com 2020-09-08 17:10:57
 */
@Data
public class HmeServiceSplitRecordDTO implements Serializable {

    private static final long serialVersionUID = 7362696386936130111L;

    @ApiModelProperty(value = "售后返品Id")
    private String splitRecordId;
    @ApiModelProperty(value = "顶层售后返品拆机Id")
    private String topSplitRecordId;
    @ApiModelProperty(value = "父级售后返品拆机Id")
    private String parentSplitRecordId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物流公司")
    @LovValue(value = "HME.LOGISTICS", meaningField = "logisticsCompanyMeaning")
    private String logisticsCompany;
    @ApiModelProperty(value = "物流公司说明")
    private String logisticsCompanyMeaning;
    @ApiModelProperty(value = "物流单号")
    private String logisticsNumber;
    @ApiModelProperty(value = "返回时间")
    private Date backDate;
    @ApiModelProperty(value = "签收人")
    private Long signForBy;
    @ApiModelProperty(value = "签收人名称")
    private String signForByName;
    @ApiModelProperty(value = "拆箱时间")
    private Date receiveDate;
    @ApiModelProperty(value = "拆箱人")
    private Long receiveBy;
    @ApiModelProperty(value = "拆箱人名称")
    private String receiveByName;
    @ApiModelProperty(value = "返回属性")
    @LovValue(value = "HME.BACK_TYPE", meaningField = "backTypeMeaning")
    private String backType;
    @ApiModelProperty(value = "返回属性说明")
    private String backTypeMeaning;
    @ApiModelProperty(value = "工单Id")
    private String workOrderId;
    @ApiModelProperty(value = "工单号")
    private String workOrderNum;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "拆机行信息")
    List<HmeServiceSplitRecordLineDTO> recordLineList;
}
