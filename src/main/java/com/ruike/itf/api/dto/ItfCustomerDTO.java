package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 客户接口输入字段
 *
 * @author yapeng.yao@hand-china.com 2020/8/19 15:00
 */
@Data
public class ItfCustomerDTO {

    @ApiModelProperty(value = "客户编码")
    private String KUNNR;
    @ApiModelProperty(value = "客户描述1")
    private String NAME1;
    @ApiModelProperty(value = "客户简称")
    private String SORT1;
    @ApiModelProperty(value = "地点编码")
    private String ADRNR;
    @ApiModelProperty(value = "")
    private String ERDAT;
    @ApiModelProperty(value = "")
    private String UPDAT;
    @ApiModelProperty(value = "客户地址")
    private String STREET;
    @ApiModelProperty(value = "国家")
    private String LANDX;
    @ApiModelProperty(value = "省份")
    private String REGION;
    @ApiModelProperty(value = "城市")
    private String BEZEI;
    @ApiModelProperty(value = "联系电话")
    private String TEL_NUMBER;
    @ApiModelProperty(value = "联系人")
    private String BUILDING;
    @ApiModelProperty(value = "地点生效日期")
    private String DATE_FROM;
    @ApiModelProperty(value = "地点失效日期")
    private String DATE_TO;
}
