package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * HmeEmployeeAttendanceExportVO4
 *
 * @author: chaonan.hu@hand-china.com 2020/8/21 16:30:19
 **/
@Data
public class HmeEmployeeAttendanceExportVO4 implements Serializable {
    private static final long serialVersionUID = -5256066479753029942L;

    private String ncRecordId;

    private String workOrderId;

    private String workOrderNum;

    private String eoId;

    private String eoNum;

    private String materialLotId;

    private String materialLotCode;

    private String primaryUomQty;

    private Date dateTime;

    private String userId;

    private String userName;

    private String ncGroupId;

    private String description;

    private String incidentNumber;

    private List<String> ncCodeIdList;

    private List<String> ncCodeDescriptionList;

}
