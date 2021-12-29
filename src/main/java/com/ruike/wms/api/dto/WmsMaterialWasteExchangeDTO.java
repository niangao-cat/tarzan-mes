package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 料废调换  实物条码查询
 * @Author tong.li
 * @Date 2020/5/7 15:01
 * @Version 1.0
 */
@Data
public class WmsMaterialWasteExchangeDTO implements Serializable {
    private static final long serialVersionUID = -3286493771744721606L;

    @ApiModelProperty(value = "实物条码")
    private String barCode;

    @ApiModelProperty(value = "已扫描的实物条码列表")
    private List<String> scanedCodeList;

    @ApiModelProperty(value = "前台界面中查出的条码扫描区域信息")
    private WmsMaterialWasteExchangeDTO2 materialInfoList;

}
