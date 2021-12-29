package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * HmeEmployeeAttendanceDTO13
 *
 * @author chaonan.hu@hand-china.com 2021-03-15 09:48:32
 **/
@Data
public class HmeEmployeeAttendanceDTO13 implements Serializable {
    private static final long serialVersionUID = -6530068813308339088L;

    @ApiModelProperty(value = "产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "产线名称")
    private String prodLineName;

    @ApiModelProperty(value = "工段ID")
    private String lineWorkcellId;

    @ApiModelProperty(value = "工序ID")
    private String processId;

    @ApiModelProperty(value = "员工ID")
    private String userId;

    @ApiModelProperty(value = "员工ID集合,后端自用")
    private List<String> userIdList;

    @ApiModelProperty(value = "时间从", required = true)
    private Date dateFrom;

    @ApiModelProperty(value = "时间至", required = true)
    private Date dateTo;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料ID集合,后端自用")
    private List<String> materialIdList;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "物料版本集合,后端自用")
    private List<String> materialVersionList;

    @ApiModelProperty(value = "工位ID集合,后端自用")
    private List<String> workcellIdList;

}
