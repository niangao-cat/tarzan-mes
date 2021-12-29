package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 成品SN发货回传接口
 *
 * @author kejin.liu01@hand-china.com@hand-china.com 2020/9/4 11:36
 */
@Data
public class ItfSnSapIfaceDTO {

    @ApiModelProperty(value = "交货单")
    private String VBELN;
    @ApiModelProperty(value = "交货单行")
    private String POSNR;
    @ApiModelProperty(value = "物料编码")
    private String MATNR;
    @ApiModelProperty(value = "序列号")
    private String SERNR;
    @ApiModelProperty(value = "序列号状态")
    private String STTXT;
    @ApiModelProperty(value = "移动类型")
    private String BWART;
    @ApiModelProperty(value = "成功标志")
    private String ZFLAG;
    @ApiModelProperty(value = "回传信息")
    private String ZMESSAGE;
}
