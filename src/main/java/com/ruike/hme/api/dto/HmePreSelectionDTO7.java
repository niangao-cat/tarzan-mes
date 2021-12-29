package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @ClassName HmePreSelectionDTO7
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/12/24 11:43
 * @Version 1.0
 **/
@Data
public class HmePreSelectionDTO7 implements Serializable {

    private static final long serialVersionUID = 42156369955731481L;
    @ApiModelProperty(value = "扫描的货位Id")
    private String locatorId;

    @ApiModelProperty(value = "条码")
    private String materialLotCode;

}
