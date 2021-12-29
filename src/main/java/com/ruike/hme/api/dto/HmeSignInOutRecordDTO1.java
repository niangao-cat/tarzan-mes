package com.ruike.hme.api.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 员工上下岗员工信息
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-13 11:28:07
 */
@Data
public class HmeSignInOutRecordDTO1 implements Serializable {
	private static final long serialVersionUID = 7878732768964491855L;
    @ApiModelProperty(value = "租户ID")    
    private Long tenantId;
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "员工ID")
    private Long employeeId;
    @ApiModelProperty(value = "员工名称")
    private String employeeName;
    @ApiModelProperty(value = "主岗位ID")
    private Long unitId;
    @ApiModelProperty(value = "主岗位名称")
    private String unitName;
    @ApiModelProperty(value = "工位ID")
    private String workcellId;
    @ApiModelProperty(value = "工位名称")
    private String workcellName;
    @ApiModelProperty(value = "工位列表")
    List<HmeSignInOutRecordDTO3> workcellList;
}
