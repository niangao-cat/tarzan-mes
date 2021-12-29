package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.mybatis.common.query.Where;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: chaonan.hu@hand-china.com 2020-07-03 10:26:22
 **/
@Data
public class HmeNcCheckDTO implements Serializable {
    private static final long serialVersionUID = 2763034391131033791L;

    @ApiModelProperty(value = "站点Id", required = true)
    private String siteId;

    @ApiModelProperty(value = "产线Id")
    private String prodLineId;

    @ApiModelProperty(value = "工序Id")
    private String processId;

    @ApiModelProperty(value = "工位Id")
    private String workcellId;

    @ApiModelProperty(value = "序列号")
    private String materialLotCode;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "创建时间从")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String dateTimeFrom;

    @ApiModelProperty(value = "创建时间至")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String dateTimeTo;

    @ApiModelProperty(value = "序列号集合")
    private List<String> materialLotIdList;

    @ApiModelProperty(value = "产线Id集合")
    private List<String> workcellIdList;

    @ApiModelProperty(value = "不良单号")
    private String ncNumber;

    @ApiModelProperty(value = "不良单号(查询)")
    private String incidentNum;

    @ApiModelProperty(value = "处理人备注")
    private String disposeComment;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "处理办法")
    private String disposeMethod;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "不良组描述")
    private String ncGroupDesc;

    @ApiModelProperty(value = "不良类型")
    private String ncType;

    @ApiModelProperty(value = "事业部")
    private String areaId;

    @ApiModelProperty(value = "不良组ID")
    private String ncGroupId;

    @ApiModelProperty(value = "供应商ID")
    private String supplierId;

    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;

    @ApiModelProperty(value = "不良代码ID")
    private String ncCodeId;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "材料编码")
    private String componentMaterialId;
}
