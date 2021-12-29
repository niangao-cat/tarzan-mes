package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * SN接口数据
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/24 4:17 下午
 */
@Data
@ApiModel("SN接口数据")
public class ItfWoSnRelIfaceVO implements Serializable {
    private static final long serialVersionUID = 7858471984028711305L;

    @ApiModelProperty("SN编号")
    private String SERNR;

    @ApiModelProperty("工单编码")
    private String AUFNR;

    @ApiModelProperty("工厂编码")
    private String WERKS;
}
