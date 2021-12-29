package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeEoJobSn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/2/4 16:35
 */
@Data
public class HmeSsnInspectResultVO3 implements Serializable {

    private static final long serialVersionUID = 3460494516165130980L;

    @ApiModelProperty(value = "工位id")
    private String workcellId;

    @ApiModelProperty(value = "工位编码")
    private String workcellCode;

    @ApiModelProperty(value = "工艺")
    private String operationId;

    @ApiModelProperty(value = "标准件编码")
    private String standardSnCode;

    @ApiModelProperty(value = "物料id")
    private List<String> materialIdList;

    @ApiModelProperty(value = "芯片类型")
    private String cosType;

    @ApiModelProperty(value = "工作方式")
    private String workWay;

    @ApiModelProperty(value = "班组id")
    private String wkcShiftId;

    @ApiModelProperty(value = "标准检验项")
    private List<HmeSsnInspectResultVO2> inspectTagList;
}
