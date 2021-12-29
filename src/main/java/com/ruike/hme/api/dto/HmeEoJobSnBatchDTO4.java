package com.ruike.hme.api.dto;

import java.io.Serializable;
import java.util.List;

import com.ruike.hme.domain.vo.HmeEoJobSnBatchVO4;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnBatchDTO4 implements Serializable {
    private static final long serialVersionUID = -5603390400299847494L;
    @ApiModelProperty(value = "勾选的进站SN")
    private HmeEoJobSnBatchDTO3 snLineListDto;
    @ApiModelProperty(value = "勾选的进站SN的组件信息")
    List<HmeEoJobSnBatchVO4> componentList;
}
