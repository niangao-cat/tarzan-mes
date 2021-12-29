package com.ruike.hme.api.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.List;

/**
 * @author yaqiong.zhou@raycus.com 2021/9/14 8:43
 */
@Data
public class HmeRepairPermitJudgeDTO implements Serializable {
    private static final long serialVersionUID = 2229608154546405470L;

    @ApiModelProperty(value = "部门")
    private String departmentId;

    @ApiModelProperty(value = "SN号")
    private String snNum;

    @ApiModelProperty(value = "sn编码字符串")
    private String snNumList;

    @ApiModelProperty(value = "sn编码")
    private List<String> snNumArray;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty(value = "工序Id")
    private String workcellId;

    @ApiModelProperty(value = "返修状态")
    private String status;
}
