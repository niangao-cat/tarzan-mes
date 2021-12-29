package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2020/10/22 11:25
 */
@Data
public class HmeReportSetupVO implements Serializable {

    private static final long serialVersionUID = -1955846299699961496L;

    private String reportType;

    private String reportName;

    private Integer sort;
}
