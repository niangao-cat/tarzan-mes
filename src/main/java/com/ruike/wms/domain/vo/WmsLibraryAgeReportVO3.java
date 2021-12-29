package com.ruike.wms.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * WmsLibraryAgeReportVO3
 *
 * @author: chaonan.hu@hand-china.com 2020/11/18 20:28:12
 **/
@Data
public class WmsLibraryAgeReportVO3 implements Serializable {
    private static final long serialVersionUID = -4679366801169886420L;

    private String title;

    private BigDecimal value;
}
