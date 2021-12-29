package com.ruike.wms.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2020/10/20 16:17
 */
@Data
public class WmsBatchDistributionDemandVO {

    private String materialId;

    private String siteId;

    private String materialVersion;

    private String soNum;

    private String soLineNum;

    private BigDecimal inStockQty;

    private String workCellId;

}
