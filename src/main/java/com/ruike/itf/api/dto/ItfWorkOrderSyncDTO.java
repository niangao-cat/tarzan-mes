package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import java.util.Objects;

/**
 * 物料接口传入DTO
 *
 * @author jiangling.zheng@hand-china.com 2020/7/21 15:41
 */
@Data
public class ItfWorkOrderSyncDTO {

    @ApiModelProperty(value = "工厂代码")
    private String plantCode;
    @ApiModelProperty(value = "物料编码")
    private String itemCode;
    @ApiModelProperty(value = "工单号")
    private String workOrderNum;
    @ApiModelProperty(value = "工单数量")
    private Double quantity;
    @ApiModelProperty(value = "工单类型")
    private String workOrderType;
    @ApiModelProperty(value = "工单状态")
    private String workOrderStatus;
    @ApiModelProperty(value = "NEW、RELEASED、HOLD、COMPLETED、CLOSED、ABANDON")
    private String scheduleStartDateStr;
    @ApiModelProperty(value = "计划开始时间")
    private String scheduleEndDateStr;
    @ApiModelProperty(value = "完工库位")
    private String completeLocator;
    @ApiModelProperty(value = "产线代码")
    private String prodLineCode;
    @ApiModelProperty(value = "生产版本")
    private String productionVersion;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "ERP创建日期")
    private String erpCreationDateStr;
    @ApiModelProperty(value = "ERP最后更新日期")
    private String erpLastUpdateDateStr;
    @ApiModelProperty(value = "")
    private String attribute1;
    @ApiModelProperty(value = "")
    private String attribute2;
    @ApiModelProperty(value = "")
    private String attribute3;
    @ApiModelProperty(value = "")
    private String attribute4;
    @ApiModelProperty(value = "")
    private String attribute5;
    @ApiModelProperty(value = "")
    private String attribute6;
    @ApiModelProperty(value = "")
    private String attribute7;
    @ApiModelProperty(value = "")
    private String attribute8;
    @ApiModelProperty(value = "")
    private String attribute9;
    @ApiModelProperty(value = "")
    private String attribute10;
    @ApiModelProperty(value = "")
    private String attribute11;
    @ApiModelProperty(value = "")
    private String attribute12;
    @ApiModelProperty(value = "")
    private String attribute13;
    @ApiModelProperty(value = "")
    private String attribute14;
    @ApiModelProperty(value = "")
    private String attribute15;
    private Double completeControlQty;
    private String completeControlType;


    public ItfWorkOrderSyncDTO(ItfSapIfaceDTO dto) {
        if (Objects.isNull(dto)) {
            return;
        }
        this.plantCode = dto.getDWERK();
        this.itemCode = dto.getMATNR();
        this.workOrderNum = dto.getAUFNR();
        this.workOrderStatus = dto.getZTEXT2();
        this.quantity = dto.getGAMNG();
        this.workOrderType = dto.getAUART();
        this.scheduleStartDateStr = dto.getGSTRP();
        this.scheduleEndDateStr = dto.getGLTRP();
        this.completeLocator = dto.getLGORT();
        this.productionVersion = dto.getVERID();
        this.remark = dto.getZBZHU();
        this.completeControlQty = Objects.isNull(dto.getZUEETO()) ? 0.0D : dto.getZUEETO();
        this.completeControlType = "PERCENT";
        this.attribute1 = dto.getKDAUF();
        this.attribute2 = dto.getZGNUM();
        this.attribute3 = dto.getVERID();
        this.attribute5 = dto.getFEVOR();
        this.attribute6 = dto.getGDNUM();
        this.attribute7 = Strings.isEmpty(dto.getKDPOS().replaceAll("^(0+)", "")) ? null : dto.getKDPOS();
        this.attribute8 = dto.getZTEXT();
        this.attribute9 = dto.getGLTRP();
        this.attribute10 = dto.getSTAT();
        this.attribute11 = dto.getTEXT1();
        this.attribute12 = dateAdd(dto.getERDAT(), dto.getERFZEIT());
        this.attribute13 = dateAdd(dto.getUDATE(), dto.getUTIME());
        this.attribute14 = Strings.isEmpty(dto.getKUNNR()) ? null : dto.getKUNNR().replaceAll("^(0+)", "");
        this.attribute15 = Strings.isEmpty(dto.getSTKTX()) ? null : dto.getSTKTX().replaceAll("^(0+)", "");

    }

    private String dateAdd(String str1, String str2) {
        if (Strings.isNotEmpty(str1) && Strings.isNotEmpty(str2)) {
//            String date = str1.replace("-", "");
//            String dateTime = date + str2;
//            String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
//            String newDate = dateTime.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
            String newDate = str1 + " " + str2;
            return newDate;
        }
        return "";
    }
}
