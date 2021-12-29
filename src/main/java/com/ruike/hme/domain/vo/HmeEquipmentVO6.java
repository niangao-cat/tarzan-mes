package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/29 17:38
 */
@Data
public class HmeEquipmentVO6 implements Serializable {

    @ApiModelProperty(value = "设备列表")
    private List<HmeEquipmentVO> hmeEquipmentVOList;

    @ApiModelProperty(value = "打印类型")
    private String printType;
}
