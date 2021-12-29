package com.ruike.wms.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/30 13:46
 */
@Data
public class WmsOutsourceOrderDetailsVO implements Serializable {

    private static final long serialVersionUID = 5042398093306897839L;

    private String instructionLineNum;

    private String barCodeStatus;

    private String barCodeStatusName;

    private String materialLotCode;

    private String materialVersion;

    private String materialVersionMeaning;

    private String qualityStatus;

    private String qualityStatusMeaning;

    private String materialCode;

    private String materialName;

    private BigDecimal actualQty;

    private String uomCode;

    private String lot;

    private String locatorCode;

    private String warehouseCode;

    private Date lastUpdateDate;

    private String lastUpdatedBy;

    private String lastUpdateByName;
}
