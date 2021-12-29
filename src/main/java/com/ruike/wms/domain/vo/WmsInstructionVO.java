package com.ruike.wms.domain.vo;

import java.io.Serializable;
import java.util.List;

import com.ruike.wms.api.dto.WmsCodeIdentifyDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * WmsInstructionVO
 *
 * @author liyuan.lv@hand-china.com 2020/04/04 22:41
 */
@Data
public class WmsInstructionVO implements Serializable {

    private static final long serialVersionUID = -4512133452220027093L;
    @ApiModelProperty("单据ID")
    private String instructionDocId;
    @ApiModelProperty("单据Num")
    private String instructionDocNum;
    @ApiModelProperty("单据状态")
    @LovValue(value = "WMS.DELIVERY_DOC.STATUS",meaningField ="instructionDocStatusMeaning" )
    private String instructionDocStatus;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("条码")
    private String barCode;
    @ApiModelProperty("工厂id")
    private String siteId;
    @ApiModelProperty("工厂code")
    private String siteCode;
    @ApiModelProperty("工厂名称")
    private String siteName;
    @ApiModelProperty("供应商id")
    private String supplierId;
    @ApiModelProperty("供应商code")
    private String supplierCode;
    @ApiModelProperty("供应商名称")
    private String supplierName;
    @ApiModelProperty("货位id")
    private String locatorId;
    @ApiModelProperty("货位code")
    private String locatorCode;
    @ApiModelProperty("单位")
    private String uomCode;
    @ApiModelProperty("条码识别DTO")
    private WmsCodeIdentifyDTO wmsCodeIdentifyDTO;
    @ApiModelProperty("单据状态描述")
    private String instructionDocStatusMeaning;
    @ApiModelProperty("单据行")
    List<WmsInstructionLineVO> orderLineList;
    @ApiModelProperty("明细行")
    List<WmsInstructionLineVO> detailLineList;
}
