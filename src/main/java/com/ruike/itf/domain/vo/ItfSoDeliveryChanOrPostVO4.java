package com.ruike.itf.domain.vo;

import com.ruike.itf.domain.entity.ItfSoDeliveryChanLotDetailIface;
import com.ruike.itf.domain.entity.ItfSoDeliveryChanOrPostIface;
import com.ruike.itf.domain.entity.ItfSoDeliveryChanSnDetailIface;
import com.ruike.itf.domain.entity.ItfSoDeliveryLineChanIface;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/13 10:11
 */
@Data
public class ItfSoDeliveryChanOrPostVO4 implements Serializable {

    private static final long serialVersionUID = -3866645305075719918L;

    @ApiModelProperty("交货单修改过账头")
    private ItfSoDeliveryChanOrPostIface iface;
    @ApiModelProperty("交货单修改过账行")
    private List<ItfSoDeliveryLineChanIface> lineList;
    @ApiModelProperty("拣配批次明细")
    private List<ItfSoDeliveryChanLotDetailIface> lotDetailIfaceList;
    @ApiModelProperty("拣配SN明细")
    private List<ItfSoDeliveryChanSnDetailIface> snDetailIfaceList;
}
