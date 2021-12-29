package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 供应商接口表
 *
 * @author yapeng.yao@hand-china.com 2020/8/19 15:00
 */
@Data
public class ItfSupplierDTO {

    @ApiModelProperty(value = "供应商代码")
    private String LIFNR;
    @ApiModelProperty(value = "供应商名称")
    private String NAME1;
    @ApiModelProperty(value = "供应商简称")
    private String SORTL;
    @ApiModelProperty(value = "供应商地点编号")
    private String ADDRNUMBER;
    @ApiModelProperty(value = "供应商详细地址")
    private String STRAS;
    @ApiModelProperty(value = "国家")
    private String COUNTRY;
    @ApiModelProperty(value = "省份")
    private String REGION;
    @ApiModelProperty(value = "城市")
    private String ORT01;
    @ApiModelProperty(value = "联系电话")
    private String TEL_NUMBER;
    @ApiModelProperty(value = "联系人")
    private String BUILDING;
    @ApiModelProperty(value = "地点生效日期")
    private String DATE_FROM;
    @ApiModelProperty(value = "地点失效日期")
    private String DATE_TO;
    @ApiModelProperty(value = "ERP创建日期")
    private String ERDAT;

}
