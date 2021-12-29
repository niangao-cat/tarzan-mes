package com.ruike.qms.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * QmsIqcExamineReportVO3
 *
 * @author: 胡超男
 **/
@Data
public class QmsIqcExamineReportVO3 implements Serializable {
    private static final long serialVersionUID = 1792890307146453980L;

    private List<String> xDataList;

    private List<BigDecimal> yDataList;
}
