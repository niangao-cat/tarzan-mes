package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.util.*;

/**
 * @Classname HmeCosMaterialLotVO
 * @Description 物料批视图
 * @Date 2020/8/20 19:26
 * @Author yuchao.wang
 */
@Data
public class HmeCosMaterialLotVO extends MtMaterialLot {
    private static final long serialVersionUID = -593452540671403108L;

    @ApiModelProperty(value = "物料批扩展属性")
    Map<String, String> materialLotAttrMap;
}