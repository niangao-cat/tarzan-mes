package com.ruike.itf.domain.vo;

import com.ruike.itf.domain.entity.ItfSoDeliveryChanLotDetailIface;
import com.ruike.itf.domain.entity.ItfSoDeliveryChanSnDetailIface;
import com.ruike.itf.domain.entity.ItfSoDeliveryLineChanIface;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/16 11:24
 */
@Data
public class ItfSoDeliveryChanOrPostVO5 implements Serializable {

    @ApiModelProperty("单据头ID")
    private String headerId;
    @ApiModelProperty("单据号")
    private String docNum;
    @ApiModelProperty("站点")
    private String siteCode;
    @ApiModelProperty("处理类型")
    private String type;

    @ApiModelProperty("交货单修改过账行")
    private List<ItfSoDeliveryLineChanIface> lineList;
    @ApiModelProperty("拣配批次明细")
    private List<ItfSoDeliveryChanLotDetailIface> lotDetailIfaceList;
    @ApiModelProperty("拣配SN明细")
    private List<ItfSoDeliveryChanSnDetailIface> snDetailIfaceList;
}
