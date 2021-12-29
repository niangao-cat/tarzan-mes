package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO3;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeTimeProcessPdaDTO5
 *
 * @author chaonan.hu@hand-china.com 2020-08-20 11:03:26
 **/
@Data
public class HmeTimeProcessPdaDTO5 implements Serializable {
    private static final long serialVersionUID = -2233137700410475576L;

    @ApiModelProperty(value = "工位Id", required = true)
    private String workcellId;

    @ApiModelProperty(value = "设备编码")
    private String equipmentCode;

    @ApiModelProperty(value = "设备描述")
    private String equipmentName;

}
