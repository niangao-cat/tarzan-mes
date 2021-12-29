package com.ruike.hme.domain.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * description
 *
 * @author liyuan.lv@hand-china.com 2020/06/10 9:45
 */
@Data
public class HmeWorkOrderSnVO implements Serializable {

    private static final long serialVersionUID = -2607193432658485248L;
    private Long tenantId;
    private Long lineNumber;
    private String workOrderNum;
    private String snNumber;
    private String eoId;
    private String materialId;
    private String uomId;
}
