package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeAfterSaleQuotationHeader;
import com.ruike.hme.domain.entity.HmeServiceReceive;
import com.ruike.hme.domain.entity.HmeServiceSplitRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeAfterSaleQuotationHeaderVO2
 *
 * @author: chaonan.hu@hand-china.com 2021/09/26 16:02
 **/
@Data
public class HmeAfterSaleQuotationHeaderVO2 implements Serializable {
    private static final long serialVersionUID = -5608189620195635933L;

    private HmeAfterSaleQuotationHeader hmeAfterSaleQuotationHeader;

    private HmeServiceReceive hmeServiceReceive;

    private HmeServiceSplitRecord hmeServiceSplitRecord;

    @ApiModelProperty(value = "上一次报价时间")
    private Date lastOfferDate;

    @ApiModelProperty(value = "扫描物料批的信息")
    private MtMaterialLot mtMaterialLot;
}
