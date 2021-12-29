package com.ruike.wms.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/09 13:46
 */
@Data
public class WmsPurchaseOrderReceiptInspectionDTO implements Serializable {

    private static final long serialVersionUID = -2284687125328174011L;

    @ApiModelProperty("工厂Id")
    private String siteId;
    @ApiModelProperty("采购订单")
    private String instructionDocNum;
    @ApiModelProperty("送货单")
    private String deliveryInstructionDocNum;
    @ApiModelProperty("物料编码")
    private String materialId;
    @ApiModelProperty("供应商Id")
    private String supplierId;
    @ApiModelProperty("接收时间从")
    private String actualReceivedDateFrom;
    @ApiModelProperty("接收时间至")
    private String actualReceivedDateTo;
    @ApiModelProperty("计划时间从")
    private String demandTimeFrom;
    @ApiModelProperty("计划时间至")
    private String demandTimeTo;


    // 非前端传输参数

    @ApiModelProperty(value = "物料ID列表", hidden = true)
    @JsonIgnore
    private List<String> materialIdList;

    @ApiModelProperty(value = "供应商ID列表", hidden = true)
    @JsonIgnore
    private List<String> supplierIdList;

    public void initParam() {
        this.materialIdList = StringUtils.isBlank(this.materialId) ? null : Arrays.asList(StringUtils.split(this.materialId, ","));
        this.supplierIdList = StringUtils.isBlank(this.supplierId) ? null : Arrays.asList(StringUtils.split(this.supplierId, ","));
    }
}
