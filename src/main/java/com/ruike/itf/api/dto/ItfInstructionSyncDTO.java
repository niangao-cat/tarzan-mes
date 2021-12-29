package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
/**
 * 采购订单DTO
 *
 * @author kejin.liu01@hand-china.com 2020/8/12 9:18
 */

@Data
public class ItfInstructionSyncDTO {

    private List<ItfInstructionITEMSyncDTO> ITEM;

    private List<ItfInstructionRESBSyncDTO> RESB;

}
