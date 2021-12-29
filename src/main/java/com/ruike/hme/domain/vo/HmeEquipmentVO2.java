package com.ruike.hme.domain.vo;

import java.io.Serializable;

import com.ruike.hme.domain.entity.HmeEquipment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * HmeEquipmentVO2
 *
 * @author liyuan.lv@hand-china.com 2020/03/18 0:08
 */
@Data
public class HmeEquipmentVO2 implements Serializable {

    private static final long serialVersionUID = -8448135939157899970L;
    @ApiModelProperty(value = "设备编码")
    private String assetEncoding;

    @ApiModelProperty(value = "设备描述")
    private String descriptions;

}
