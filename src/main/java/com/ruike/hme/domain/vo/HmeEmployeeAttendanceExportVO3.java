package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeEmployeeAttendanceExportVO3
 *
 * @author: chaonan.hu@hand-china.com 2020/8/21 15:45:26
 **/
@Data
public class HmeEmployeeAttendanceExportVO3 implements Serializable {
    private static final long serialVersionUID = 681548450575394517L;

    private String jobId;

    private String workOrderId;

    private String workOrderNum;

    private String eoId;

    private String eoNum;

    private String materialLotId;

    private String primaryUomQty;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date siteInDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date siteOutDate;

    private BigDecimal processTime;

    private String identification;
}
