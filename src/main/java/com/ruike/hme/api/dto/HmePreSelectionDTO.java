package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName HmePreSelectionDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/18 15:23
 * @Version 1.0
 **/
@Data
public class HmePreSelectionDTO implements Serializable {

    private static final long serialVersionUID = 6068817269591395834L;
    @ApiModelProperty(value = "sap料号")
    private String materialId;

    @ApiModelProperty(value = "产品类型")
    private String productType;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "工单状态")
    private String status;

    @ApiModelProperty(value = "计划交付时间从")
    private Date planEndTimeFrom;

    @ApiModelProperty(value = "计划交付时间至")
    private Date planEndTimeTo;

    @ApiModelProperty(value = "产线code")
    private List<String> prodLineCodeList;
}
