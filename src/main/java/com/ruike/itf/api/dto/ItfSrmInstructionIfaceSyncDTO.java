package com.ruike.itf.api.dto;

import com.ruike.itf.domain.entity.ItfSrmInstructionIface;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 采购订单状态回传接口
 *
 * @author kejin.liu01@hand-china.com 2020/9/17 19:07
 */
@Data
@CustomPrimary
public class ItfSrmInstructionIfaceSyncDTO {

    @ApiModelProperty(value = "送货单号")
    private String asnNumber;

    @ApiModelProperty(value = "送货单类型")
    private String asnTypeCode;

    @ApiModelProperty(value = "客户")
    private String vendorCode;

    @ApiModelProperty(value = "工厂")
    private String shipToOrganizationId;

    @ApiModelProperty(value = "送货单状态")
    private String asnStatus;

    @ApiModelProperty(value = "处理状态/SRM返回")
    private String status;

    @ApiModelProperty(value = "处理信息/SRM返回")
    private String message;

    public ItfSrmInstructionIfaceSyncDTO(ItfSrmInstructionIface iface) {
        this.asnNumber = iface.getInstructionDocNum();
        this.asnTypeCode = iface.getInstructionDocType();
        this.vendorCode = iface.getSupplierCode();
        this.shipToOrganizationId = iface.getPlantCode();
        this.asnStatus = iface.getInstructionDocStatus();
    }

}
