package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeEoJobSnBatchVO4;
import com.ruike.hme.domain.vo.HmeEoJobSnVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

@Data
public class HmeEoJobSnSingleDTO implements Serializable {
    private static final long serialVersionUID = 5805039234259540438L;
    @ApiModelProperty(value = "进站SN")
    HmeEoJobSnVO snLineDto;
    @ApiModelProperty(value = "勾选的进站SN的组件信息")
    List<HmeEoJobSnBatchVO4> componentList;

    @ApiModelProperty(value = "使用源数据标识")
    String useSourceFlag;

    @ApiModelProperty(value = "首序作业平台标识")
    String isFirstProcess;

    @ApiModelProperty(value = "泵浦源作业平台标识")
    String isPumpProcess;

    @ApiModelProperty(value = "设备首序作业平台标识")
    private String isEquipmentFirstProcess;
}
