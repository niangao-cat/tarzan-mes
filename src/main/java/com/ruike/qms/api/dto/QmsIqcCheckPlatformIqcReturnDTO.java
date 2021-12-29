package com.ruike.qms.api.dto;

import com.ruike.qms.domain.entity.QmsIqcHeader;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description
 * @Author tong.li  IQC检验界面查询 返回信息
 * @Date 2020/5/12 14:40
 * @Version 1.0
 */
@Data
public class QmsIqcCheckPlatformIqcReturnDTO extends QmsIqcHeader implements Serializable {
    private static final long serialVersionUID = -7798539815202261446L;



    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "单位名称")
    private String uomName;

    @ApiModelProperty(value = "检验来源含义")
    private String docTypeMeaning;
    @ApiModelProperty(value = "检验方式含义")
    private String inspectionMethodMeaning;
    @ApiModelProperty(value = "加急标识含义")
    private String identificationMeaning;
    @ApiModelProperty(value = "检验类型含义")
    private String inspectionTypeMeaning;

    @ApiModelProperty(value = "检验状态含义")
    private String inspectionStatusMeaning;

    @ApiModelProperty(value = "检验结果含义")
    private String inspectionResultMeaning;


    @ApiModelProperty(value = "接收人姓名")
    private String receiptByName;

    @ApiModelProperty(value = "来源单号")
    private String fromDocNum;

    @LovValue(value = "QMS.IQC_INSPECTION_LEVELS", meaningField = "inspectionLevelsMeaning")
    @ApiModelProperty(value = "检验水平")
    private String inspectionLevels;

    @ApiModelProperty(value = "不良项数")
    private BigDecimal ncQty;

    @ApiModelProperty(value = "来源指令Id")
    private String inspectionId;

    @ApiModelProperty(value = "iqc版本")
    private String iqcVersion;

    @ApiModelProperty(value = "库位名称")
    private String locatorName;

    @ApiModelProperty(value = "库位编码")
    private String locatorCode;

}
