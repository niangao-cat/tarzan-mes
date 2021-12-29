package com.ruike.hme.api.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 员工上下岗员工信息
 *
 * @author jianfeng.xia01@hand-china.com 2020-07-13 11:28:07
 */
@Data
public class HmeSignInOutRecordDTO3 implements Serializable {
	private static final long serialVersionUID = 7878732768964491855L;
	@ApiModelProperty(value = "工位ID")
    private String workcellId;
    @ApiModelProperty(value = "工位名称")
    private String workcellName;
    //@ApiModelProperty(value = "资质ID")
    //private String qualityId;
}
