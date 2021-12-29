package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeLoadJobDTO
 *
 * @author: chaonan.hu@hand-china.com 2021/2/3 14:04:12
 **/
@Data
public class HmeLoadJobDTO implements Serializable {
    private static final long serialVersionUID = -6922479790440986789L;

    @ApiModelProperty(value = "主键ID")
    private String loadJobId;

    @ApiModelProperty(value = "装载行序列号")
    private String loadSequence;

    @ApiModelProperty(value = "作业类型")
    @LovValue(lovCode = "HME_LOAD_JOB_TYPE",meaningField="loadJobTypeMeaning")
    private String loadJobType;

    @ApiModelProperty(value = "作业类型含义")
    private String loadJobTypeMeaning;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "条码ID")
    private String materialLotId;

    @ApiModelProperty(value = "条码编码")
    private String materialLotCode;

    @ApiModelProperty(value = "位置行")
    private String loadRow;

    @ApiModelProperty(value = "位置列")
    private String loadColumn;

    @ApiModelProperty(value = "位置")
    private String position;

    @ApiModelProperty(value = "来源条码ID")
    private String sourceMaterialLotId;

    @ApiModelProperty(value = "来源条码编码")
    private String sourceMaterialLotCode;

    @ApiModelProperty(value = "来源位置行")
    private String sourceLoadRow;

    @ApiModelProperty(value = "来源位置列")
    private String sourceLoadColumn;

    @ApiModelProperty(value = "来源位置")
    private String sourcePosition;

    @ApiModelProperty(value = "芯片数")
    private BigDecimal cosNum;

    @ApiModelProperty(value = "热沉编码")
    private String hotSinkCode;

    @ApiModelProperty(value = "状态")
    @LovValue(lovCode = "HME_LOAD_JOB_STATUS",meaningField="statusMeaning")
    private String status;

    @ApiModelProperty(value = "状态含义")
    private String statusMeaning;

    @ApiModelProperty(value = "工艺ID")
    private String operationId;

    @ApiModelProperty(value = "工艺编码")
    private String operationName;

    @ApiModelProperty(value = "工艺描述")
    private String description;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工单编码")
    private String workOrderNum;

    @ApiModelProperty(value = "wafer")
    private String waferNum;

    @ApiModelProperty(value = "COS类型")
    private String cosType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "投料物料ID")
    private String bomMaterialId;

    @ApiModelProperty(value = "投料物料编码")
    private String bomMaterialCode;

    @ApiModelProperty(value = "投料物料条码ID")
    private String bomMaterialLotId;

    @ApiModelProperty(value = "投料物料条码")
    private String bomMaterialLotCode;

    @ApiModelProperty(value = "投料物料条码供应商ID")
    private String bomMaterialLotSupplier;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "不良")
    private String nc;

    @ApiModelProperty(value = "设备")
    private String equipment;

    @ApiModelProperty(value = "金锡比")
    private String attribute13;

    @ApiModelProperty(value = "贴片机台编码")
    private String attribute10;

    @ApiModelProperty(value = "创建人")
    private String realName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date creationDate;

    @ApiModelProperty(value = "更新人")
    private String lastUpdateByName;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date lastUpdateDate;
}
