package com.ruike.hme.api.dto;

import com.ruike.hme.domain.entity.HmeEquipmentWkcRel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 设备工位关系维护查询DTO
 * @author: han.zhang
 * @create: 2020/06/29 15:35
 */
@Getter
@Setter
@ToString
public class HmeEquipmentWkcRelDTO extends HmeEquipmentWkcRel implements Serializable {

    @ApiModelProperty(value = "设备类别编码")
    private String assetEncoding;
}