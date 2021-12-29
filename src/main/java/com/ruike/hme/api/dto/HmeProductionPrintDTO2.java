package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeProductionPrintVO3;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.order.api.dto.MtEoDTO5;

import java.io.Serializable;
import java.util.List;

/**
 * HmeProductionPrintDTO2
 *
 * @author: chaonan.hu@hand-china.com 2021/10/19 19:34
 **/
@Data
public class HmeProductionPrintDTO2 implements Serializable {
    private static final long serialVersionUID = -3801557488376442803L;

    @ApiModelProperty(value = "执行作业信息")
    private List<MtEoDTO5> eoList;

    @ApiModelProperty(value = "执行作业对应的InternalCode信息")
    private List<HmeProductionPrintVO3> eoInternalCodeList;
}
