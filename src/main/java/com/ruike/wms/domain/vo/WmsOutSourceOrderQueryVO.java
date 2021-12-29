package com.ruike.wms.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tarzan.instruction.domain.entity.MtInstructionDoc;

/**
 * @program: tarzan-mes
 * @description: 外协订单查询
 * @author: han.zhang
 * @create: 2020/06/18 10:49
 */
@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class WmsOutSourceOrderQueryVO extends MtInstructionDoc {

    @ApiModelProperty(value = "站点编码")
    private String siteCode;

    @ApiModelProperty(value = "站点描述")
    private String siteName;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商描述")
    private String supplierName;

    @ApiModelProperty(value = "创建人姓名")
    private String personName;

    @ApiModelProperty(value = "供应商站点id")
    private String supplierSiteId;

    @ApiModelProperty(value = "供应商站点编码")
    private String supplierSiteCode;

    @ApiModelProperty(value = "供应商站点名称")
    private String supplierSiteName;

    @ApiModelProperty(value = "备注")
    private String remark;


}