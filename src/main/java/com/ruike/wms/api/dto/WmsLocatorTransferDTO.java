package com.ruike.wms.api.dto;

import com.ruike.wms.domain.vo.WmsLocatorTransferVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 库位转移参数
 * @author: han.zhang
 * @create: 2020/05/08 18:25
 */
@Getter
@Setter
@ToString
public class WmsLocatorTransferDTO implements Serializable {
    private static final long serialVersionUID = -4804848270738480712L;
    @ApiModelProperty(value = "扫描的货位Id")
    private String locatorId;
    @ApiModelProperty(value = "扫描的条码id")
    private List<WmsLocatorTransferVO> materialLotList;
}