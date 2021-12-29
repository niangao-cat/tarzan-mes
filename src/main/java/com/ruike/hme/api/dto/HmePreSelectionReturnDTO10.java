package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @program: tarzan-mes->HmePreSelectionReturnDTO10
 * @description:
 * @author: chaonan.hu@hand-china.com 2020/01/12 14:02:34
 **/
@Data
public class HmePreSelectionReturnDTO10 implements Serializable {
    private static final long serialVersionUID = -639574136203741624L;

    @ApiModelProperty(value = "电流")
    private String current;

    @ApiModelProperty(value = "采集项")
    @LovValue(value = "HME.COS_FUNCTION", meaningField = "collectionItemDesc")
    private String collectionItem;

    @ApiModelProperty(value = "采集项描述")
    private String collectionItemDesc;
}
