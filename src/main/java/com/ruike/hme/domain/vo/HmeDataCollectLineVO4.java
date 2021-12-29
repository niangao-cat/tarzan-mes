package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 生产数据采集-工位扫描VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/20 12:56
 */
@Data
public class HmeDataCollectLineVO4 implements Serializable {

    private static final long serialVersionUID = 5293283720644252635L;

    @ApiModelProperty("工位Id")
    private String workcellId;

    @ApiModelProperty(value = "工作单元编号")
    private String workcellCode;

    @ApiModelProperty(value = "工作单元名称")
    private String workcellName;

    @ApiModelProperty(value = "工艺列表")
    private List<HmeDataCollectLineVO5> operationList;

    @ApiModelProperty(value = "产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "产线名称")
    private String prodLineName;
}
