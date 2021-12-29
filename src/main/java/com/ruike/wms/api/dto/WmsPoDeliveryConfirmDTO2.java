package com.ruike.wms.api.dto;

import java.io.Serializable;

import com.ruike.wms.domain.entity.WmsDocLotRel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;

/**
 * @ClassName WmsPoDeliveryConfirmDTO
 * @Deacription TODO
 * @Author ywz
 * @Date 2020/4/14 11:17
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class WmsPoDeliveryConfirmDTO2 implements Serializable {

    private static final long serialVersionUID = -1937983087910544903L;

    @ApiModelProperty(value = "事件ID")
    private String eventId;
    @ApiModelProperty(value = "免检标识")
    private Boolean exemptionFlag;
    @ApiModelProperty(value = "事件ID")
    private String receivePendingLocatorId;
    @ApiModelProperty(value = "单据头")
    private MtInstructionDoc instructionDoc;
    @ApiModelProperty(value = "单据行")
    private MtInstruction instruction;
    @ApiModelProperty(value = "批次")
    private String lot;

}
