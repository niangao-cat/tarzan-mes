package com.ruike.hme.domain.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * HmeCompleteBoxingVO
 *
 * @author liyuan.lv@hand-china.com 2020/06/01 10:24
 */
@Data
public class HmeCompleteBoxingVO2 implements Serializable {

    private static final long serialVersionUID = -1707033450738983546L;
    private String siteId;
    private String workcellId;
    private String workcellCode;
    private String operationId;
}
