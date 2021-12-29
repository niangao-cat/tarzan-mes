package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruike.hme.domain.entity.HmeAfterSaleQuotationHeader;
import com.ruike.hme.domain.entity.HmeServiceReceive;
import com.ruike.hme.domain.entity.HmeServiceSplitRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * HmeAfterSaleQuotationHeaderVO3
 *
 * @author: chaonan.hu@hand-china.com 2021/09/26 16:02
 **/
@Data
public class HmeAfterSaleQuotationHeaderVO3 implements Serializable {
    private static final long serialVersionUID = -5608189620195635933L;

    @ApiModelProperty(value = "头部数据")
    private HmeAfterSaleQuotationHeaderVO6 headData;

    @ApiModelProperty(value = "光学物料行数据")
    private List<HmeAfterSaleQuotationHeaderVO4> opticsLineList;

    @ApiModelProperty(value = "电学物料行数据")
    private List<HmeAfterSaleQuotationHeaderVO4> electricLineList;

    @ApiModelProperty(value = "工时费物料行数据")
    private List<HmeAfterSaleQuotationHeaderVO4> hourFeeLineList;

    @ApiModelProperty(value = "光学物料无需更换标识")
    private String opticsNoFlag;

    @ApiModelProperty(value = "电学物料无需更换标识")
    private String electricNoFlag;

    @ApiModelProperty(value = "扫描SN对应的物料批ID")
    private String materialLotId;

    @ApiModelProperty(value = "删除的物料行数据")
    private List<HmeAfterSaleQuotationHeaderVO4> deleteLineList;

    @ApiModelProperty(value = "取消原因")
    private String cancelReason;
}
