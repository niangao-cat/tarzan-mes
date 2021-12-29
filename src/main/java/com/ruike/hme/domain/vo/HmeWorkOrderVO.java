package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import lombok.Data;

/**
 * HmeWorkOrderVO
 *
 * @author liyuan.lv@hand-china.com 2020/06/29 17:35
 */
@Data
public class HmeWorkOrderVO implements Serializable {

    private static final long serialVersionUID = 8683096398437127726L;
    private String siteId;
    private String workOrderId;
    private String workOrderNum;
    private BigDecimal woQty;
    private String materialId;
    private String materialCode;
    private String materialName;
    private String remark;

    private String operationIdListStr;
    private List<String> operationIdList;
}
