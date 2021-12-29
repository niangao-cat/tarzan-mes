package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: chaonan.hu@hand-china.com 2020-09-10 15:07:45
 **/
@Data
public class HmeNcDisposePlatformDTO27 implements Serializable {
    private static final long serialVersionUID = 2569443593638438066L;

    @ApiModelProperty(value = "不良材料清单临时表ID")
    private String ncComponentTempId;

    @ApiModelProperty(value = "条码数据")
    private List<HmeNcDisposePlatformDTO28> materialLotList;
}
