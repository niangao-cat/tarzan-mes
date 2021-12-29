package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnReworkVO4 implements Serializable {
    private static final long serialVersionUID = 8154944874562292362L;
    @ApiModelProperty(value = "进站SN")
    private HmeEoJobSnVO snLine;
    @ApiModelProperty(value = "组件")
    private List<HmeEoJobSnReworkVO> componentList;
}
