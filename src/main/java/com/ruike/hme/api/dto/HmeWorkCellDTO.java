package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName HmeWorkCellDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/14 18:05
 * @Version 1.0
 **/
@Data
public class HmeWorkCellDTO implements Serializable {
    private static final long serialVersionUID = -141583111889402796L;

    @ApiModelProperty(value = "工作单元Id")
    private String workcellId;
    @ApiModelProperty(value = "工作单元编号")
    private String workcellCode;
    @ApiModelProperty(value = "工作单元名称")
    private String workcellName;
    @ApiModelProperty(value = "岗位")
    private String unitId;
    @ApiModelProperty(value = "资质")
    private String qualityId;

}
