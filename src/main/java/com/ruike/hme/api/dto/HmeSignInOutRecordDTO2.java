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
public class HmeSignInOutRecordDTO2 implements Serializable {
	private static final long serialVersionUID = 7878732768964491855L;
    @ApiModelProperty(value = "岗位ID")
    private Long unitId;
    @ApiModelProperty(value = "岗位ID")
    private String unitName;
    @ApiModelProperty(value = "主岗位标识")
    private Integer primaryPositionFlag;

    @ApiModelProperty(value = "工位列表")
    List<HmeSignInOutRecordDTO3> workcellList;
}
