package com.ruike.itf.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruike.itf.domain.entity.ItfSoDeliveryChanLotDetailIface;
import com.ruike.itf.domain.entity.ItfSoDeliveryChanSnDetailIface;
import com.ruike.itf.domain.entity.ItfSoDeliveryLineChanIface;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/12 21:04
 */
@Data
public class ItfSoDeliveryChanOrPostVO2 implements Serializable {

    private static final long serialVersionUID = -235401470353080743L;

    @ApiModelProperty("交货单修改过账接口行")
    private ItfSoDeliveryLineChanIface lineChanIface;
    @ApiModelProperty("拣配SN明细")
    private List<ItfSoDeliveryChanSnDetailIface> snDetailIfaceList;
    @ApiModelProperty("拣配批次明细")
    private List<ItfSoDeliveryChanLotDetailIface> lotDetailIfaceList;

    @ApiModelProperty("批次下标")
    @JsonIgnore
    private Integer instructionIndex;
}
