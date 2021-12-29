package com.ruike.hme.domain.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * HmeEoJobWipVO3
 *
 * @author liyuan.lv@hand-china.com 2020/04/21 14:44
 */
@Data
public class HmeEoJobWipVO3 implements Serializable {


    private static final long serialVersionUID = 8437404643687625900L;
    private String materialCode;
    private String materialName;
    private String uomName;
    private Double workingQtySum;
    private Double completedQtySum;
    private Double queueQtySum;
}
