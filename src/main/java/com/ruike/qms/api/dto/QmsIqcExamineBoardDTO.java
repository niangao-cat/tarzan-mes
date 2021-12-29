package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.opensaml.xml.signature.Y;

import java.math.BigDecimal;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-05-06 10:37:10
 */
@Data
public class QmsIqcExamineBoardDTO {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "ICQ ID")
    private String iqcHeaderId;
    @ApiModelProperty(value = "ICQ编码")
    private String iqcNumber;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "单位名称")
    private String uomName;
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "检验时长")
    private BigDecimal inspTime;
    @ApiModelProperty(value = "检验员ID")
    private Long qcBy;
    @ApiModelProperty(value = "检验员名称")
    private String qcByName;
    @ApiModelProperty(value = "检验单标识")
    @LovValue(value = "QMS.IDENTIFICATION", meaningField = "identificationMeaning")
    private String identification;
    @ApiModelProperty(value = "检验单描述")
    private String identificationMeaning;
    @LovValue(value = "WMS.FLAG_YN", meaningField = "uaiFlagMeaning")
    @ApiModelProperty(value = "特采标识")
    private String uaiFlag;
    @ApiModelProperty(value = "特采描述")
    private String uaiFlagMeaning;
    @LovValue(value = "QMS.DOC_INSPECTION_TYPE", meaningField = "inspectionTypeMeaning")
    @ApiModelProperty(value = "检验单类型")
    private String inspectionType;
    @ApiModelProperty(value = "检验单类型描述")
    private String inspectionTypeMeaning;

}
