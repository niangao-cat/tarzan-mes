package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO3;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeTimeProcessPdaDTO3
 *
 * @author chaonan.hu@hand-china.com 2020-08-20 10:18:37
 **/
@Data
public class HmeTimeProcessPdaDTO3 implements Serializable {
    private static final long serialVersionUID = 4034305923049690779L;

    @ApiModelProperty(value = "物料数据", required = true)
    private List<HmeTimeProcessPdaVO3> materialDataList;

    @ApiModelProperty(value = "班次日历Id", required = true)
    private String wkcShiftId;

    @ApiModelProperty(value = "工位Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "工艺路线ID列表", required = true)
    private List<String> operationIdList;

    @ApiModelProperty(value = "设备编码", required = true)
    private String equipmentCode;
}
